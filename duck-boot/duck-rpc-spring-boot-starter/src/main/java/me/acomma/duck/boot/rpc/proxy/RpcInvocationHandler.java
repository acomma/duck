package me.acomma.duck.boot.rpc.proxy;

import me.acomma.duck.boot.rpc.domain.RpcInvocation;
import me.acomma.duck.boot.rpc.domain.RpcRequest;
import me.acomma.duck.boot.rpc.domain.RpcResponse;
import me.acomma.duck.boot.rpc.domain.Service;
import me.acomma.duck.boot.rpc.exception.RpcException;
import me.acomma.duck.boot.rpc.loadbalance.LoadBalance;
import me.acomma.duck.boot.rpc.registry.RegistryService;
import me.acomma.duck.boot.rpc.remoting.NettyClient;
import me.acomma.duck.boot.rpc.serialize.Serializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class RpcInvocationHandler implements InvocationHandler {
    private final Class<?> clazz;
    private final RegistryService registryService;
    private final LoadBalance loadBalance;

    public RpcInvocationHandler(Class<?> clazz, RegistryService registryService, LoadBalance loadBalance) {
        this.clazz = clazz;
        this.registryService = registryService;
        this.loadBalance = loadBalance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String serviceName = this.clazz.getName();

        List<Service> services = this.registryService.discovery(serviceName);
        Service service = loadBalance.select(services);
        if (service == null) {
            throw new RpcException("未找到服务");
        }

        RpcInvocation invocation = new RpcInvocation();
        invocation.setServiceName(serviceName);
        invocation.setMethodName(method.getName());
        invocation.setParameterTypes(method.getParameterTypes());
        invocation.setArguments(args);

        RpcRequest request = new RpcRequest();
        request.setSerializationType(Serializer.HESSIAN);
        request.setRequestId(System.currentTimeMillis());
        request.setInvocation(invocation);

        NettyClient client = new NettyClient(service.getHost(), service.getPort());
        RpcResponse response = client.send(request);

        return response.getResult().recreate();
    }
}
