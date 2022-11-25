package me.acomma.duck.util.advice;

import lombok.extern.slf4j.Slf4j;
import me.acomma.duck.util.RestResult;
import me.acomma.duck.util.code.BusinessErrorCode;
import me.acomma.duck.util.exception.BusinessException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 3)
@Slf4j
public class BusinessExceptionHandler {
    @ExceptionHandler
    public RestResult<Void> handleBusinessException(BusinessException exception) {
        log.error("发生业务异常", exception);
        BusinessErrorCode businessErrorCode = exception.getBusinessErrorCode();
        return RestResult.<Void>builder().code(businessErrorCode.code()).message(businessErrorCode.message()).build();
    }
}
