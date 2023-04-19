package me.acomma.duck.boot.jdbc;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

/**
 * 数据源切面.
 *
 * <p>数据源切面应该在事务切面 {@link org.springframework.transaction.interceptor.TransactionInterceptor} 之前执行,
 * 而事务切面默认的 {@code order} 是 {@link Ordered#LOWEST_PRECEDENCE}, 因此数据源切面需要设置一个比它小的数字才行.
 */
@Aspect
public class DataSourceAspect implements Ordered {
    /**
     * 环绕通知, 当方法上有 {@link DataSourceSelector} 注解时生效.
     *
     * @param proceedingJoinPoint 处理连接点, 即被 {@code @DataSourceSelector} 注解的方法
     * @return 目标方法调用结束后返回的结果, 返回值类型由具体的方法决定
     * @throws Throwable 目标方法在执行过程中可能抛出的错误或异常, 由调用方决定如何处理该错误或异常
     */
    @Around("@annotation(DataSourceSelector)")
    public Object selectDataSource(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = signature.getMethod();
            DataSourceSelector selector = method.getAnnotation(DataSourceSelector.class);
            DataSourceContext.set(selector.value());
            return proceedingJoinPoint.proceed();
        } finally {
            DataSourceContext.clear();
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }
}
