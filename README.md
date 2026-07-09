# giants-cache

[![Maven Central](https://img.shields.io/maven-central/v/com.github.vencent-lu/giants-cache.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:com.github.vencent-lu%20AND%20a:giants-cache)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![JDK](https://img.shields.io/badge/JDK-1.7%2B-red.svg)](https://www.oracle.com/java/technologies/downloads/)
[![Spring](https://img.shields.io/badge/Spring-4.2.x-6db33f.svg)](https://spring.io/)

JAVA 缓存中间层，通过 XML 配置动态为应用增加缓存能力，**无需修改业务代码、无 API 侵入**。

giants-cache 在方法层（基于 AOP）和 Servlet 层（基于 Filter）拦截调用，按配置将返回结果 / HTTP 响应写入缓存后端（EhCache / Redis / Memcached），并提供分布式 Session、分布式锁等能力。业务代码只需保持原样，缓存策略全部集中在一份 `giants-cache.xml` 中维护。

- 完整使用说明见 [docs/USER_GUIDE.md](docs/USER_GUIDE.md)。

## 特性

- **无侵入**：方法缓存通过 Spring AOP 织入，Servlet 缓存通过 Filter 拦截，业务代码零改动。
- **配置驱动**：所有缓存规则集中在 `giants-cache.xml`，支持按方法签名 / 方法名 / 类名匹配。
- **多后端可插拔**：EhCache（本地）、Redis（Jedis 或 Spring Data Redis）、Memcached，统一 `GiantsCache` 接口。
- **方法结果缓存**：自动缓存方法返回值，支持缓存清除方法（cleanMethod）与关联清除（clearCache）。
- **Servlet 响应缓存**：缓存完整 HTTP 响应（状态码、Header、Cookie、Body），支持按 URI 正则匹配、按查询参数 / Cookie 组合 Key、远程 Purge。
- **分布式 Session**：用缓存后端替换容器 `HttpSession`，实现多机 Session 共享。
- **分布式锁**：Redis 后端提供 `getLock` / `releaseLock`。

## 模块结构

| 模块 | artifactId | 说明 |
| --- | --- | --- |
| core | `giants-cache-core` | 核心接口、配置模型、AOP、Servlet/Session Filter，内置无缓存实现 |
| ehcache | `giants-cache-ehcache` | 基于 EhCache 2.x 的本地缓存实现 |
| redis | `giants-cache-redis` | 基于 Redis 的实现，支持 Jedis 与 Spring Data Redis 两种客户端 |
| memcached | `giants-cache-memcached` | 基于 Memcached 的实现，内置连接池管理 |

当前版本：`1.1.2`（`groupId`：`com.github.vencent-lu`）。

## 快速开始

### 1. 引入依赖

以 Redis 后端为例（Maven）：

```xml
<dependency>
    <groupId>com.github.vencent-lu</groupId>
    <artifactId>giants-cache-redis</artifactId>
    <version>1.1.2</version>
</dependency>
```

`giants-cache-redis` / `giants-cache-ehcache` / `giants-cache-memcached` 均已依赖 `giants-cache-core`，按后端选择其一即可。

### 2. 编写缓存配置 `giants-cache.xml`

放在 classpath 根目录（默认文件名 `giants-cache.xml`）：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<cacheConfig name="giants-cache">
    <methodCacheModel name="serviceCache" defaultTimeToLive="600">
        <!-- 缓存 UserService 全部方法，getUser 结果缓存 300 秒 -->
        <cacheElement name="com.example.service.UserService" timeToLive="300">
            <!-- 调用这些方法时清空本 cacheElement 缓存 -->
            <cleanMethod name="updateUser"/>
            <cleanMethod name="deleteUser"/>
            <!-- 不缓存该方法 -->
            <exclusionMethod name="sendVerifyCode"/>
        </cacheElement>
    </methodCacheModel>
</cacheConfig>
```

### 3. 配置 Spring（Redis + AOP）

```xml
<!-- Redis 客户端（Jedis） -->
<bean id="jedisPool" class="redis.clients.jedis.JedisPool">
    <constructor-arg><bean class="redis.clients.jedis.JedisPoolConfig"/></constructor-arg>
    <constructor-arg value="127.0.0.1"/>
    <constructor-arg value="6379" type="int"/>
</bean>
<bean id="redisClient" class="com.giants.cache.redis.JedisClientImpl">
    <property name="jedisPool" ref="jedisPool"/>
</bean>

<!-- 缓存后端实现 + 管理器 -->
<bean id="giantsCache" class="com.giants.cache.redis.impl.GiantsRedisImpl">
    <property name="redisClient" ref="redisClient"/>
</bean>
<bean id="giantsCacheManager" class="com.giants.cache.core.GiantsCacheManager">
    <constructor-arg ref="giantsCache"/>
</bean>

<!-- AOP 切面 -->
<bean id="giantsCacheAop" class="com.giants.cache.core.aop.GiantsCacheAop">
    <property name="cacheModelName" value="serviceCache"/>
    <property name="cacheConfigFilePath" value="giants-cache.xml"/>
</bean>
<aop:config>
    <aop:aspect ref="giantsCacheAop">
        <aop:pointcut id="servicePointcut"
            expression="execution(* com.example.service..*.*(..))"/>
        <aop:around method="serviceMethodCache" pointcut-ref="servicePointcut"/>
    </aop:aspect>
</aop:config>
```

完成后，`com.example.service` 包下的方法调用会自动走缓存，无需改动任何业务代码。

其余后端（EhCache / Memcached）、Servlet 响应缓存、分布式 Session、分布式锁的完整配置与说明，请参阅 **[docs/USER_GUIDE.md](docs/USER_GUIDE.md)**。

## 依赖环境

- JDK 1.7+
- Spring 4.2.x（AOP、tx）
- Servlet 3.0+（使用 Servlet / Session Filter 时）
- 后端组件：EhCache 2.6.x / Jedis 2.8.x / Spring Data Redis 2.1.x / Memcached

## License

Apache License 2.0
