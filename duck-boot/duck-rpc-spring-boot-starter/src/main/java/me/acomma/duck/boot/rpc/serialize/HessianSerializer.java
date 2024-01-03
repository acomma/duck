package me.acomma.duck.boot.rpc.serialize;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import me.acomma.duck.boot.rpc.exception.RpcException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class HessianSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T object) {
        byte[] result;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            Hessian2Output output = new Hessian2Output(os);
            output.writeObject(object);
            output.flush();
            result = os.toByteArray();
        } catch (Exception e) {
            throw new RpcException("序列化异常", e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        T result;
        try (ByteArrayInputStream is = new ByteArrayInputStream(data)) {
            Hessian2Input input = new Hessian2Input(is);
            result = (T) input.readObject(clazz);
        } catch (Exception e) {
            throw new RpcException("反序列化异常", e);
        }
        return result;
    }
}
