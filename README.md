# giants-cache

[![Maven Central](https://img.shields.io/maven-central/v/com.github.vencent-lu/giants-cache.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:com.github.vencent-lu%20AND%20a:giants-cache)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![JDK](https://img.shields.io/badge/JDK-1.7%2B-red.svg)](https://www.oracle.com/java/technologies/downloads/)
[![Spring](https://img.shields.io/badge/Spring-4.2.x-6db33f.svg)](https://spring.io/)

JAVA 缓存中间层，通过配置动态为应用增加缓存能力，**无需修改业务代码、无 API 侵入**。

giants-cache 在方法层（基于 AOP）和 Servlet 层（基于 Filter）拦截调用，按配置将返回结果 / HTTP 响应写入缓存后端（EhCache / Redis / Memcached），并提供分布式 Session、分布式锁等能力。业务代码保持原样，缓存策略通过 **XML 或 Properties/YAML** 集中维护，支持配合 **Nacos Config 实现动态刷新**。

**1.2.0 版本新特性**：
- 🔧 引入 `CacheConfigBuilder` 抽象，支持多种配置加载方式（XML / Properties / YAML）
- 🔥 **Properties/YAML 配置方式支持 `@RefreshScope`**，配合 Nacos 等配置中心可**热更新缓存规则，无需重启应用**
- 🚀 提供 Spring Boot 自动配置支持，简化接入流程
- 📦 新增 `giants-cache-config-xml` 与 `giants-cache-config-properties` 独立配置模块

- 完整使用说明见 [docs/USER_GUIDE.md](docs/USER_GUIDE.md)。

## 特性

- **无侵入**：方法缓存通过 Spring AOP 织入，Servlet 缓存通过 Filter 拦截，业务代码零改动。
- **配置驱动**：缓存规则集中管理，支持 XML 与 Properties/YAML 两种配置方式，按方法签名 / 方法名 / 类名匹配。
- **动态刷新**：使用 Properties 配置方式时支持 `@RefreshScope`，配合 Nacos Config 可实现缓存规则热更新，无需重启应用。
- **多后端可插拔**：EhCache（本地）、Redis（Jedis 或 Spring Data Redis）、Memcached，统一 `GiantsCache` 接口。
- **方法结果缓存**：自动缓存方法返回值，支持缓存清除方法（cleanMethod）与关联清除（clearCache）。
- **Servlet 响应缓存**：缓存完整 HTTP 响应（状态码、Header、Cookie、Body），支持按 URI 正则匹配、按查询参数 / Cookie 组合 Key、远程 Purge。
- **分布式 Session**：用缓存后端替换容器 `HttpSession`，实现多机 Session 共享。
- **分布式锁**：Redis 后端提供 `getLock` / `releaseLock`。
- **Spring Boot 集成**：提供自动配置支持，简化接入流程。

## 模块结构

| 模块 | artifactId | 说明 |
| --- | --- | --- |
| core | `giants-cache-core` | 核心接口、配置模型、AOP、Servlet/Session Filter，内置无缓存实现 |
| config-xml | `giants-cache-config-xml` | XML 配置加载器（基于 `giants-xmlmapping`），提供 Spring Boot 自动配置支持 |
| config-properties | `giants-cache-config-properties` | Properties/YAML 配置加载器，支持 `@RefreshScope` 动态刷新（可配合 Nacos） |
| ehcache | `giants-cache-ehcache` | 基于 EhCache 2.x 的本地缓存实现 |
| redis | `giants-cache-redis` | 基于 Redis 的实现，支持 Jedis 与 Spring Data Redis 两种客户端 |
| memcached | `giants-cache-memcached` | 基于 Memcached 的实现，内置连接池管理 |

当前版本：`1.2.0`（`groupId`：`com.github.vencent-lu`）。

## 快速开始

### 1. 引入依赖

以 Redis 后端 + XML 配置为例（Maven）：

```xml
<!-- 后端实现 -->
<dependency>
    <groupId>com.github.vencent-lu</groupId>
    <artifactId>giants-cache-redis</artifactId>
    <version>1.2.0</version>
</dependency>

<!-- 配置模块（XML 方式） -->
<dependency>
    <groupId>com.github.vencent-lu</groupId>
    <artifactId>giants-cache-config-xml</artifactId>
    <version>1.2.0</version>
</dependency>
```

> 若使用 Properties/YAML 配置方式（支持动态刷新），引入 `giants-cache-config-properties` 替代 `giants-cache-config-xml`。

### 2. 编写缓存配置

**方式一：XML 配置**（传统方式）

放在 classpath 根目录，文件名 `giants-cache.xml`：

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

**方式二：Properties/YAML 配置**（支持动态刷新）

在 `application.yml` 中定义：

```yaml
giants:
  cache:
    config-map:
      default:
        name: giants-cache
        method-cache-models:
          - name: serviceCache
            default-time-to-live: 600
            cache-elements:
              - name: com.example.service.UserService
                time-to-live: 300
                clean-methods:
                  - name: updateUser
                  - name: deleteUser
                exclusion-methods:
                  - name: sendVerifyCode
```

> 配合 Nacos Config，修改配置后无需重启即可生效。详见 [docs/USER_GUIDE.md](docs/USER_GUIDE.md) 第 3.2 节。

### 3. 配置 Spring（Redis + AOP）

**方式一：传统 Spring XML 配置**

```xml
<!-- 配置加载器（XML 方式） -->
<bean id="cacheConfigBuilder" class="com.giants.cache.config.xml.CacheConfigXmlBuilder">
    <constructor-arg value="default"/>                      <!-- cacheConfigKey -->
    <constructor-arg value="giants-cache.xml"/>             <!-- XML 文件路径 -->
</bean>

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
    <constructor-arg ref="cacheConfigBuilder"/>
    <property name="redisClient" ref="redisClient"/>
</bean>
<bean id="giantsCacheManager" class="com.giants.cache.core.GiantsCacheManager">
    <constructor-arg ref="giantsCache"/>
</bean>

<!-- AOP 切面 -->
<bean id="giantsCacheAop" class="com.giants.cache.core.aop.GiantsCacheAop">
    <property name="cacheModelName" value="serviceCache"/>
    <property name="cacheConfigKey" value="default"/>       <!-- 对应 cacheConfigBuilder 的 key -->
</bean>
<aop:config>
    <aop:aspect ref="giantsCacheAop">
        <aop:pointcut id="servicePointcut"
            expression="execution(* com.example.service..*.*(..))"/>
        <aop:around method="serviceMethodCache" pointcut-ref="servicePointcut"/>
    </aop:aspect>
</aop:config>
```

**方式二：Spring Boot + XML 配置（自动配置）**

引入 `giants-cache-config-xml` 后，在 `application.yml` 配置：

```yaml
giants:
  cache:
    cache-config-key: default
    cache-config-xml-file-path: giants-cache.xml
```

仅需手动装配后端和 AOP：

```java
@Configuration
public class CacheConfiguration {
    
    @Bean
    public GiantsCache giantsCache(CacheConfigBuilder cacheConfigBuilder, RedisClient redisClient) {
        GiantsRedisImpl cache = new GiantsRedisImpl(cacheConfigBuilder);
        cache.setRedisClient(redisClient);
        return cache;
    }
    
    @Bean
    public GiantsCacheManager giantsCacheManager(GiantsCache giantsCache) {
        return new GiantsCacheManager(giantsCache);
    }
    
    @Bean
    public GiantsCacheAop giantsCacheAop() {
        GiantsCacheAop aop = new GiantsCacheAop();
        aop.setCacheModelName("serviceCache");
        aop.setCacheConfigKey("default");
        return aop;
    }
}
```

**方式三：Spring Boot + Properties/YAML 配置（支持动态刷新）**

引入 `giants-cache-config-properties`，在 `application.yml` 直接定义缓存规则：

```yaml
giants:
  cache:
    name: giants-cache
    config-map:
      default:                                  # cacheConfigKey
        name: giants-cache
        method-cache-models:
          - name: serviceCache
            default-cache: false
            default-time-to-live: 600
            cache-elements:
              - name: com.example.service.UserService
                time-to-live: 300
                clean-methods:
                  - name: updateUser
                  - name: deleteUser
                exclusion-methods:
                  - name: sendVerifyCode
```

配合 Nacos Config + `@RefreshScope`，修改配置后无需重启即可生效。

完成后，`com.example.service` 包下的方法调用会自动走缓存，无需改动任何业务代码。

---

## 配置方式选择

| 特性 | XML 配置 | Properties/YAML 配置 |
| --- | --- | --- |
| 配置位置 | `giants-cache.xml` | `application.yml` / Nacos |
| 结构清晰度 | ★★★★★ | ★★★☆☆ |
| **动态刷新** | ✗ 需重启 | ✓ **支持热更新** |
| 配置中心集成 | ✗ | ✓ 原生支持 Nacos / Apollo |
| 推荐场景 | 传统 Spring / 配置稳定 | Spring Boot / 需要热更新 |

详细配置说明、多种后端（EhCache / Memcached）、Servlet 响应缓存、分布式 Session、分布式锁等，请参阅 **[docs/USER_GUIDE.md](docs/USER_GUIDE.md)**。

---

## 依赖环境

- JDK 1.7+
- Spring 4.2.x（AOP、tx）
- Servlet 3.0+（使用 Servlet / Session Filter 时）
- 后端组件：EhCache 2.6.x / Jedis 2.8.x / Spring Data Redis 2.1.x / Memcached

## License

Apache License 2.0
