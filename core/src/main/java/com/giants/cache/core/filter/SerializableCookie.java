/**
 * 
 */
package com.giants.cache.core.filter;

import java.io.Serializable;

import javax.servlet.http.Cookie;

/**
 * A serializable cookie which wraps Cookie.
 * This gets around a NotSerializable error with Cookie when non memory stores are used.
 *
 * @author vencent.lu
 */
public class SerializableCookie implements Serializable {

	private static final long serialVersionUID = -5345368259503864463L;
	
	private String name;
    private String value;
    private String comment;
    private String domain;
    private int maxAge;
    private String path;
    private boolean secure;
    private int version;

    /** Creates a cookie. */
    public SerializableCookie(final Cookie cookie) {
        name = cookie.getName();
        value = cookie.getValue();
        comment = cookie.getComment();
        domain = cookie.getDomain();
        maxAge = cookie.getMaxAge();
        path = cookie.getPath();
        secure = cookie.getSecure();
        version = cookie.getVersion();
    }

    /** Builds a Cookie object from this object. */
    public Cookie toCookie() {
        final Cookie cookie = new Cookie(name, value);
        cookie.setComment(comment);
        //Otherwise null pointer exception
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setSecure(secure);
        cookie.setVersion(version);
        return cookie;
    }

}
