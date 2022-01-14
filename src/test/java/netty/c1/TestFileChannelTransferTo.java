package netty.c1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 类描述
 *
 * @author zcl
 * @Description 数据传输
 * @Date 2022/1/2 21:15
 */
public class TestFileChannelTransferTo {
    public static void main(String[] args) {
        FileChannel from = null;
        FileChannel to = null;
        try {
            from = new FileInputStream("src/data.txt").getChannel();
            to = new FileOutputStream("src/to.txt").getChannel();
            long size = from.size();
            for (long left = size; left > 0; ) {
                System.out.println("position:" + (size - left) + " left:" + left);
                left -= from.transferTo((size - left), left, to);
            }
        } catch (IOException e) {
            closeResouece(from);
            closeResouece(to);
            e.printStackTrace();
        } finally {
            closeResouece(from);
            closeResouece(to);
        }
    }

    private static void closeResouece(FileChannel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
