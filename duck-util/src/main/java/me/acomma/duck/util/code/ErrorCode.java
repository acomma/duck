package me.acomma.duck.util.code;

import me.acomma.duck.util.Enumerable;

/**
 * 错误码。
 *
 * @param <T> 错误码的类型
 */
public interface ErrorCode<T> extends Enumerable<T> {
    /**
     * @return 错误码
     */
    T code();

    /**
     * @return 错误消息
     */
    String message();
}
