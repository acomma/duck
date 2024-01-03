package me.acomma.duck.boot.rpc.remoting;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.acomma.duck.boot.rpc.domain.RpcFuture;
import me.acomma.duck.boot.rpc.domain.RpcResponse;

/**
 * RPC 响应处理器。
 */
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        RpcFuture.received(response);
    }
}
