package me.acomma.duck.boot.rpc.proxy;

import me.acomma.duck.boot.rpc.loadbalance.LoadBalance;
import me.acomma.duck.boot.rpc.registry.RegistryService;

import java.lang.reflect.Proxy;

public class JdkProxyFactory {
    private final RegistryService registryService;
    private final LoadBalance loadBalance;

    public JdkProxyFactory(RegistryService registryService, LoadBalance loadBalance) {
        this.registryService = registryService;
        this.loadBalance = loadBalance;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new RpcInvocationHandler(clazz, registryService, loadBalance));
    }
}
