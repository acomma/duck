package me.acomma.duck.boot.rpc.remoting;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.acomma.duck.boot.rpc.domain.RpcInvoker;
import me.acomma.duck.boot.rpc.domain.RpcRequest;
import me.acomma.duck.boot.rpc.domain.RpcResponse;
import me.acomma.duck.boot.rpc.domain.RpcResult;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * RPC 请求处理器。
 */
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcRequest> {
    // 业务线程池
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        threadPoolExecutor.submit(() -> {
            RpcInvoker invoker = RpcInvoker.get(request.getInvocation().getServiceName());
            RpcResult result = invoker.invoke(request.getInvocation());

            RpcResponse response = new RpcResponse();
            response.setSerializationType(request.getSerializationType());
            response.setRequestId(request.getRequestId());
            response.setResult(result);

            ctx.writeAndFlush(response);
        });
    }
}
