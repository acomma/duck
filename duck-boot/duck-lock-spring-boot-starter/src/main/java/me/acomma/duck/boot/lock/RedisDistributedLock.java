package me.acomma.duck.boot.lock;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁的 <a href="https://redis.io/">Redis</a> 实现.
 *
 * <p>Redis 无论哨兵模式还是集群模式都只能满足 CAP 理论的 AP 模型，因此 Redis 分布式锁无法总是满足可用性特征,
 * 但是可以使用 <a href="https://redis.io/topics/distlock">Redlock</a> 算法来优化实现, 这是一个取舍问题,
 * 当前实现未实现 Redlock 算法.
 *
 * <p>因为加锁使用的 {@code SET} 命令的 {@code NX} 和 {@code PX} 参数从 Redis 2.6.12 开始支持,
 * 解锁使用的脚本从 Redis 2.6.0 开始支持, 所以应使用最低版本为 2.6.12 的 Redis 服务器.
 *
 * <p>在使用的时候需要配置以 {@code spring.redis} 为前缀的属性.
 */
public class RedisDistributedLock implements DistributedLock {
    /**
     * 释放锁的脚本. 只有当锁定的健存在并且存储在该健中的值正是我期望的值时才删除该健, 这是为了防止释放由其他线程获得的锁.
     */
    private static final String UNLOCK_SCRIPT = """
            if redis.call('GET',KEYS[1]) == ARGV[1] then
              return redis.call('DEL',KEYS[1])
            else
              return 0
            end
            """;

    private final RedisTemplate<String, String> redisTemplate;

    private final String key;

    private final String value;

    private final long timeout;

    private final RedisDistributedLockMonitor monitor;

    /**
     * 创建一个新的 {@link RedisDistributedLock} 对象.
     *
     * @param redisTemplate 简化 Redis 数据访问代码的帮助类
     * @param key           要锁定的对象
     * @param timeout       超时时间, 单位: 毫秒
     */
    public RedisDistributedLock(RedisTemplate<String, String> redisTemplate, String key, long timeout) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.value = UUID.randomUUID().toString();
        this.timeout = timeout;
        this.monitor = new RedisDistributedLockMonitor(this.redisTemplate, this.key, this.value, this.timeout);
    }

    @Override
    public void lock() {
        while (true) {
            try {
                // 自旋
                while (!acquireLock()) {
                    TimeUnit.MILLISECONDS.sleep(100);
                }
                break;
            } catch (InterruptedException e) {
                // 此方法必须是不可中断的, 因此捕获并忽略中断, 并且只有在获得锁时才中断 while 循环.
            } catch (Exception e) {
                throw new DistributedLockException("获取锁 " + key + " 失败", e);
            }
        }
    }

    @Override
    public boolean tryLock() {
        return acquireLock();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        try {
            long now = System.currentTimeMillis();
            long expire = now + TimeUnit.MILLISECONDS.convert(time, unit);
            boolean acquired;
            while (!(acquired = acquireLock()) && System.currentTimeMillis() < expire) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            return acquired;
        } catch (Exception e) {
            throw new DistributedLockException("获取锁 " + key + " 失败", e);
        }
    }

    @Override
    public void unlock() {
        if (releaseLock()) {
            return;
        }
        throw new DistributedLockException("释放锁 " + key + " 失败");
    }

    /**
     * 使用命令 {@code SET <key> <value> NX PX <timeout>} 获取锁.
     *
     * <p>{@code value} 参数配合释放锁脚本保证了锁的对称性, {@code NX} 参数保证了锁的互斥性, {@code timeout} 参数保证了锁的安全性.
     *
     * @return 如果获取了锁, 则为 true, 否则为 false
     */
    private boolean acquireLock() {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.MILLISECONDS);
        boolean success = Objects.equals(Boolean.TRUE, result);
        if (success) {
            monitor.start();
        }
        return success;
    }

    /**
     * 使用脚本释放锁.
     *
     * @return 如果释放了锁, 则为 true, 否则为 false
     */
    private boolean releaseLock() {
        monitor.stop();
        Boolean result = redisTemplate.execute(RedisScript.of(UNLOCK_SCRIPT, Boolean.class), Collections.singletonList(key), value);
        return Objects.equals(Boolean.TRUE, result);
    }
}
