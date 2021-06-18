/**
 * 
 */
package com.giants.cache.core.filter;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.giants.cache.common.CacheConstants;
import com.giants.cache.config.elements.ServletCacheElement;
import com.giants.cache.config.elements.ServletCacheModel;
import com.giants.cache.core.CacheKey;
import com.giants.cache.core.Element;
import com.giants.cache.core.GiantsCacheManager;
import com.giants.cache.core.ServletCacheKey;
import com.giants.cache.core.exception.UndefinedCacheModelException;
import com.giants.web.filter.AbstractFilter;

/**
 * @author vencent.lu
 *
 */
public class GiantsCacheFilter extends AbstractFilter {
	
	private String cacheModelName;
	private String cacheConfigFilePath;
	private GiantsCacheManager giantsCacheManager;	

	/* (non-Javadoc)
	 * @see com.giants.web.filter.AbstractFilter#init()
	 */
	@Override
	public void init() throws ServletException {
		this.cacheModelName = this.findInitParameter("cacheModelName",
				"servlet").trim();
		this.cacheConfigFilePath = this.findInitParameter("cacheConfigFilePath",
				CacheConstants.DEFAULT_CONFIG_FILE_PATH).trim();
		this.giantsCacheManager = GiantsCacheManager.getInstance(this.cacheConfigFilePath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.giants.web.filter.AbstractFilter#doFilter(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (StringUtils.isEmpty(this.cacheConfigFilePath)
				|| this.giantsCacheManager == null
				|| this.giantsCacheManager.getCacheConfig() == null) {
			chain.doFilter(request, response);
			return;
		}
		String URI = request.getServletPath();
		ServletCacheModel cacheModel = ((ServletCacheModel) this.giantsCacheManager
				.getCacheConfig().getCacheModel(this.cacheModelName));
		if (cacheModel != null
				&& cacheModel.getPurgeServletCache() != null
				&& URI.startsWith(cacheModel.getPurgeServletCache()
						.getPurgeURIPrefix())) {
			if (cacheModel.getPurgeServletCache().allowPurge(
					request.getRemoteAddr())) {
				URI = URI.replaceFirst(cacheModel.getPurgeServletCache()
						.getPurgeURIPrefix(), "");
				ServletCacheElement cacheElement = this.giantsCacheManager
						.getServletCacheElementConf(this.cacheModelName, URI);
				if (cacheElement != null) {
					CacheKey cacheKey = this.createServletCacheKey(request,
							URI, cacheElement);
					try {
						Element element = this.giantsCacheManager
								.getCacheElement(cacheKey);
						if (element != null) {
							this.giantsCacheManager.removeCacheElement(element);
						}
					} catch (UndefinedCacheModelException e) {
						throw new ServletException(e);
					}
				}
				response.sendRedirect(new StringBuffer(request.getContextPath())
						.append(URI).toString());
			} else {
				response.sendError(403, "Don't allow access");
			}
		} else {
			ServletCacheElement cacheElement = this.giantsCacheManager
					.getServletCacheElementConf(this.cacheModelName, URI);
			if (cacheElement == null) {
				chain.doFilter(request, response);
				return;
			} else {
				CacheKey cacheKey = this.createServletCacheKey(request, URI,
						cacheElement);
				try {
					Element element = this.giantsCacheManager
							.getCacheElement(cacheKey);
					if (element == null) {
						ServletResultInfo servletResult = this
								.buildServletResult(request, response, chain);
						if (servletResult.isOk() && servletResult.getResponseBody().length > 0) {
							this.giantsCacheManager
									.putCacheElement(new Element(
											this.cacheModelName, cacheKey,
											servletResult, cacheElement
													.getTimeToLive()));
						}
						response.getOutputStream().write(
								servletResult.getResponseBody());
					} else if (element.isExpired()) {
						ServletResultInfo servletResult = this
								.buildServletResult(request, response, chain);
						if (servletResult.isOk() && servletResult.getResponseBody().length > 0) {
							element.update(servletResult);
						}
						response.getOutputStream().write(
								servletResult.getResponseBody());
					} else {
						this.writeResponse(request, response,
								(ServletResultInfo) element.getValue());
					}
				} catch (UndefinedCacheModelException e) {
					throw new ServletException(e);
				}
			}
		}
	}
	
