/**
 * 
 */
package com.giants.cache.redis;

import java.io.Serializable;

/**
 * @author vencent.lu
 *
 */
public class Tuple {
	
	private final Serializable member;
	private final double score;
	
	public Tuple(Serializable member, double score) {
		super();
		this.member = member;
		this.score = score;
	}

	public Serializable getMember() {
		return member;
	}

	public double getScore() {
		return score;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((member == null) ? 0 : member.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tuple other = (Tuple) obj;
		if (member == null) {
			if (other.member != null)
				return false;
		} else if (!member.equals(other.member))
			return false;
		return true;
	}
	
	

}
