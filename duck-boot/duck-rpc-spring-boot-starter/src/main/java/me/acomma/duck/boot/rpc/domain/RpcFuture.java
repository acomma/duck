package me.acomma.duck.boot.rpc.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class RpcFuture {
    private static final Map<Long, RpcFuture> FUTURES = new HashMap<>();
    // 使用 CountDownLatch 实现异步转同步
    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    private RpcResponse response;

    public RpcFuture(Long requestId) {
        FUTURES.put(requestId, this);
    }

    public RpcResponse get() throws InterruptedException {
        this.countDownLatch.await();
        return this.response;
    }

    public static void received(RpcResponse response) {
        RpcFuture future = FUTURES.remove(response.getRequestId());
        future.doReceived(response);
    }

    private void doReceived(RpcResponse response) {
        this.response = response;
        this.countDownLatch.countDown();
    }
}
