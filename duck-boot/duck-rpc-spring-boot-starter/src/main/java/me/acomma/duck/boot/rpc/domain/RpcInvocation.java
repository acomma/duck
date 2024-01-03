package me.acomma.duck.boot.rpc.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class RpcInvocation implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String serviceName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] arguments;
}
