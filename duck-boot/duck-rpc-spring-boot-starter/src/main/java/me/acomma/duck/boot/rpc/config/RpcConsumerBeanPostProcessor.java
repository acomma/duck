package me.acomma.duck.boot.rpc.config;

import me.acomma.duck.boot.rpc.annotation.RpcReference;
import me.acomma.duck.boot.rpc.proxy.JdkProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

public class RpcConsumerBeanPostProcessor implements BeanPostProcessor {
    private final JdkProxyFactory jdkProxyFactory;

    public RpcConsumerBeanPostProcessor(JdkProxyFactory jdkProxyFactory) {
        this.jdkProxyFactory = jdkProxyFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz;
        if (AopUtils.isAopProxy(bean)) {
            clazz = AopUtils.getTargetClass(bean);
        } else {
            clazz = bean.getClass();
        }

        try {
            for (Field field : clazz.getDeclaredFields()) {
                RpcReference reference = field.getAnnotation(RpcReference.class);
                if (reference != null) {
                    Class<?> targetClazz = field.getType();
                    Object proxy = jdkProxyFactory.getProxy(targetClazz);
                    field.setAccessible(true);
                    field.set(bean, proxy);
                }
            }
        } catch (Exception e) {
            throw new BeanCreationException(beanName, e);
        }

        return bean;
    }
}
