package me.acomma.duck.boot.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存元数据的注册中心.
 */
public class CacheMetadataRegistry {
    private final ConcurrentMap<String, CacheMetadata> container = new ConcurrentHashMap<>(16);

    public void register(CacheMetadata cacheMetadata) {
        String key = String.join(":", cacheMetadata.getCacheName(), cacheMetadata.getKey());
        container.putIfAbsent(key, cacheMetadata);
    }

    public CacheMetadata get(String cacheName, String key) {
        String join = String.join(":", cacheName, key);
        return container.get(join);
    }
}
