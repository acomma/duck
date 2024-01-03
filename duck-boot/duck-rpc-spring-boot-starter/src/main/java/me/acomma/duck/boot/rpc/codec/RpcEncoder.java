package me.acomma.duck.boot.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.acomma.duck.boot.rpc.domain.RpcRequest;
import me.acomma.duck.boot.rpc.domain.RpcResponse;
import me.acomma.duck.boot.rpc.serialize.Serializer;
import me.acomma.duck.boot.rpc.serialize.SerializerFactory;

/**
 * RPC 编码器。
 */
public class RpcEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof RpcRequest request) {
            encodeRequest(ctx, out, request);
        } else if (msg instanceof RpcResponse response) {
            encodeResponse(ctx, out, response);
        }
    }

    private void encodeRequest(ChannelHandlerContext ctx, ByteBuf out, RpcRequest request) {
        // 魔数
        out.writeShort(0xdaac);
        // 协议版本
        out.writeByte(1);
        // 请求
        out.writeByte(1);
        // 请求 ID
        out.writeLong(request.getRequestId());

        Serializer serializer = SerializerFactory.getSerializer(request.getSerializationType());
        byte[] payload = serializer.serialize(request.getInvocation());

        // 有效数据长度
        out.writeInt(payload.length);
        // 有效数据内容
        out.writeBytes(payload);
    }

    private void encodeResponse(ChannelHandlerContext ctx, ByteBuf out, RpcResponse response) {
        // 魔数
        out.writeShort(0xdaac);
        // 协议版本
        out.writeByte(1);
        // 响应
        out.writeByte(2);
        // 请求 ID
        out.writeLong(response.getRequestId());

        Serializer serializer = SerializerFactory.getSerializer(response.getSerializationType());
        byte[] payload = serializer.serialize(response.getResult());

        // 有效数据长度
        out.writeInt(payload.length);
        // 有效数据内容
        out.writeBytes(payload);
    }
}
