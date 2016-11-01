/**
 * 
 */
package com.giants.cache.core.session;

import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.giants.cache.core.exception.UndefinedCacheModelException;

/**
 * @author vencent.lu
 *
 */
public class GiantsHttpSessionServletRequest extends HttpServletRequestWrapper {
	
	private String sessionIdName;
	private int sessionTimeout;
	private ServletContext servletContext;
	private HttpServletResponse response;
	private GiantsSession session;
	private boolean isHasGetSession = false;
	private GiantsSessionCache giantsSessionCache;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequestWrapper#getSession(boolean)
	 */
	public GiantsHttpSessionServletRequest(ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response,
			GiantsSessionCache giantsSessionCache, String sessionIdName,
			int sessionTimeout) {
		super(request);
		this.servletContext = servletContext;
		this.response = response;
		this.giantsSessionCache = giantsSessionCache;
		this.sessionIdName = sessionIdName;
		this.sessionIdName = sessionIdName;
		this.sessionTimeout = sessionTimeout;
	}
	
	public GiantsSession getGiantsSession() {
		return this.session;
	}

	@Override
	public HttpSession getSession(boolean create) {
		if (this.session != null) {
			return this.session;
		} else if (this.isHasGetSession && !create) {
			return null;
		}
		
		String sessionId = this.getSessionId();
		if (StringUtils.isNotEmpty(sessionId)) {
			this.isHasGetSession = true;
			try {
				this.session = (GiantsSession)this.giantsSessionCache.getSession(sessionId);
			} catch (UndefinedCacheModelException e) {
				e.printStackTrace();
			}
			if (this.session != null) {
				if (this.session.isValid()) {
					return this.session;
				} else {
					return null;
				}
			}
		}
		if (create) {
			this.session = new GiantsSession(UUID.randomUUID().toString()
					.replace("-", "").toUpperCase(), this.servletContext);
			this.session.setValid(true);
			this.session.setNew(true);
			this.session.setCreationTime(System.currentTimeMillis());
			this.session.setMaxInactiveInterval(this.sessionTimeout);
			Cookie cookie = new Cookie(this.sessionIdName, session.getId());
			cookie.setMaxAge(-1);
			cookie.setPath(new StringBuffer(this.getContextPath()).append('/').toString());
			response.addCookie(cookie);
			return this.session;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequestWrapper#getSession()
	 */
	@Override
	public HttpSession getSession() {
		return this.getSession(true);
	}
		
	public String getSessionIdCookieName() {
		return sessionIdName;
	}
	
	private String getSessionId() {
		Cookie[] cookies = this.getCookies();
		if (ArrayUtils.isNotEmpty(cookies)) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(this.sessionIdName)) {
					return cookie.getValue();
				}
			}
		}		
		return this.getParameter(this.sessionIdName);
	}	

}
