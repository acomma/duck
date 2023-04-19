package me.acomma.duck.boot.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Objects;

/**
 * 分布式锁切面.
 *
 * <p>当同时存在事务切面 {@link org.springframework.transaction.interceptor.TransactionInterceptor} 时,
 * 目前分布式锁切面在事务切面之前执行, 而事务切面默认的 {@code order} 是 {@link Ordered#LOWEST_PRECEDENCE},
 * 因此分布式锁切面需要设置一个比它小的数字才行.
 */
@Aspect
public class DistributedLockAspect implements Ordered {
    /**
     * 环绕通知, 当方法上有 {@link Lockable} 注解时生效.
     *
     * @param proceedingJoinPoint 处理连接点, 即被 {@code @Lockable} 注解的方法
     * @param lockable            描述方法的锁属性
     * @return 目标方法调用结束后返回的结果, 返回值类型由具体的方法决定
     * @throws Throwable 目标方法在执行过程中可能抛出的错误或异常, 由调用方决定如何处理该错误或异常
     */
    @Around("@annotation(lockable)")
    public Object locking(ProceedingJoinPoint proceedingJoinPoint, Lockable lockable) throws Throwable {
        ExpressionParser parser = new SpelExpressionParser();
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        EvaluationContext context = new StandardEvaluationContext();

        Object[] args = proceedingJoinPoint.getArgs();
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();

        String[] parameterNames = discoverer.getParameterNames(signature.getMethod());
        if (Objects.nonNull(parameterNames)) {
            for (int index = 0; index < parameterNames.length; index++) {
                context.setVariable(parameterNames[index], args[index]);
            }
        }

        Expression expression = parser.parseExpression(lockable.key());
        String key = expression.getValue(context, String.class);

        DistributedLock lock = DistributedLockFactory.create(key, lockable.timeout() * 1000L);
        if (!lock.tryLock()) {
            throw new DistributedLockException("获取锁 " + key + " 失败");
        }

        try {
            return proceedingJoinPoint.proceed();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }
}
