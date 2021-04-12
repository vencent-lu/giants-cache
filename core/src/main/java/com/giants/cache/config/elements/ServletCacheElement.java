/**
 * 
 */
package com.giants.cache.config.elements;

import java.util.List;

import com.giants.common.regex.Pattern;
import com.giants.xmlmapping.annotation.XmlAttribute;
import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.annotation.XmlManyElement;

/**
 * @author vencent.lu
 *
 */
@XmlEntity
public class ServletCacheElement extends CacheElement {

	private static final long serialVersionUID = -3644313733390730398L;
	
	@XmlAttribute
	private String regex;
	
	private Pattern uriPattern;
	
	@XmlAttribute
	private boolean queryParam = true;
	
	@XmlAttribute
	private boolean session = false;
	
	@XmlManyElement
	private List<ExclusionQueryParam> exclusionQueryParams;
	
	@XmlManyElement
	private List<ExclusionSession> exclusionSessions;

	public ServletCacheElement() {
		super();
	}

	/**
	 * @param name name
	 * @param timeToLive timeToLive
	 */
	public ServletCacheElement(String name, Long timeToLive) {
		super(name, timeToLive);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isAllowAccordingParam(String paramName) {
		if (this.exclusionQueryParams == null) {
			return true;
		}
		for (ExclusionQueryParam exclusionQueryParam : this.exclusionQueryParams) {
			if (exclusionQueryParam.getName().equals(paramName)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isAllowAccordingSession(String sessionName) {
		if (this.exclusionSessions == null) {
			return true;
		}
		for (ExclusionSession exclusionSession : this.exclusionSessions) {
			if (exclusionSession.getName().equals(sessionName)) {
				return false;
			}
		}
		return true;
	}
	
	public Pattern getURIPattern() {
		return this.uriPattern;
	}

	/**
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * @param regex the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
		this.uriPattern = Pattern.compile(this.regex);
	}

	/**
	 * @return the session
	 */
	public boolean isSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(boolean session) {
		this.session = session;
	}

	/**
	 * @return the queryParam
	 */
	public boolean isQueryParam() {
		return queryParam;
	}

	/**
	 * @param queryParam the queryParam to set
	 */
	public void setQueryParam(boolean queryParam) {
		this.queryParam = queryParam;
	}

	/**
	 * @param exclusionQueryParams the exclusionQueryParams to set
	 */
	public void setExclusionQueryParams(
			List<ExclusionQueryParam> exclusionQueryParams) {
		this.exclusionQueryParams = exclusionQueryParams;
	}

	/**
	 * @param exclusionSessions the exclusionSessions to set
	 */
	public void setExclusionSessions(List<ExclusionSession> exclusionSessions) {
		this.exclusionSessions = exclusionSessions;
	}

}
