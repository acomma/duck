package me.acomma.duck.web.advice;

import me.acomma.duck.util.RestResult;
import me.acomma.duck.util.code.ErrorCode;
import me.acomma.duck.util.code.SystemErrorCode;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(3)
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    public RestResult<Void> handleException() {
        ErrorCode<Integer> errorCode = SystemErrorCode.SYSTEM_ERROR;
        return RestResult.<Void>builder().code(errorCode.code()).message(errorCode.message()).build();
    }
}
