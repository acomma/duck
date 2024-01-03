package me.acomma.duck.boot.rpc.domain;

import me.acomma.duck.boot.rpc.exception.RpcException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RpcInvoker {
    private static final Map<String, RpcInvoker> INVOKERS = new HashMap<>();

    private final Object bean;

    public RpcInvoker(Object bean) {
        this.bean = bean;
    }

    public static RpcInvoker get(String serviceName) {
        return RpcInvoker.INVOKERS.get(serviceName);
    }

    public static void put(String serviceName, RpcInvoker invoker) {
        RpcInvoker.INVOKERS.put(serviceName, invoker);
    }

    public RpcResult invoke(RpcInvocation invocation) {
        try {
            return new RpcResult(doInvoke(this.bean, invocation.getMethodName(), invocation.getParameterTypes(), invocation.getArguments()));
        } catch (InvocationTargetException e) {
            return new RpcResult(e.getTargetException());
        } catch (Throwable e) {
            throw new RpcException("调用方法 " + invocation.getMethodName() + " 失败", e);
        }
    }

    private Object doInvoke(Object bean, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable {
        Method method = bean.getClass().getMethod(methodName, parameterTypes);
        return method.invoke(bean, arguments);
    }
}
