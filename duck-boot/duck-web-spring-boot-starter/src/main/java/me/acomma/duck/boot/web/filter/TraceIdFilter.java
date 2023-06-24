package me.acomma.duck.boot.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * 链路跟踪 ID 过滤器。优先从请求头中获取名称为 {@code Trace-Id} 的链路跟踪 ID，如果没有获取到就生成一个新的。
 */
@Component
public class TraceIdFilter extends OncePerRequestFilter implements OrderedFilter {
    private static final String TRACE_ID = "traceId";

    @Override
    public int getOrder() {
        // 保证链路跟踪ID过滤器在 Spring Security 过滤器之前执行，
        // 这个值是能设置的最大值，如果 -100 则无法保证在 Spring Security 过滤器之前执行
        return REQUEST_WRAPPER_FILTER_MAX_ORDER - 101;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String traceId = request.getHeader("Trace-Id");
            MDC.put(TRACE_ID, Objects.requireNonNullElseGet(traceId, this::generateTraceId));

            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID);
        }
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
