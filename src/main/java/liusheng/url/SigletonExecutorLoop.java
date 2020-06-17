package liusheng.url;

import liusheng.url.pipeline.Handler;
import liusheng.url.pipeline.Pipeline;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 2020年:  04 月:  13 日:  11小时:  47分钟:
 * 用户名: 👨‍LiuSheng👨‍
 */

public class SigletonExecutorLoop<T> implements ExecutorLoop {
    private final Queue<Runnable> queue = new LinkedBlockingQueue<>();
    private volatile Thread thread;
    private AtomicBoolean start = new AtomicBoolean(false);
    private int number = 0;
    private volatile Pipeline<T> pipeline;
    private SourceGroup<T> sourceGroup = new SourceGroup<>();
    private Handler handler;


    public SigletonExecutorLoop() {
        pipeline = new DefaultPipeline(this);
    }

    @Override
    public void run() {
        if (!start.compareAndSet(false, true)) {
            throw new RuntimeException();
        }
        thread = thread.currentThread();

        for (; ; ) {
            try {
                T t = sourceGroup.get();
                if (Objects.isNull(t)) {
                    if (++number >= 100) {
                        number = 0;
                        TimeUnit.SECONDS.sleep(1);
                    }
                    continue;
                }
                // 进行处理
                pipeline.handle(t);



            } catch (Throwable e) {
                e.printStackTrace();
            }finally {
                executeAllTask();
            }

        }
    }

    private void executeAllTask() {
        Runnable runnable;
        while ((runnable = queue.poll()) != null) {
            runnable.run();
        }
    }

    @Override
    public void execute(Runnable command) {
        queue.offer(command);
    }

    @Override
    public void register(Source source) {
        queue.offer(() -> {
            sourceGroup.addSource(source);
        });
    }
}
