package me.acomma.duck.boot.cache.redis;

import me.acomma.duck.boot.cache.CacheMetadataRegistry;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

/**
 * 自定义的 Redis 缓存管理器.
 */
public class ExtendedRedisCacheManager extends RedisCacheManager {
    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultCacheConfig;
    private final CacheMetadataRegistry cacheMetadataRegistry;

    public ExtendedRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, CacheMetadataRegistry cacheMetadataRegistry) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
        this.cacheMetadataRegistry = cacheMetadataRegistry;
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        return new ExtendedRedisCache(name, cacheWriter, cacheConfig != null ? cacheConfig : defaultCacheConfig, cacheMetadataRegistry);
    }
}
