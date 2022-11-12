package me.acomma.duck.web.advice;

import me.acomma.duck.util.RestResult;
import me.acomma.duck.util.code.ErrorCode;
import me.acomma.duck.util.exception.BusinessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BusinessExceptionHandler {
    @ExceptionHandler
    public RestResult<Void> handleBusinessException(BusinessException exception) {
        ErrorCode<Integer> errorCode = exception.getErrorCode();
        return RestResult.<Void>builder().code(errorCode.code()).message(errorCode.message()).build();
    }
}
