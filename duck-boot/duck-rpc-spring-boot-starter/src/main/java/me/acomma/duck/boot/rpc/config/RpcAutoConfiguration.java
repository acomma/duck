package me.acomma.duck.boot.rpc.config;

import me.acomma.duck.boot.rpc.loadbalance.LoadBalance;
import me.acomma.duck.boot.rpc.loadbalance.RandomLoadBalance;
import me.acomma.duck.boot.rpc.proxy.JdkProxyFactory;
import me.acomma.duck.boot.rpc.registry.RegistryService;
import me.acomma.duck.boot.rpc.registry.ZookeeperRegistryService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RpcProperties.class)
public class RpcAutoConfiguration {
    @Bean
    public LoadBalance loadBalance() {
        return new RandomLoadBalance();
    }

    @Bean
    public RegistryService registryService(RpcProperties properties) {
        return new ZookeeperRegistryService(properties.getRegistry().getAddress());
    }

    @Bean
    public JdkProxyFactory jdkProxyFactory(RegistryService registryService, LoadBalance loadBalance) {
        return new JdkProxyFactory(registryService, loadBalance);
    }

    @Bean
    public RpcProviderApplicationListener rpcProviderApplicationListener(RpcProperties properties, RegistryService registryService) {
        return new RpcProviderApplicationListener(properties, registryService);
    }

    @Bean
    public RpcConsumerBeanPostProcessor rpcConsumerBeanPostProcessor(JdkProxyFactory jdkProxyFactory) {
        return new RpcConsumerBeanPostProcessor(jdkProxyFactory);
    }
}
