package me.acomma.duck.util.id;

import java.security.SecureRandom;

/**
 * ID 生成器。
 *
 * <p><b>警告</b>：当前的实现有缺陷，不能在生产环境使用。
 */
public final class ID {
    private static final IdWorker worker;

    /*
     * 这里模仿 MongoDB ObjectId 方式随机生成 datacenterId 和 workerId。
     */
    static {
        SecureRandom secureRandom = new SecureRandom();
        int datacenterId = secureRandom.nextInt(32);
        int workerId = secureRandom.nextInt(32);

        worker = new IdWorker(workerId, datacenterId);
    }

    private ID() {
    }

    /**
     * 生成下一个 ID。
     * @return 下一个 ID
     */
    public static long nextId() {
        return worker.nextId();
    }
}
