/**
 * 
 */
package com.giants.cache.core.session;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;


/**
 * @author vencent.lu
 *
 */
public class GiantsSession implements HttpSession, Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id = null;
	
	private volatile boolean isValid = false;
	
	private long creationTime = 0L;
	
	private int maxInactiveInterval = -1;
	
	private volatile long lastAccessedTime = creationTime;
	
	private transient ServletContext servletContext = null;
	
	private Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();
	
	private static final String EMPTY_ARRAY[] = new String[0];
	
	private transient volatile boolean expiring = false;
	
	private transient boolean isNew = false;
	
	private transient boolean isUpdate = false;
		
	public GiantsSession(String id, ServletContext servletContext) {
		super();
		this.id = id;
		this.servletContext = servletContext;
	}

	private boolean isValidInternal() {
        return this.isValid;
    }

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getCreationTime()
	 */
	@Override
	public long getCreationTime() {
		if (!isValidInternal())
            throw new IllegalStateException();//TODO
		return this.creationTime;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getId()
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getLastAccessedTime()
	 */
	@Override
	public long getLastAccessedTime() {
		if (!isValidInternal()) {
            throw new IllegalStateException();//TODO
        }
        return this.lastAccessedTime;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getServletContext()
	 */
	@Override
	public ServletContext getServletContext() {
		return this.servletContext;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
	 */
	@Override
	public void setMaxInactiveInterval(int interval) {
		this.isUpdate = true;
		this.maxInactiveInterval = interval;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
	 */
	@Override
	public int getMaxInactiveInterval() {
		return this.maxInactiveInterval;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getSessionContext()
	 */
	@Override
	@Deprecated
	public HttpSessionContext getSessionContext() {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
	 */
	@Override
	public Object getAttribute(String name) {
		if (!isValidInternal())
            throw new IllegalStateException();//TODO
        if (name == null) return null;
        return this.attributes.get(name);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
	 */
	@Override
	public Object getValue(String name) {
		return this.getAttribute(name);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getAttributeNames()
	 */
	@Override
	public Enumeration<String> getAttributeNames() {
		if (!isValidInternal())
            throw new IllegalStateException();//TODO
        Set<String> names = new HashSet<String>();
        names.addAll(attributes.keySet());
        return Collections.enumeration(names);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#getValueNames()
	 */
	@Override
	public String[] getValueNames() {
		if (!isValidInternal())
            throw new IllegalStateException();//TODO
        return attributes.keySet().toArray(EMPTY_ARRAY);
	}
		
	public void setAttribute(String name, Object value, boolean notify) {

        // Name cannot be null
        if (name == null)
            throw new IllegalArgumentException();//TODO

        // Null value is the same as removeAttribute()
        if (value == null) {
            removeAttribute(name);
            return;
        }

        // Validate our current state
        if (!this.isValid)
            throw new IllegalStateException();//TODO
        if (!(value instanceof Serializable))
            throw new IllegalArgumentException();//TODO
       
        this.attributes.put(name, value);
        this.isUpdate = true;
    }

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setAttribute(String name, Object value) {
		this.setAttribute(name, value, true);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
	 */
	@Override
    @Deprecated
	public void putValue(String name, Object value) {
		this.setAttribute(name, value);
	}
	
	public void removeAttribute(String name, boolean notify) {

        // Validate our current state
        if (!isValidInternal())
            throw new IllegalStateException();//TODO
        this.removeAttributeInternal(name, notify);

    }

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
	 */
	@Override
	public void removeAttribute(String name) {
		this.removeAttribute(name, true);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
	 */
	@Override
	public void removeValue(String name) {
		this.removeAttribute(name);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#invalidate()
	 */
	@Override
	public void invalidate() {
		if (!isValidInternal())
            throw new IllegalStateException();//TODO
        // Cause this session to expire
        this.expire();
	}

	public void setNew(boolean isNew) {
        this.isNew = isNew;
    }
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSession#isNew()
	 */
	@Override
	public boolean isNew() {
		if (!isValidInternal())
            throw new IllegalStateException();//TODO
        return this.isNew;
	}
		
	public boolean isUpdate() {
		if (!isValidInternal())
            throw new IllegalStateException();//TODO
		return this.isUpdate;
	}

	private void removeAttributeInternal(String name, boolean notify) {

        // Avoid NPE
        if (name == null) return;

        // Remove this attribute from our collection
        attributes.remove(name);
        this.isUpdate = true;
    }
	
	public void expire() {
		this.isValid = false;
        this.expiring = true;
    }
	
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public void setCreationTime(long time) {
		this.creationTime = time;
		this.lastAccessedTime = time;
	}

	public boolean isValid() {
		 if (!this.isValid) {
	            return false;
	        }

	        if (this.expiring) {
	            return true;
	        }

	        if (maxInactiveInterval > 0) {
	            long timeNow = System.currentTimeMillis();
	            int timeIdle = (int) ((timeNow - lastAccessedTime) / 1000L);	          
	            if (timeIdle >= maxInactiveInterval) {
	                expire();
	            }
	        }
	        return this.isValid;
	}
	
	public void endAccess() {
		this.isNew = false;
		this.isUpdate = false;
		this.lastAccessedTime = System.currentTimeMillis();
	}

	public boolean isExpiring() {
		return expiring;
	}

	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

}
