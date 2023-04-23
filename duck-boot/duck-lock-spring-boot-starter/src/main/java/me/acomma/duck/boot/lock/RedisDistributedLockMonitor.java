package me.acomma.duck.boot.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * {@link RedisDistributedLock} 的监视器.
 *
 * <p>监视器每隔三分之一过期时间就重置一次过期时间, 从而达到续期的目的.
 */
public class RedisDistributedLockMonitor {
    private static final Logger log = LoggerFactory.getLogger(RedisDistributedLockMonitor.class);

    /**
     * 续期脚本. 只有当锁定的健存在并且存储在该健中的值正是我们期望的值时才重新设置过期时间, 这是为了防止设置由其他线程获得的锁的过期时间.
     */
    private static final String RENEW_SCRIPT = """
            if redis.call('GET',KEYS[1]) == ARGV[1] then
              return redis.call('PEXPIRE',KEYS[1],ARGV[2])
            else
              return 0
            end
            """;

    private final RedisTemplate<String, String> redisTemplate;

    private final String key;

    private final String value;

    private final long timeout;

    private final RedisScript<Boolean> renewScript;

    private final ScheduledExecutorService scheduler;

    public RedisDistributedLockMonitor(RedisTemplate<String, String> redisTemplate, String key, String value, long timeout) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.value = value;
        this.timeout = timeout;
        this.renewScript = RedisScript.of(RENEW_SCRIPT, Boolean.class);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * 启动分布式锁的监视器.
     */
    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            Boolean result = redisTemplate.execute(renewScript, Collections.singletonList(key), value, String.valueOf(timeout));
            boolean success = Objects.equals(Boolean.TRUE, result);
            if (success) {
                if (log.isDebugEnabled()) {
                    log.debug("给锁 {} 续期成功", key);
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("给锁 {} 续期失败, 停止监视器", key);
                }
                stop();
            }
        }, 0, timeout / 3, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止分布式锁的监视器.
     */
    public void stop() {
        scheduler.shutdown();
    }
}
