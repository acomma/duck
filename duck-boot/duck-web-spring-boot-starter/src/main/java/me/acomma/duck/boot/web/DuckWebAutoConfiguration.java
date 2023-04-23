package me.acomma.duck.boot.web;

import me.acomma.duck.boot.web.advice.BusinessExceptionHandler;
import me.acomma.duck.boot.web.advice.DatabaseExceptionHandler;
import me.acomma.duck.boot.web.advice.GlobalExceptionHandler;
import me.acomma.duck.boot.web.advice.RestResultResponseBodyAdvice;
import me.acomma.duck.boot.web.advice.ValidationExceptionHandler;
import me.acomma.duck.boot.web.filter.TraceIdFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration(after = {WebMvcAutoConfiguration.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import({
        GlobalExceptionHandler.class,
        DatabaseExceptionHandler.class,
        BusinessExceptionHandler.class,
        ValidationExceptionHandler.class,
        RestResultResponseBodyAdvice.class,
        TraceIdFilter.class,
})
public class DuckWebAutoConfiguration {
}
