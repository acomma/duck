package me.acomma.duck.boot.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁.
 *
 * <p>在<a href="https://segmentfault.com/a/1190000021603215">基于 etcd 实现分布式锁</a>这篇文章中作者总结了分布式锁应具备的特点：
 * <ul>
 *     <li>互斥性: 在任意时刻, 只有一个客户端（进程）能持有锁;</li>
 *     <li>安全性: 避免死锁情况, 当一个客户端在持有锁期间内, 由于意外崩溃而导致锁未能主动解锁, 其持有的锁也能够被正确释放, 并保证后续其它客户端也能加锁;</li>
 *     <li>可用性: 分布式锁需要有一定的高可用能力, 当提供锁的服务节点故障（宕机）时不影响服务运行, 避免单点风险, 如 Redis 的集群模式、哨兵模式, ETCD/zookeeper 的集群选主能力等保证 HA, 保证自身持有的数据与故障节点一致;</li>
 *     <li>对称性: 对同一个锁, 加锁和解锁必须是同一个进程, 即不能把其他进程持有的锁给释放了, 这又称为锁的可重入性.</li>
 * </ul>
 *
 * <p>分布式锁应该满足 CAP 理论中的 CP 模型.
 */
public interface DistributedLock {
    /**
     * 获取锁.
     *
     * <p>如果锁不可用, 则当前线程将被禁用以进行线程调度, 并且处于休眠状态, 直到获得锁为止.
     */
    void lock();

    /**
     * 仅在调用时锁是空闲的时才获取锁.
     *
     * @return 如果获取了锁, 则为 true, 否则为 false
     */
    boolean tryLock();

    /**
     * 如果锁在给定的时间内空闲, 则获取锁.
     *
     * @param time 等待锁的最长时间
     * @param unit 时间参数的时间单位
     * @return 如果获取了锁, 则为 true; 如果在获取锁之前经过了等待时间, 则为 false
     */
    boolean tryLock(long time, TimeUnit unit);

    /**
     * 释放锁.
     *
     * <p>锁只能被它的持有者释放.
     */
    void unlock();
}
