package me.acomma.duck.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * REST 请求的响应结果，用来统一响应结果的格式。
 *
 * @param <T> 响应的数据的类型
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public class RestResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -7148370151036271835L;

    /**
     * 错误码。
     */
    private Integer code;

    /**
     * 错误消息。
     */
    private String message;

    /**
     * 响应的数据。
     */
    private T data;
}
