package me.acomma.duck.web.filter;

import org.slf4j.MDC;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * 链路跟踪 ID 过滤器。
 */
@Component
public class TraceIdFilter extends OncePerRequestFilter implements OrderedFilter {
    private static final String TRACE_ID = "traceId";

    @Override
    public int getOrder() {
        return REQUEST_WRAPPER_FILTER_MAX_ORDER;
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
