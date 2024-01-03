package me.acomma.duck.boot.rpc.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Service {
    // 服务的地址
    private String host;
    // 服务的端口
    private int port;
    // 服务的名称
    private String name;
}
