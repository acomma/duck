package me.acomma.duck.web.advice;

import lombok.extern.slf4j.Slf4j;
import me.acomma.duck.util.RestResult;
import me.acomma.duck.util.code.ErrorCode;
import me.acomma.duck.util.code.SystemErrorCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 2)
@Slf4j
public class DatabaseExceptionHandler {
    @ExceptionHandler
    public RestResult<Void> handleSQLException(SQLException exception) {
        log.error("访问数据库异常", exception);
        ErrorCode<Integer> errorCode = SystemErrorCode.ACCESS_DATABASE_FAILED;
        return RestResult.<Void>builder().code(errorCode.code()).message(errorCode.message()).build();
    }
}
