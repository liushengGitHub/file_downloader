package liusheng.url;

import liusheng.url.pipeline.Handler;
import liusheng.url.pipeline.HandlerChain;
import liusheng.url.pipeline.Pipeline;

/**
 * 2020年:  04 月:  14 日:  13小时:  02分钟:
 * 用户名: 👨‍LiuSheng👨‍
 */

public abstract class HandlerGroup implements Handler {
    @Override
    public void handle(HandlerContext handlerContext, HandlerChain handlerChain) throws Exception {
        Pipeline pipeline = handlerContext.pipeline();

        addHandler(pipeline);
        pipeline.removeHandler();
    }

    protected abstract void addHandler(Pipeline pipeline);
}
