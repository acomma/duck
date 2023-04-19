package me.acomma.duck.boot.lock;

import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;

/**
 * 创建分布式锁的工厂.
 */
public class DistributedLockFactory {
    private final RedisTemplate<String, String> redisTemplate;

    private static DistributedLockFactory INSTANCE;

    public DistributedLockFactory(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        DistributedLockFactory.INSTANCE = this;
    }

    /**
     * 创建一个分布式锁.
     *
     * @param key     要锁定的对象
     * @param timeout 锁的超时时间, 单位毫秒
     * @return 分布式锁实例
     */
    public static DistributedLock create(String key, long timeout) {
        return new RedisDistributedLock(INSTANCE.redisTemplate, key, timeout);
    }
}
