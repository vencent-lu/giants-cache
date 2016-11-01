/**
 * 
 */
package com.giants.cache.core.session;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.giants.cache.core.GiantsCacheManager;
import com.giants.cache.core.exception.UndefinedCacheModelException;
import com.giants.web.filter.AbstractFilter;

/**
 * @author vencent.lu
 *
 */
public class GiantsSessionFilter extends AbstractFilter {
	
	private String sessionIdName;
	private int sessionTimeout;
	private GiantsSessionCache giantsSessionCache;
	
	

	@Override
	public void init() throws ServletException {
		super.init();
		this.sessionIdName = this.findInitParameter("sessionIdName",
				"GSESSIONID");
		this.sessionTimeout = Integer.parseInt(this.findInitParameter(
				"sessionTimeout", "1800"));
		this.giantsSessionCache = GiantsCacheManager.getGiantsSessionCache();
		/*this.giantsSessionCache = WebApplicationContextUtils
				.getWebApplicationContext(this.getServletContext()).getBean(GiantsSessionCache.class);*/
	}



	/* (non-Javadoc)
	 * @see com.giants.web.filter.AbstractFilter#doFilter(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
				
		GiantsHttpSessionServletRequest giantsRequest = new GiantsHttpSessionServletRequest(
				this.getServletContext(), request, response,
				this.giantsSessionCache, this.sessionIdName,
				this.sessionTimeout);
		try{
			chain.doFilter(giantsRequest, response);
		} finally {
			GiantsSession session = giantsRequest.getGiantsSession();
			if (session != null) {
				if (session.isExpiring()) {
					try {
						this.giantsSessionCache.removeSession(session);
					} catch (UndefinedCacheModelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					boolean isNew = session.isNew();
					boolean isUpdate = session.isUpdate();
					session.endAccess();					
					if (isNew || isUpdate) {
						try {
							this.giantsSessionCache.putSession(session);
						} catch (UndefinedCacheModelException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (session.getMaxInactiveInterval() != -1) {
						try {
							this.giantsSessionCache.expireSession(session);
						} catch (UndefinedCacheModelException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}					
				}			
			}
		}		
	}

}
