package me.acomma.duck.boot.web.advice;

import lombok.extern.slf4j.Slf4j;
import me.acomma.duck.util.RestResult;
import me.acomma.duck.util.code.ErrorCode;
import me.acomma.duck.util.code.SystemErrorCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public RestResult<Void> handleException(Exception exception) {
        log.error("发生未知异常", exception);
        ErrorCode<Integer> errorCode = SystemErrorCode.SYSTEM_ERROR;
        return RestResult.<Void>builder().code(errorCode.code()).message(errorCode.message()).build();
    }
}
