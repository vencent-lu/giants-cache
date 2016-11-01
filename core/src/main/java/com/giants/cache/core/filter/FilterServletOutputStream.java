/**
 * 
 */
package com.giants.cache.core.filter;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

/**
 * @author vencent.lu
 *
 */
public class FilterServletOutputStream extends ServletOutputStream {
	
	 	private OutputStream stream;

	    /**
	     * Creates a FilterServletOutputStream.
	     */
	    public FilterServletOutputStream(final OutputStream stream) {
	        this.stream = stream;
	    }

	    /**
	     * Writes to the stream.
	     */
	    public void write(final int b) throws IOException {
	        stream.write(b);
	    }

	    /**
	     * Writes to the stream.
	     */
	    public void write(final byte[] b) throws IOException {
	        stream.write(b);
	    }

	    /**
	     * Writes to the stream.
	     */
	    public void write(final byte[] b, final int off, final int len) throws IOException {
	        stream.write(b, off, len);
	    }

}
