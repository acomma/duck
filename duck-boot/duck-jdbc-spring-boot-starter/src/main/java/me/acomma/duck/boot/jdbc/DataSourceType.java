package me.acomma.duck.boot.jdbc;

/**
 * 数据源类型，用来标识当前的数据源的类型, 适用于数据库进行了主从分离的系统.
 */
public enum DataSourceType {
    /**
     * 主库类型.
     */
    PRIMARY,

    /**
     * 从库类型.
     */
    REPLICA
}
