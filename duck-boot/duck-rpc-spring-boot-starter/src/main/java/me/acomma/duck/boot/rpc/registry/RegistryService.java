package me.acomma.duck.boot.rpc.registry;

import me.acomma.duck.boot.rpc.domain.Service;

import java.util.List;

public interface RegistryService {
    void register(Service service);

    void unregister(Service service);

    List<Service> discovery(String serviceName);

    void destroy();
}
