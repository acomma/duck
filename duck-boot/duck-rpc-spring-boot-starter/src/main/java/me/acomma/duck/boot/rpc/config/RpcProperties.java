package me.acomma.duck.boot.rpc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "duck.rpc")
public class RpcProperties {
    private ServerConfig server = new ServerConfig();
    private RegistryConfig registry = new RegistryConfig();

    @Getter
    @Setter
    public static class ServerConfig {
        private int port = 9090;
    }

    @Getter
    @Setter
    public static class RegistryConfig {
        private String address = "127.0.0.1:2181";
    }
}
