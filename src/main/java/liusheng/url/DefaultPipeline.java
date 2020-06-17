package liusheng.url;

import liusheng.url.pipeline.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 年: 2020  月: 04 日: 04 小时: 14 分钟: 50
 * 用户名: LiuSheng
 */

public class DefaultPipeline<T> implements Pipeline<T> {
    private Executor executor;


    private List<HandlerHolder> handlers = new ArrayList<>();

    public DefaultPipeline(Executor executor) {
        this.executor = executor;
    }

    @Override
    public Executor execute() {
        return executor;
    }

    @Override
    public Pipeline addHandlerLast(Handler handler) {
        return addHandlerLast(UUID.randomUUID().toString(), handler);
    }

    @Override
    public Pipeline addHandlerLast(String name, Handler handler) {

        handlers.add(new DefaultHandlerHolder(handler, new DefaultHandlerInfo(handler, name)));
        return this;
    }

    @Override
    public Pipeline addHandlerFirst(Handler handler) {
        return addHandlerFirst(UUID.randomUUID().toString(), handler);
    }

    @Override
    public Pipeline addHandlerFirst(String name, Handler handler) {
        handlers.add(0,new DefaultHandlerHolder(handler, new DefaultHandlerInfo(handler, name)));
        return this;
    }

    @Override
    public void handle(T t) throws Exception {
        HandlerContext<T> handlerContext = new DefaultHandlerContext<>(this,executor,t);
        HandlerChain handlerChain = new DefaultHandlerChain(handlers,handlerContext);
        handlerChain.next();
    }

    @Override
    public void removeHandler(Handler handler) {
        handlers.removeIf(handlerHolder -> handlerHolder.handler() == handler);
    }

}
