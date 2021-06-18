/**
 * 
 */
package com.giants.cache.core.aop;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;

import com.giants.cache.common.CacheConstants;
import com.giants.cache.config.elements.CacheElement;
import com.giants.cache.config.elements.ClearCache;
import com.giants.cache.config.elements.ClearElement;
import com.giants.cache.core.Element;
import com.giants.cache.core.GiantsCacheManager;
import com.giants.cache.core.MethodCacheKey;
import com.giants.cache.core.exception.UndefinedCacheModelException;
import com.giants.common.lang.reflect.ReflectUtils;

/**
 * @author vencent.lu
 *
 */
public class GiantsCacheAop {
	
	private String cacheModelName;
	private GiantsCacheManager giantsCacheManager;
	private String cacheConfigFilePath = CacheConstants.DEFAULT_CONFIG_FILE_PATH;
	private boolean supportMultipleInstance = false;
	
	
	public Object serviceMethodCache(ProceedingJoinPoint service) throws Throwable {
		if (StringUtils.isEmpty(this.cacheConfigFilePath)) {
			return service.proceed();
		}
		if (this.giantsCacheManager == null) {
			this.giantsCacheManager = GiantsCacheManager
					.getInstance(this.cacheConfigFilePath);
		}
		if (this.giantsCacheManager == null || this.giantsCacheManager.getCacheConfig() == null) {
			return service.proceed();
		}
		MethodCacheKey cacheKey = this.getMethodCacheKey(service);
		Object result = this.executeCacheAgent(cacheKey, service);
		this.executeClearCacheAgent(cacheKey);
		return result;
	}
	
	private Object executeCacheAgent(MethodCacheKey cacheKey,
			ProceedingJoinPoint service) throws Throwable {
		if (!cacheKey.isReturnValue()) {
			CacheElement cacheElement = this.giantsCacheManager.getCacheElementConf(
					this.cacheModelName, cacheKey.getClassName());
			if (cacheElement != null && (cacheElement.isCleanMethod(cacheKey
					.getSimpleMethodName())
					|| cacheElement.isCleanMethod(cacheKey
							.getSimpleMethod()))) {
				this.giantsCacheManager.removeCacheElement(
						this.cacheModelName, cacheKey.getClassName());
			}
			return service.proceed();
		}
		
		CacheElement cacheElement = this.giantsCacheManager.getCacheElementConf(
				this.cacheModelName, cacheKey.getKeyName());
		if (cacheElement == null) {
			cacheElement = this.giantsCacheManager.getCacheElementConf(
					this.cacheModelName, cacheKey.getMethodName());
			if (cacheElement != null) {
				cacheKey.setElementConfName(cacheKey.getMethodName());
			} else {
				cacheElement = this.giantsCacheManager.getCacheElementConf(
						this.cacheModelName, cacheKey.getClassName());
				if (cacheElement != null) {
					boolean isNuLL = false;
					if (cacheElement.isExclusionMethod(cacheKey
							.getSimpleMethodName())
							|| cacheElement.isExclusionMethod(cacheKey
									.getSimpleMethod())) {
						isNuLL = true;
					}
					if (cacheElement.isCleanMethod(cacheKey
							.getSimpleMethodName())
							|| cacheElement.isCleanMethod(cacheKey
									.getSimpleMethod())) {
						this.giantsCacheManager.removeCacheElement(
								this.cacheModelName, cacheKey.getClassName());
						isNuLL = true;
					}
					cacheKey.setElementConfName(cacheKey.getClassName());
					if (isNuLL) {
						cacheElement = null;
					}
				}
			}
		}
		if (cacheElement == null) {
			return service.proceed();
		}
		Element element = this.giantsCacheManager.getCacheElement(cacheKey);
		Object result;
		if (element == null) {
			result = service.proceed();
			this.giantsCacheManager.putCacheElement(new Element(this.cacheModelName,
					cacheKey, result, cacheElement.getTimeToLive()));
		} else if (!element.getTimeToLive().equals(cacheElement.getTimeToLive())) {
			result = service.proceed();
			element.update(result, cacheElement.getTimeToLive());
			this.giantsCacheManager.putCacheElement(element);
		} else if (element.isExpired()) {
			result = service.proceed();
			element.update(result);
			this.giantsCacheManager.putCacheElement(element);
		} else {
			result = element.getValue();
		}
		return result;
	}
	
	private void executeClearCacheAgent(MethodCacheKey cacheKey)
			throws UndefinedCacheModelException {
		ClearCache clearCache = this.giantsCacheManager.getClearCache(
				this.cacheModelName, cacheKey.getKeyName());
		if (clearCache == null) {
			clearCache = this.giantsCacheManager.getClearCache(this.cacheModelName,
					cacheKey.getMethodName());
		}
		if (clearCache == null) {
			clearCache = this.giantsCacheManager.getClearCache(this.cacheModelName,
					cacheKey.getClassName());
		}
		if (clearCache == null) {
			return;
		}
		for (ClearElement clearElement : clearCache.getClearElements()) {
			this.giantsCacheManager.removeCacheElement(this.cacheModelName,
					clearElement.getName());
		}
	}
	
	private MethodCacheKey getMethodCacheKey(ProceedingJoinPoint service)
			throws ClassNotFoundException {
		String[] methodSignatures = service.getSignature().toLongString().split(" ");
		String targetName = service.getSignature().getDeclaringTypeName();
		String methodName = service.getSignature().getName();
		Class<?> targetClass = service.getTarget().getClass();
		String[] parses = methodSignatures[methodSignatures.length-1].split("\\(");
		String returnValue = methodSignatures[methodSignatures.length-2];
		String argTypesStr = parses[1].replace(")", "");
		
		if (!targetClass.getName().equals(targetName)
				&& ReflectUtils.getInterface(targetClass, targetName) == null) {
			if (StringUtils.isNotEmpty(argTypesStr)) {
				String[] argTypeNames = argTypesStr.split("\\,");
				Class<?>[] parameterTypes = new Class<?>[argTypeNames.length];
				for (int i=0; i < argTypeNames.length; i++) {
					parameterTypes[i] = ReflectUtils.classForName(argTypeNames[i]);
				}
				Class<?> inter = ReflectUtils.findMethodInterface(service.getTarget()
						.getClass(), methodName, parameterTypes);
				if (inter != null) {
					targetName = inter.getName();
				}
			}
		}
		methodName = new StringBuffer(targetName).append('.')
				.append(methodName).append('(').append(argTypesStr).append(')')
				.toString();
		
		MethodCacheKey cacheKey = new MethodCacheKey(this.cacheModelName,
				methodName);
		if (returnValue.equals("void")) {
			cacheKey.setReturnValue(false);
			return cacheKey;
		}
		if (this.supportMultipleInstance
				&& service.getTarget() instanceof Serializable) {
			cacheKey.setTarget(service.getTarget());
		}
		Object[] arguments = service.getArgs();
		if (ArrayUtils.isNotEmpty(arguments)) {
			for (int i = 0; i < arguments.length; i++) {
				cacheKey.addArgument(arguments[i]);
			}
		}
		return cacheKey;
	}

	public void setCacheModelName(String cacheModelName) {
		this.cacheModelName = cacheModelName;
	}

	public void setCacheConfigFilePath(String cacheConfigFilePath) {
		this.cacheConfigFilePath = cacheConfigFilePath;
	}

	public void setSupportMultipleInstance(boolean supportMultipleInstance) {
		this.supportMultipleInstance = supportMultipleInstance;
	}

}
