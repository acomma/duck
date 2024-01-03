package me.acomma.duck.boot.rpc.remoting;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import me.acomma.duck.boot.rpc.codec.RpcDecoder;
import me.acomma.duck.boot.rpc.codec.RpcEncoder;
import me.acomma.duck.boot.rpc.domain.RpcFuture;
import me.acomma.duck.boot.rpc.domain.RpcRequest;
import me.acomma.duck.boot.rpc.domain.RpcResponse;

/**
 * Netty 客户端。
 */
public class NettyClient {
    // 要连接的服务器的地址
    private final String host;
    // 要链接的服务器的端口
    private final int port;
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;

        this.bootstrap = new Bootstrap();
        this.eventLoopGroup = new NioEventLoopGroup(4);
        this.bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 注意 Pipeline 的顺序
                        ch.pipeline()
                                .addLast(new RpcEncoder())
                                .addLast(new RpcDecoder())
                                .addLast(new RpcResponseHandler());
                    }
                });
    }

    public RpcResponse send(RpcRequest request) throws InterruptedException {
        RpcFuture future = new RpcFuture(request.getRequestId());

        ChannelFuture channelFuture = this.bootstrap.connect(host, port).sync();
        channelFuture.addListener((ChannelFutureListener) cf -> {
            if (!channelFuture.isSuccess()) {
                channelFuture.cause().printStackTrace();
                this.eventLoopGroup.shutdownGracefully();
            }
        });

        channelFuture.channel().writeAndFlush(request);

        return future.get();
    }
}
