package me.acomma.duck.boot.jdbc;

/**
 * 数据源上下文, 持有当前线程的数据源类型, 内部使用 {@link ThreadLocal} 实现, 利用 {@code ThreadLocal} 提供的线程隔离能力实现数据隔离.
 */
public class DataSourceContext {
    // 线程本地变量, 保存当前线程的数据源类型.
    private static final ThreadLocal<DataSourceType> context = new ThreadLocal<>();

    /**
     * 设置当前线程的数据源类型.
     *
     * @param dataSourceType 数据源类型
     */
    public static void set(DataSourceType dataSourceType) {
        context.set(dataSourceType);
    }

    /**
     * 获取当前线程的数据源类型.
     *
     * @return 当前线程的数据源类型
     */
    public static DataSourceType get() {
        return context.get();
    }

    /**
     * 清除当前线程的数据源类型, 当程序运行结束需要调用该方法清理资源以防止内存泄漏.
     */
    public static void clear() {
        context.remove();
    }
}
