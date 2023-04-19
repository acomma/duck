package me.acomma.duck.boot.cache.redis;

import me.acomma.duck.boot.cache.CacheMetadata;
import me.acomma.duck.boot.cache.CacheMetadataRegistry;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;

/**
 * 自定义的 Redis 缓存.
 */
public class ExtendedRedisCache extends RedisCache {
    private final String name;
    private final RedisCacheWriter cacheWriter;
    private final CacheMetadataRegistry cacheMetadataRegistry;

    protected ExtendedRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig, CacheMetadataRegistry cacheMetadataRegistry) {
        super(name, cacheWriter, cacheConfig);
        this.name = name;
        this.cacheWriter = cacheWriter;
        this.cacheMetadataRegistry = cacheMetadataRegistry;
    }

    @Override
    public void put(Object key, Object value) {
        Object cacheValue = preProcessCacheValue(value);
        if (!isAllowNullValues() && cacheValue == null) {
            throw new IllegalArgumentException(String.format(
                    "Cache '%s' does not allow 'null' values. Avoid storing null via '@Cacheable(unless=\"#result == null\")' or configure RedisCache to allow 'null' via RedisCacheConfiguration.",
                    name));
        }

        CacheMetadata cacheMetadata = cacheMetadataRegistry.get(name, key.toString());

        cacheWriter.put(name, createAndConvertCacheKey(key), serializeCacheValue(cacheValue), Duration.ofSeconds(cacheMetadata.getTtl()));
    }

    private byte[] createAndConvertCacheKey(Object key) {
        return serializeCacheKey(createCacheKey(key));
    }
}
