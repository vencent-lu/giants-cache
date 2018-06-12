/**
 * 
 */
package com.giants.cache.redis;

import java.io.Serializable;

/**
 * @author vencent.lu
 *
 */
public class Lock implements Serializable {
    private static final long serialVersionUID = 2482992503584537196L;
    
    private Serializable key;
    private Long expireTime;
    
    public Lock(Serializable key, Long expireTime) {
        super();
        this.key = key;
        this.expireTime = expireTime;
    }
    
    public Serializable getKey() {
        return key;
    }
    public Long getExpireTime() {
        return expireTime;
    }

}
