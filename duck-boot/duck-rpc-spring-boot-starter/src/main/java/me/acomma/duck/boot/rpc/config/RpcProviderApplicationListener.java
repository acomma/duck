package me.acomma.duck.boot.rpc.config;

import me.acomma.duck.boot.rpc.annotation.RpcService;
import me.acomma.duck.boot.rpc.domain.RpcInvoker;
import me.acomma.duck.boot.rpc.domain.Service;
import me.acomma.duck.boot.rpc.exception.RpcException;
import me.acomma.duck.boot.rpc.registry.RegistryService;
import me.acomma.duck.boot.rpc.remoting.NettyServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class RpcProviderApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    private final RpcProperties properties;
    private final RegistryService registryService;

    public RpcProviderApplicationListener(RpcProperties properties, RegistryService registryService) {
        this.properties = properties;
        this.registryService = registryService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        // Spring 包含多个容器，比如 Web 容器和核心容器，它们之间还有父子关系，为了避免重复执行注册，只处理顶层容器
        if (context.getParent() == null) {
            registerService(context);
            startServer();
        }
    }

    private void registerService(ApplicationContext context) {
        Map<String, Object> beans = context.getBeansWithAnnotation(RpcService.class);
        if (!beans.isEmpty()) {
            String host;
            try {
                host = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                throw new RpcException("获取服务地址失败", e);
            }

            for (Map.Entry<String, Object> entry : beans.entrySet()) {
                Object bean = entry.getValue();
                Class<?> clazz = bean.getClass();
                RpcService annotation = clazz.getAnnotation(RpcService.class);

                // 获取服务接口的类型
                Class<?> interfaceClass = null;
                if (annotation.interfaceClass() != null && annotation.interfaceClass() != void.class) {
                    interfaceClass = annotation.interfaceClass();
                } else {
                    Class<?>[] interfaces = bean.getClass().getInterfaces();
                    if (interfaces.length > 0) {
                        interfaceClass = interfaces[0];
                    }
                }

                if (interfaceClass != null) {
                    String serviceName = interfaceClass.getName();

                    Service service = new Service();
                    service.setHost(host);
                    service.setPort(properties.getServer().getPort());
                    service.setName(serviceName);

                    registryService.register(service);

                    RpcInvoker invoker = new RpcInvoker(bean);
                    RpcInvoker.put(serviceName, invoker);
                }
            }
        }
    }

    private void startServer() {
        new Thread(() -> {
            NettyServer server = new NettyServer(properties.getServer().getPort());
            server.start();
        }).start();
    }
}
