package me.acomma.duck.boot.rpc.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class RpcResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Object result;
    private Throwable exception;

    public RpcResult(Object result) {
        this.result = result;
    }

    public RpcResult(Throwable exception) {
        this.exception = exception;
    }

    public Object recreate() throws Throwable {
        if (exception != null) {
            throw exception;
        }
        return result;
    }
}
