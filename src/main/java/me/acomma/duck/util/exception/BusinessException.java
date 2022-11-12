package me.acomma.duck.util.exception;

import lombok.Getter;
import me.acomma.duck.util.code.ErrorCode;

import java.io.Serial;

/**
 * 业务异常。
 */
@Getter
public class BusinessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2356791864188814845L;

    private final ErrorCode<Integer> errorCode;

    public BusinessException(ErrorCode<Integer> errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode<Integer> errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode<Integer> errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode<Integer> errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }
}
