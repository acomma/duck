package me.acomma.duck.boot.rpc.serialize;

import me.acomma.duck.boot.rpc.exception.RpcException;

public class SerializerFactory {
    public static Serializer getSerializer(byte type) {
        if (type == Serializer.HESSIAN) {
            return new HessianSerializer();
        }
        throw new RpcException("序列化类型错误");
    }
}
