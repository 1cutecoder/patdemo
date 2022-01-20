package protocol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zcl
 * @date 2022/1/20 9:44
 */
public class SequenceIdGenerator {
    private static final AtomicInteger id = new AtomicInteger();

    public static int nexId() {
        return id.incrementAndGet();
    }
}
