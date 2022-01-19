package netty.c3;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author zcl
 * @date 2022/1/7 15:38
 */
@Slf4j
public class NettyPromise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EventLoop eventLoop = new NioEventLoopGroup().next();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);
        new Thread(() -> {
            //任意一个线程执行计算，计算完毕后向promise 填充结果
            log.debug("开始计算...");
            try {
                TimeUnit.SECONDS.sleep(1);
                //int i=1/0;
            } catch (InterruptedException e) {
                promise.setFailure(e);
                e.printStackTrace();
            }
            promise.setSuccess(80);
        }).start();
        log.debug("等待结果...");
        log.debug("结果是{}", promise.get());
    }
}
