package me.acomma.duck.boot.web.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.acomma.duck.util.RestResult;
import me.acomma.duck.util.code.BusinessErrorCode;
import me.acomma.duck.util.exception.BusinessException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Locale;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 3)
@RequiredArgsConstructor
@Slf4j
public class BusinessExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler
    public RestResult<Void> handleBusinessException(BusinessException exception, HttpServletRequest request) {
        log.error("发生业务异常", exception);
        BusinessErrorCode errorCode = exception.getErrorCode();
        String message = getMessage(exception, request);
        return RestResult.<Void>builder().code(errorCode.code()).message(message).build();
    }

    private String getMessage(BusinessException exception, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        BusinessErrorCode errorCode = exception.getErrorCode();
        try {
            return messageSource.getMessage(exception.getMessage(), exception.getArguments(), exception.getMessage(), locale);
        } catch (NoSuchMessageException e) {
            return messageSource.getMessage(errorCode.code().toString(), exception.getArguments(), errorCode.message(), locale);
        }
    }
}
