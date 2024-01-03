package me.acomma.duck.boot.rpc.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RpcResponse {
    private byte serializationType;
    private long requestId;
    private RpcResult result;
}
