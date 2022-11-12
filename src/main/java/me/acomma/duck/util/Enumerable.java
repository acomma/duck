package me.acomma.duck.util;

/**
 * 可枚举的。
 *
 * @param <T> 枚举值的值的类型
 */
public interface Enumerable<T> {
    /**
     * 返回枚举值的值。
     */
    T value();
}
