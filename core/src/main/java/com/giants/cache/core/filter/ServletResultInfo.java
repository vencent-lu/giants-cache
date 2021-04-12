/**
 * 
 */
package com.giants.cache.core.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vencent.lu
 *
 */
public class ServletResultInfo implements Serializable {

	private static final long serialVersionUID = 8276797931541053017L;
	
	private final ArrayList<Header<? extends Serializable>> responseHeaders = new ArrayList<Header<? extends Serializable>>();
	private final ArrayList<SerializableCookie> serializableCookies = new ArrayList<SerializableCookie>();
	private String contentType;
    private byte[] responseBody;
    private int statusCode;

    /**
     *
     * @param statusCode statusCode
     * @param contentType contentType
     * @param cookies cookies
     * @param responseBody responseBody
     * @param headers headers
     */
	public ServletResultInfo(final int statusCode, final String contentType,
			final List<Cookie> cookies,
			final byte[] responseBody,
			final List<Header<? extends Serializable>> headers) {
		this.init(statusCode, contentType, headers, cookies, responseBody);
	}
	
	/**
     * This really should be in the non-deprecated constructor but exists to allow for the deprecated constructor to work
     * correctly without duplicating code.
     */
    private void init(final int statusCode, final String contentType, 
            final List<Header<? extends Serializable>> headers, final List<Cookie> cookies,
            final byte[] body) {
        if (headers != null) {
            this.responseHeaders.addAll(headers);
        }
        this.contentType = contentType;
        this.statusCode = statusCode;
        this.responseBody = body;
        this.extractCookies(cookies);

    }

    private void extractCookies(List<Cookie> cookies) {
        if (cookies != null) {
            for (Iterator<Cookie> iterator = cookies.iterator(); iterator.hasNext();) {
                final Cookie cookie = iterator.next();
                serializableCookies.add(new SerializableCookie(cookie));
            }
        }
    }

	/**
	 * @return the contentType
	 */
	public final String getContentType() {
		return contentType;
	}
    	
	/**
     * @return All of the headers set on the page
     */
    public List<Header<? extends Serializable>> getHeaders() {
        return this.responseHeaders;
    }
    
    /**
     * @return the cookies of the response.
     */
    public List<SerializableCookie> getSerializableCookies() {
        return this.serializableCookies;
    }
    
    /**
     * @return the status code of the response.
     */
    public int getStatusCode() {
        return this.statusCode;
    }

	/**
	 * @return the responseBody
	 */
	public final byte[] getResponseBody() {
		return responseBody;
	}
    
	/**
     * Returns true if the response is Ok.
     *
     * @return true if the response code is 200.
     */
    public boolean isOk() {
        return (statusCode == HttpServletResponse.SC_OK);
    }
}
