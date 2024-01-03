package me.acomma.duck.boot.rpc.loadbalance;

import me.acomma.duck.boot.rpc.domain.Service;

import java.util.List;

public interface LoadBalance {
    Service select(List<Service> services);
}
