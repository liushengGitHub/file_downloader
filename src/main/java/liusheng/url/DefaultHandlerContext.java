package liusheng.url;

import liusheng.url.pipeline.Pipeline;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 2020年:  04 月:  14 日:  13小时:  04分钟:
 * 用户名: 👨‍LiuSheng👨‍
 */

public class DefaultHandlerContext<T> implements HandlerContext<T> {
    private final T t;
    private Map<String, Object> state = new HashMap<>();

    private Pipeline<T> pipeline;

    private Executor executor;


    public DefaultHandlerContext(Pipeline<T> pipeline, Executor executor, T t) {
        this.pipeline = pipeline;
        this.t = t;
        this.executor = executor;
    }

    @Override
    public Map<String, Object> state() {
        return state;
    }

    @Override
    public Pipeline<T> pipeline() {
        return pipeline;
    }

    @Override
    public T source() {
        return t;
    }

    @Override
    public void execute(Runnable command) {
        executor.execute(command);
    }
}
