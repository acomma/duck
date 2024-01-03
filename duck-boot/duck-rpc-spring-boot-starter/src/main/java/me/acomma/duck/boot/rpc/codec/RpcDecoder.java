package me.acomma.duck.boot.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import me.acomma.duck.boot.rpc.domain.RpcInvocation;
import me.acomma.duck.boot.rpc.domain.RpcRequest;
import me.acomma.duck.boot.rpc.domain.RpcResponse;
import me.acomma.duck.boot.rpc.domain.RpcResult;
import me.acomma.duck.boot.rpc.exception.RpcException;
import me.acomma.duck.boot.rpc.serialize.Serializer;
import me.acomma.duck.boot.rpc.serialize.SerializerFactory;

import java.util.List;

/**
 * RPC 解码器。
 */
public class RpcDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 可读字节数小于 16（请求头长度）时无法解析出有效数据长度
        if (in.readableBytes() < 16) {
            return;
        }

        // 标记可读数据包的开始位置
        in.markReaderIndex();

        // 魔数
        short magic = in.readShort();
        if (magic != (short) 0xdaac) {
            throw new RpcException("魔数不正确");
        }

        // 协议版本
        byte version = in.readByte();
        // 消息类型，请求/响应
        byte messageType = in.readByte();
        // 请求 ID
        long requestId = in.readLong();
        // 有效数据长度
        int length = in.readInt();

        // 后续可读字节数小于有效数据长度，此时不是一个完整的数据包，还原数据包的可读数据位置
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        // 有效数据内容
        byte[] payload = new byte[length];
        in.readBytes(payload);

        Serializer serializer = SerializerFactory.getSerializer(Serializer.HESSIAN);

        switch (messageType) {
            case 1:
                RpcRequest request = new RpcRequest();
                request.setSerializationType(Serializer.HESSIAN);
                request.setRequestId(requestId);
                request.setInvocation(serializer.deserialize(payload, RpcInvocation.class));
                out.add(request);
                break;
            case 2:
                RpcResponse response = new RpcResponse();
                response.setSerializationType(Serializer.HESSIAN);
                response.setRequestId(requestId);
                response.setResult(serializer.deserialize(payload, RpcResult.class));
                out.add(response);
                break;
        }
    }
}
