package netty.c5;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

import static netty.c1.ByteBufferUtil.debugAll;

/**
 * @author zcl
 * @date 2022/1/5 15:23
 */
@Slf4j
public class AioFileChannel {
    public static void main(String[] args) {
        AsynchronousFileChannel channel = null;
        try {
            channel = AsynchronousFileChannel.open(Paths.get("src/data.txt"), StandardOpenOption.READ);
            ByteBuffer buffer = ByteBuffer.allocate(16);
            log.debug("read begin...");
            channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    attachment.flip();
                    log.debug("read completed...");
                    debugAll(attachment);
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    System.out.println("read failed...");
                }
            });
            try {TimeUnit.SECONDS.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
            log.debug("read end...");
        } catch (IOException e) {
            closeResource(channel);
            e.printStackTrace();
        } finally {
            closeResource(channel);
        }
    }

    private static void closeResource(AsynchronousFileChannel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
