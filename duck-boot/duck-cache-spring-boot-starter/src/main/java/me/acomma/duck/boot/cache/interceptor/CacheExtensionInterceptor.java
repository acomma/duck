package me.acomma.duck.boot.cache.interceptor;

import lombok.extern.slf4j.Slf4j;
import me.acomma.duck.boot.cache.CacheMetadata;
import me.acomma.duck.boot.cache.CacheMetadataRegistry;
import me.acomma.duck.boot.cache.CacheExtension;
import me.acomma.duck.boot.cache.expression.CacheOperationExpressionEvaluator;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 缓存扩展的拦截器.
 */
@Slf4j
public class CacheExtensionInterceptor implements MethodInterceptor, BeanFactoryAware {
    private final CacheOperationExpressionEvaluator evaluator = new CacheOperationExpressionEvaluator();

    private final CacheMetadataRegistry cacheMetadataRegistry;

    private BeanFactory beanFactory;

    public CacheExtensionInterceptor(CacheMetadataRegistry cacheMetadataRegistry) {
        this.cacheMetadataRegistry = cacheMetadataRegistry;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        MethodMetadata metadata = new MethodMetadata(invocation);

        if (!metadata.getTargetClass().equals(metadata.getTarget().getClass())) {
            return invocation.proceed();
        }

        Method targetMethod = metadata.getTargetMethod();
        if (targetMethod.isAnnotationPresent(CacheExtension.class)) {
            CacheExtension cacheExtension = targetMethod.getAnnotation(CacheExtension.class);
            Cacheable cacheable = targetMethod.getAnnotation(Cacheable.class);
            CacheMetadata cacheMetadata = getCacheKey(cacheable, cacheExtension, metadata);
            cacheMetadataRegistry.register(cacheMetadata);
        }

        return invocation.proceed();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public CacheMetadata getCacheKey(Cacheable cacheable, CacheExtension cacheExtension, MethodMetadata methodMetadata) {
        Method method = methodMetadata.getMethod();
        Object[] args = methodMetadata.getArgs();
        Object target = methodMetadata.getTarget();
        Class<?> targetClass = methodMetadata.getTargetClass();
        Method targetMethod = methodMetadata.getTargetMethod();
        AnnotatedElementKey methodKey = new AnnotatedElementKey(targetMethod, targetClass);
        String keyExpression = cacheable.key();

        EvaluationContext evaluationContext = evaluator.createEvaluationContext(null, method, args, target, targetClass, targetMethod, CacheOperationExpressionEvaluator.NO_RESULT, beanFactory);
        Object key = evaluator.key(keyExpression, methodKey, evaluationContext);

        String cacheName = cacheable.cacheNames().length > 0 ? cacheable.cacheNames()[0] : "";

        return new CacheMetadata(cacheName, Objects.requireNonNull(key).toString(), cacheExtension.ttl());
    }
}