	private ServletResultInfo buildServletResult(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		ByteArrayOutputStream outstr = new ByteArrayOutputStream();
		ResponseWrapper wrapper = new ResponseWrapper(response, outstr);
		chain.doFilter(request, wrapper);
		wrapper.flush();
		return new ServletResultInfo(wrapper.getStatus(),
				wrapper.getContentType(), wrapper.getCookies(),
				outstr.toByteArray(), wrapper.getAllHeaders());
	}
		
	@SuppressWarnings("unchecked")
	private ServletCacheKey createServletCacheKey(HttpServletRequest request,
			String URI, ServletCacheElement cacheElement) {
		ServletCacheKey servletCacheKey = new ServletCacheKey(
				this.cacheModelName, URI);
		if (cacheElement.isQueryParam()
				&& request.getParameterNames().hasMoreElements()) {
			Enumeration<String> parameterNames = request.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String parameterName = parameterNames.nextElement();
				if (cacheElement.isAllowAccordingParam(parameterName)) {
					servletCacheKey.setParameter(parameterName,
							request.getParameter(parameterName));
				}
			}
		}

		if (cacheElement.isCookie()) {
			Cookie[] cookies = request.getCookies();
			if (ArrayUtils.isNotEmpty(cookies)) {
				for (Cookie cookie : cookies) {
					if (cacheElement.isAllowCookieName(cookie.getName())) {
						servletCacheKey.setCookie(cookie.getName(), cookie.getValue());
					}
				}
			}
		}

		servletCacheKey.setElementConfName(cacheElement.getName());

		return servletCacheKey;
	}
	
	protected void writeResponse(HttpServletRequest request,
			HttpServletResponse response, ServletResultInfo resultInfo)
			throws IOException {
		response.setStatus(resultInfo.getStatusCode());
		if (StringUtils.isNotEmpty(resultInfo.getContentType())) {
			response.setContentType(resultInfo.getContentType());
		}

		List<SerializableCookie> cookies = resultInfo.getSerializableCookies();
		if (cookies != null) {
			for (SerializableCookie cookie : cookies) {
				response.addCookie(cookie.toCookie());
			}
		}

		this.setHeaders(resultInfo, response);
		
		response.setLocale(request.getLocale());

		response.setContentLength(resultInfo.getResponseBody().length);
		OutputStream out = new BufferedOutputStream(response.getOutputStream());
		out.write(resultInfo.getResponseBody());
		out.flush();
	}
    
	protected void setHeaders(ServletResultInfo resultInfo,
			HttpServletResponse response) {
		List<Header<? extends Serializable>> headers = resultInfo.getHeaders();
		TreeSet<String> setHeaders = new TreeSet<String>(
				String.CASE_INSENSITIVE_ORDER);
		for (Header<? extends Serializable> header : headers) {
			String name = header.getName();
			switch (header.getType()) {
			case STRING:
				if (setHeaders.contains(name)) {
					response.addHeader(name, (String) header.getValue());
				} else {
					setHeaders.add(name);
					response.setHeader(name, (String) header.getValue());
				}
				break;
			case DATE:
				if (setHeaders.contains(name)) {
					response.addDateHeader(name, (Long) header.getValue());
				} else {
					setHeaders.add(name);
					response.setDateHeader(name, (Long) header.getValue());
				}
				break;
			case INT:
				if (setHeaders.contains(name)) {
					response.addIntHeader(name, (Integer) header.getValue());
				} else {
					setHeaders.add(name);
					response.setIntHeader(name, (Integer) header.getValue());
				}
				break;
			default:
				throw new IllegalArgumentException("No mapping for Header: "
						+ header);
			}
		}
	}

	public void setCacheModelName(String cacheModelName) {
		this.cacheModelName = cacheModelName;
	}

	public void setCacheConfigFilePath(String cacheConfigFilePath) {
		this.cacheConfigFilePath = cacheConfigFilePath;
	}
}
