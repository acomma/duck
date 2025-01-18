package me.acomma.duck.util.exception;

import lombok.Getter;
import me.acomma.duck.util.code.BusinessErrorCode;

import java.io.Serial;

/**
 * 业务异常。
 */
@Getter
public class BusinessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2356791864188814845L;

    private final BusinessErrorCode errorCode;
    private final Object[] arguments;

    public BusinessException(BusinessErrorCode errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
        this.arguments = new Object[0];
    }

    public BusinessException(BusinessErrorCode errorCode, Object... arguments) {
        super(errorCode.message());
        this.errorCode = errorCode;
        this.arguments = new Object[arguments.length];
        System.arraycopy(arguments, 0, this.arguments, 0, arguments.length);
    }

    public BusinessException(BusinessErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.arguments = new Object[0];
    }

    public BusinessException(BusinessErrorCode errorCode, String message, Object... arguments) {
        super(message);
        this.errorCode = errorCode;
        this.arguments = new Object[arguments.length];
        System.arraycopy(arguments, 0, this.arguments, 0, arguments.length);
    }

    public BusinessException(BusinessErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.arguments = new Object[0];
    }

    public BusinessException(BusinessErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.arguments = new Object[0];
    }
}
