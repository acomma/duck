package me.acomma.duck.util.advice;

import me.acomma.duck.util.RestResult;
import me.acomma.duck.util.code.SystemErrorCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 4)
public class ValidationExceptionHandler {
    @ExceptionHandler
    public RestResult<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ObjectError error = exception.getAllErrors().get(0);
        String message = error.getDefaultMessage();
        return RestResult.<Void>builder().code(SystemErrorCode.INVALID_PARAMETER.value()).message(message).build();
    }

    @ExceptionHandler
    public RestResult<Void> handleConstraintViolationException(ConstraintViolationException exception) {
        ConstraintViolation<?> violation = exception.getConstraintViolations().iterator().next();
        String message = violation.getMessage();
        return RestResult.<Void>builder().code(SystemErrorCode.INVALID_PARAMETER.value()).message(message).build();
    }
}
