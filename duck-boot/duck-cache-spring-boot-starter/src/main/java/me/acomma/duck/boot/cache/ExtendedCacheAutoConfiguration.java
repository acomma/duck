package me.acomma.duck.boot.cache;

import me.acomma.duck.boot.cache.interceptor.CacheExtensionInterceptor;
import me.acomma.duck.boot.cache.redis.ExtendedRedisCacheManager;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * 自定义的缓存自动配置.
 */
@Configuration
@ConditionalOnClass(RedisConnectionFactory.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnBean(RedisConnectionFactory.class)
@EnableConfigurationProperties(CacheProperties.class)
@EnableCaching
public class ExtendedCacheAutoConfiguration {
    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    public ExtendedRedisCacheManager cacheManager(CacheProperties cacheProperties, RedisConnectionFactory redisConnectionFactory, CacheMetadataRegistry cacheMetadataRegistry) {
        RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);

        RedisCacheConfiguration defaultCacheConfiguration = redisCacheConfiguration(cacheProperties);

        return new ExtendedRedisCacheManager(cacheWriter, defaultCacheConfiguration, cacheMetadataRegistry);
    }

    @Bean
    public CacheExtensionInterceptor cacheExtensionInterceptor(CacheMetadataRegistry cacheMetadataRegistry) {
        return new CacheExtensionInterceptor(cacheMetadataRegistry);
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnBean(CacheExtensionInterceptor.class)
    public AbstractPointcutAdvisor cacheExtensionPointcutAdvisor(CacheExtensionInterceptor cacheExtensionInterceptor) {
        AbstractPointcutAdvisor cacheableAdviser = new MethodAnnotationPointcutAdvisor(CacheExtension.class, cacheExtensionInterceptor);
        // 保证在 org.springframework.cache.interceptor.CacheInterceptor 之前执行
        cacheableAdviser.setOrder(Ordered.LOWEST_PRECEDENCE - 1);
        return cacheableAdviser;
    }

    @Bean
    public CacheMetadataRegistry cacheMetadataRegistry() {
        return new CacheMetadataRegistry();
    }

    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
        config = config.computePrefixWith(cacheName -> cacheName + ":");
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(Duration.ofMinutes(1));
        }

        return config;
    }
}
