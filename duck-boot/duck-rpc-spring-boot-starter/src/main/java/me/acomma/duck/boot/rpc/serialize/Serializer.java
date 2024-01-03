package me.acomma.duck.boot.rpc.serialize;

public interface Serializer {
    byte HESSIAN = 0x1;

    <T> byte[] serialize(T object);

    <T> T deserialize(byte[] data, Class<T> clazz);
}
