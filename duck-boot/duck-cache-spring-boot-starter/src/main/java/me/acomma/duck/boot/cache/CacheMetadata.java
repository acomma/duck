package me.acomma.duck.boot.cache;

import lombok.Getter;
import lombok.Setter;

/**
 * 缓存的元数据.
 */
@Getter
@Setter
public class CacheMetadata {
    /**
     * 缓存的名字.
     */
    private String cacheName;

    /**
     * 缓存的 key, 由 {@link org.springframework.cache.annotation.Cacheable#key()} 经过表达式计算后的结果.
     */
    private String key;

    /**
     * 缓存的生存时间.
     */
    private long ttl;

    public CacheMetadata(String cacheName, String key, long ttl) {
        this.cacheName = cacheName;
        this.key = key;
        this.ttl = ttl;
    }
}
