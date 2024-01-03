package me.acomma.duck.boot.rpc.loadbalance;

import me.acomma.duck.boot.rpc.domain.Service;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance implements LoadBalance {
    private final Random random = new Random();

    @Override
    public Service select(List<Service> services) {
        if (services == null) {
            return null;
        }
        if (services.size() == 1) {
            return services.get(0);
        }
        int index = random.nextInt(services.size());
        return services.get(index);
    }
}
