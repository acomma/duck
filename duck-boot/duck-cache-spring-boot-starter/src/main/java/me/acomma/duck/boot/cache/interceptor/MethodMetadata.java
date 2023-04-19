package me.acomma.duck.boot.cache.interceptor;

import lombok.Getter;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 方法的元数据.
 */
@Getter
public class MethodMetadata {
    private final Method method;

    private final Object[] args;

    private final Object target;

    private final Class<?> targetClass;

    private final Method targetMethod;

    public MethodMetadata(MethodInvocation invocation) {
        this.method = BridgeMethodResolver.findBridgedMethod(invocation.getMethod());
        this.args = extractArgs(method, invocation.getArguments());
        this.target = invocation.getThis();
        this.targetClass = getTargetClass(target);
        this.targetMethod = (!Proxy.isProxyClass(targetClass) ? AopUtils.getMostSpecificMethod(invocation.getMethod(), targetClass) : method);
    }

    private Object[] extractArgs(Method method, Object[] args) {
        if (!method.isVarArgs()) {
            return args;
        }
        Object[] varArgs = ObjectUtils.toObjectArray(args[args.length - 1]);
        Object[] combinedArgs = new Object[args.length - 1 + varArgs.length];
        System.arraycopy(args, 0, combinedArgs, 0, args.length - 1);
        System.arraycopy(varArgs, 0, combinedArgs, args.length - 1, varArgs.length);
        return combinedArgs;
    }

    private Class<?> getTargetClass(Object target) {
        return AopProxyUtils.ultimateTargetClass(target);
    }
}
