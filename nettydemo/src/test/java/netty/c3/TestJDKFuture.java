package netty.c3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author zcl
 * @date 2022/1/7 15:22
 */
@Slf4j
public class TestJDKFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() {
                log.debug("执行计算");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 50;
            }
        });
        log.debug("等待结果");
        log.debug("结果是{}", future.get());
        future.cancel(false);
    }
}
