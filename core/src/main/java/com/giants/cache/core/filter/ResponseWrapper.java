/**
 * 
 */
package com.giants.cache.core.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.giants.cache.core.filter.Header.Type;

/**
 * @author vencent.lu
 *
 */
public class ResponseWrapper extends HttpServletResponseWrapper
		implements Serializable {

	private static final long serialVersionUID = 7243861926899979905L;
	
	private int statusCode = SC_OK;
	private final List<Cookie> cookies = new ArrayList<Cookie>();
	private final Map<String, List<Serializable>> headersMap = new TreeMap<String, List<Serializable>>(String.CASE_INSENSITIVE_ORDER);
	
	private ServletOutputStream stream;
    private PrintWriter writer;

	/**
	 * @param response response
	 * @param stream stream
	 */
	public ResponseWrapper(HttpServletResponse response, OutputStream stream) {
		super(response);
		this.stream = new FilterServletOutputStream(stream);
	}
	    
    /* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#getOutputStream()
	 */
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return this.stream;
	}
    
    /* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#getWriter()
	 */
	@Override
	public PrintWriter getWriter() throws IOException {
		 if (this.writer == null) {
	            this.writer = new PrintWriter(new OutputStreamWriter(this.stream, getCharacterEncoding()), true);
	        }
	        return this.writer;
	}
	
	 /**
     * Sets the status code for this response.
     */
    public void setStatus(final int code) {
        statusCode = code;
        super.setStatus(code);
    }
    
    /**
     * Send the error. If the response is not ok, most of the logic is bypassed and the error is sent raw
     * Also, the content is not cached.
     *
     * @param i      the status code
     * @param string the error message
     * @throws IOException IOException
     */
    public void sendError(int i, String string) throws IOException {
        statusCode = i;
        super.sendError(i, string);
    }

    /**
     * Send the error. If the response is not ok, most of the logic is bypassed and the error is sent raw
     * Also, the content is not cached.
     *
     * @param i the status code
     * @throws IOException IOException
     */
    public void sendError(int i) throws IOException {
        statusCode = i;
        super.sendError(i);
    }
    
    /**
     * Send the redirect. If the response is not ok, most of the logic is bypassed and the error is sent raw.
     * Also, the content is not cached.
     *
     * @param string the URL to redirect to
     * @throws IOException IOException
     */
    public void sendRedirect(String string) throws IOException {
        statusCode = HttpServletResponse.SC_MOVED_TEMPORARILY;
        super.sendRedirect(string);
    }
    
    /**
     * Sets the status code for this response.
     */
    public void setStatus(final int code, final String msg) {
        statusCode = code;
        super.setStatus(code);
    }

    /**
     * Returns the status code for this response.
     */
    public int getStatus() {
        return statusCode;
    }
    
    /**
     * Adds a cookie.
     */
    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
        super.addCookie(cookie);
    }

    /**
     * Gets all the cookies.
     * @return List cookies
     */
    public List<Cookie> getCookies() {
        return cookies;
    }
    
    /**
     * @see javax.servlet.http.HttpServletResponseWrapper#addHeader(java.lang.String, java.lang.String)
     */
    @Override
    public void addHeader(String name, String value) {
        List<Serializable> values = this.headersMap.get(name);
        if (values == null) {
            values = new LinkedList<Serializable>();
            this.headersMap.put(name, values);
        }
        values.add(value);
        
        super.addHeader(name, value);
    }
    
    /**
     * @see javax.servlet.http.HttpServletResponseWrapper#setHeader(java.lang.String, java.lang.String)
     */
    @Override
    public void setHeader(String name, String value) {
        final LinkedList<Serializable> values = new LinkedList<Serializable>();
        values.add(value);
        this.headersMap.put(name, values);
        
        super.setHeader(name, value);
    }
    
    /**
     * @see javax.servlet.http.HttpServletResponseWrapper#addDateHeader(java.lang.String, long)
     */
    @Override
    public void addDateHeader(String name, long date) {
        List<Serializable> values = this.headersMap.get(name);
        if (values == null) {
            values = new LinkedList<Serializable>();
            this.headersMap.put(name, values);
        }
        values.add(date);
        
        super.addDateHeader(name, date);
    }
    
    /**
     * @see javax.servlet.http.HttpServletResponseWrapper#setDateHeader(java.lang.String, long)
     */
    @Override
    public void setDateHeader(String name, long date) {
        final LinkedList<Serializable> values = new LinkedList<Serializable>();
        values.add(date);
        this.headersMap.put(name, values);
        
        super.setDateHeader(name, date);
    }
    
    /**
     * @see javax.servlet.http.HttpServletResponseWrapper#addIntHeader(java.lang.String, int)
     */
    @Override
    public void addIntHeader(String name, int value) {
        List<Serializable> values = this.headersMap.get(name);
        if (values == null) {
            values = new LinkedList<Serializable>();
            this.headersMap.put(name, values);
        }
        values.add(value);
        
        super.addIntHeader(name, value);
    }
    
    /**
     * @see javax.servlet.http.HttpServletResponseWrapper#setIntHeader(java.lang.String, int)
     */
    @Override
    public void setIntHeader(String name, int value) {
        final LinkedList<Serializable> values = new LinkedList<Serializable>();
        values.add(value);
        this.headersMap.put(name, values);
        
        super.setIntHeader(name, value);
    }
    
    /**
     * @return All of the headersMap set/added on the response
     */
    public List<Header<? extends Serializable>> getAllHeaders() {
        final List<Header<? extends Serializable>> headers = new LinkedList<Header<? extends Serializable>>();
        
        for (final Map.Entry<String, List<Serializable>> headerEntry : this.headersMap.entrySet()) {
            String name = headerEntry.getKey();
            for (final Serializable value : headerEntry.getValue()) {
                Type type = Header.Type.determineType(value.getClass());
                switch (type) {
                    case STRING:
                        headers.add(new Header<String>(name, (String)value));
                    break;
                    case DATE:
                        headers.add(new Header<Long>(name, (Long)value));
                    break;
                    case INT:
                        headers.add(new Header<Integer>(name, (Integer)value));
                    break;
                    default:
                        throw new IllegalArgumentException("No mapping for Header.Type: " + type);
                }
            }
        }
        
        return headers;
    }

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#flushBuffer()
	 */
	@Override
	public void flushBuffer() throws IOException {
		this.flush();
        super.flushBuffer();
	}

	/**
     * Flushes all the streams for this response.
     * @throws IOException IOException
     */
    public void flush() throws IOException {
        if (this.writer != null) {
            this.writer.flush();
        }
        this.stream.flush();
    }
    
    /**
     * Resets the response.
     */
    public void reset() {
        super.reset();
        cookies.clear();
        headersMap.clear();
        statusCode = SC_OK;
    }

}
