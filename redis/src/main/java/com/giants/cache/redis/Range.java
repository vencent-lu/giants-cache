/**
 * 
 */
package com.giants.cache.redis;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.giants.common.Assert;

/**
 * 范围查询 范围对象
 * @author vencent.lu
 *
 */
public class Range {
	
	public static final Charset CHARSET = Charset.forName("UTF-8");
	
	public static final byte[] POSITIVE_INFINITY_BYTES = "+inf".getBytes(CHARSET);
	public static final byte[] NEGATIVE_INFINITY_BYTES = "-inf".getBytes(CHARSET);
	
	private Boundary min;
	private Boundary max;
	
	public static Range range() {
		return new Range();
	}
	
	public static Range unbounded() {
		Range range = new Range();
		range.min = Boundary.infinite();
		range.max = Boundary.infinite();
		return range;
	}
	
	/**
	 * Greater Than Equals
	 * 
	 * @param min
	 * @return
	 */
	public Range gte(Object min) {
		this.min = new Boundary(min, true);
		return this;
	}

	/**
	 * Greater Than
	 * 
	 * @param min
	 * @return
	 */
	public Range gt(Object min) {
		this.min = new Boundary(min, false);
		return this;
	}

	/**
	 * Less Then Equals
	 * 
	 * @param max
	 * @return
	 */
	public Range lte(Object max) {
		Assert.notNull(max, "Max already set for range.");
		this.max = new Boundary(max, true);
		return this;
	}

	/**
	 * Less Than
	 * 
	 * @param max
	 * @return
	 */
	public Range lt(Object max) {
		Assert.notNull(max, "Max already set for range.");
		this.max = new Boundary(max, false);
		return this;
	}
		
	public Boundary getMin() {
		return this.min;
	}

	public Boundary getMax() {
		return this.max;
	}
	
	private byte[] boundaryToBytes(Boundary boundary, byte[] defaultValue){
		if (boundary == null || boundary.getValue() == null) {
			return defaultValue;
		}
		byte[] prefix = boundary.isIncluding() ? new byte[] {} : "(".getBytes(CHARSET);
		byte[] value = null;
		if (boundary.getValue() instanceof byte[]) {
			value = (byte[]) boundary.getValue();
		} else if (boundary.getValue() instanceof Double) {
			value = String.valueOf(boundary.getValue()).getBytes(CHARSET);
		} else if (boundary.getValue() instanceof Long) {
			value = String.valueOf(boundary.getValue()).getBytes();
		} else if (boundary.getValue() instanceof Integer) {
			value = String.valueOf(boundary.getValue()).getBytes();
		} else if (boundary.getValue() instanceof String) {
			value = String.valueOf(boundary.getValue()).getBytes(CHARSET);
		} else {
			throw new IllegalArgumentException(String.format("Cannot convert %s to binary format", boundary.getValue()));
		}

		ByteBuffer buffer = ByteBuffer.allocate(prefix.length + value.length);
		buffer.put(prefix);
		buffer.put(value);
		return buffer.array();
	}
	
	public byte[] minToBytes() {
		return this.boundaryToBytes(this.min, NEGATIVE_INFINITY_BYTES);
	}
	
	public byte[] maxToBytes() {
		return this.boundaryToBytes(this.max, POSITIVE_INFINITY_BYTES);
	}

	public static class Boundary {	

		private Object value;
		private boolean including;

		public static Boundary infinite() {
			return new Boundary(null, true);
		}

		private Boundary(Object value, boolean including) {
			this.value = value;
			this.including = including;
		}

		public Object getValue() {
			return value;
		}

		public boolean isIncluding() {
			return including;
		}
		
	}

}
