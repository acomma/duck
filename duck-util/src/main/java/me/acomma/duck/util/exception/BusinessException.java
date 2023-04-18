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

    private final BusinessErrorCode businessErrorCode;

    public BusinessException(BusinessErrorCode businessErrorCode) {
        super(businessErrorCode.message());
        this.businessErrorCode = businessErrorCode;
    }

    public BusinessException(BusinessErrorCode businessErrorCode, String message) {
        super(message);
        this.businessErrorCode = businessErrorCode;
    }

    public BusinessException(BusinessErrorCode businessErrorCode, String message, Throwable cause) {
        super(message, cause);
        this.businessErrorCode = businessErrorCode;
    }

    public BusinessException(BusinessErrorCode businessErrorCode, Throwable cause) {
        super(cause);
        this.businessErrorCode = businessErrorCode;
    }
}
