package me.acomma.duck.boot.cache;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import java.io.Serial;
import java.lang.annotation.Annotation;

/**
 * 适用于方法上的注解的切面顾问.
 */
public class MethodAnnotationPointcutAdvisor extends AbstractPointcutAdvisor {
    @Serial
    private static final long serialVersionUID = 1552824470871887633L;

    private final Pointcut pointcut;

    private final Advice advice;

    public MethodAnnotationPointcutAdvisor(Class<? extends Annotation> methodAnnotationType, Advice advice) {
        this.pointcut = new AnnotationMatchingPointcut(null, methodAnnotationType);
        this.advice = advice;
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }
}
