package liusheng.url;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 2020年:  04 月:  13 日:  17小时:  34分钟:
 * 用户名: 👨‍LiuSheng👨‍
 */

public class DefaultExecutorLoopGroup<T> implements ExecutorLoopGroup {

    private List<SigletonExecutorLoop<T>> executorLoops;
    private int loopNumber = Runtime.getRuntime().availableProcessors();
    private AtomicInteger number = new AtomicInteger();
    private Executor executor = new ThreadPerTaskExecutor(Thread::new);

    public DefaultExecutorLoopGroup() {
        executorLoops = new ArrayList<>(loopNumber);
        for (int i = 0; i < loopNumber; i++) {
            SigletonExecutorLoop<T> executorLoop = new SigletonExecutorLoop<T>();
            executor.execute(executorLoop);
            executorLoops.add(executorLoop);
        }
    }

    @Override
    public ExecutorLoop next() {
        return executorLoops.get(number.getAndIncrement() % loopNumber);
    }
}
