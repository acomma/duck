package me.acomma.duck.boot.rpc.registry;

import me.acomma.duck.boot.rpc.domain.Service;
import me.acomma.duck.boot.rpc.exception.RpcException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class ZookeeperRegistryService implements RegistryService {
    private final ServiceDiscovery<Service> serviceDiscovery;

    public ZookeeperRegistryService(String registryAddress) {
        try {
            CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddress, new ExponentialBackoffRetry(1000, 3));
            client.start();

            JsonInstanceSerializer<Service> serializer = new JsonInstanceSerializer<>(Service.class);
            serviceDiscovery = ServiceDiscoveryBuilder.builder(Service.class)
                    .client(client)
                    .basePath("/duck/rpc/service")
                    .serializer(serializer)
                    .build();

            serviceDiscovery.start();
        } catch (Exception e) {
            throw new RpcException("启动服务发现失败", e);
        }
    }

    @Override
    public void register(Service service) {
        try {
            ServiceInstance<Service> serviceInstance = ServiceInstance.<Service>builder()
                    .name(service.getName())
                    .address(service.getHost())
                    .port(service.getPort())
                    .payload(service)
                    .build();
            serviceDiscovery.registerService(serviceInstance);
        } catch (Exception e) {
            throw new RpcException("注册服务失败", e);
        }
    }

    @Override
    public void unregister(Service service) {
        try {
            ServiceInstance<Service> serviceInstance = ServiceInstance.<Service>builder()
                    .name(service.getName())
                    .address(service.getHost())
                    .port(service.getPort())
                    .payload(service)
                    .build();
            serviceDiscovery.unregisterService(serviceInstance);
        } catch (Exception e) {
            throw new RpcException("注销服务失败", e);
        }
    }

    @Override
    public List<Service> discovery(String serviceName) {
        try {
            Collection<ServiceInstance<Service>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
            if (serviceInstances == null || serviceInstances.isEmpty()) {
                return null;
            }
            return serviceInstances.stream().map(ServiceInstance::getPayload).toList();
        } catch (Exception e) {
            throw new RpcException("发现服务失败", e);
        }
    }

    @Override
    public void destroy() {
        try {
            serviceDiscovery.close();
        } catch (IOException e) {
            throw new RpcException("销毁服务发现失败", e);
        }
    }
}
