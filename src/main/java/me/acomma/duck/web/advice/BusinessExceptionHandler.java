package me.acomma.duck.web.advice;

import me.acomma.duck.util.RestResult;
import me.acomma.duck.util.code.BusinessErrorCode;
import me.acomma.duck.util.exception.BusinessException;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(2)
public class BusinessExceptionHandler {
    @ExceptionHandler
    public RestResult<Void> handleBusinessException(BusinessException exception) {
        BusinessErrorCode businessErrorCode = exception.getBusinessErrorCode();
        return RestResult.<Void>builder().code(businessErrorCode.code()).message(businessErrorCode.message()).build();
    }
}
