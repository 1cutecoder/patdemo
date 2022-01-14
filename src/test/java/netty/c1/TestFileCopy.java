package netty.c1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;

/**
 * 类描述
 *
 * @author zcl
 * @Description TODO
 * @Date 2022/1/2 22:20
 */
public class TestFileCopy {
    public static void main(String[] args) throws IOException {
        String source = "D:\\workspace\\atguigu\\src\\test\\netty\\c1\\file";
        String target = "D:\\workspace\\atguigu\\src\\test\\netty\\c1\\copyto";
        Files.walk(Paths.get(source)).forEach(path -> {
            try {
                String targetName = path.toString().replace(source, target);
                if (Files.isDirectory(path)) {
                    Files.createDirectory(Paths.get(targetName));
                } else if (Files.isRegularFile(path)) {
                    Files.copy(path, Paths.get(targetName));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
