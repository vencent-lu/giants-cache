package com.giants.cache.core.session;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import com.giants.cache.core.exception.UndefinedCacheModelException;

public interface GiantsSessionCache extends Serializable {
	
	void putSession(HttpSession session) throws UndefinedCacheModelException;
	HttpSession getSession(String sessionId) throws UndefinedCacheModelException;
	void removeSession(HttpSession session) throws UndefinedCacheModelException;
	void expireSession(HttpSession session) throws UndefinedCacheModelException;

}
