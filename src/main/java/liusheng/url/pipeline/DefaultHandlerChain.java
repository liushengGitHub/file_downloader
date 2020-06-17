package liusheng.url.pipeline;

import liusheng.url.HandlerContext;
import liusheng.url.HandlerHolder;

import java.util.List;

/**
 * 年: 2020  月: 04 日: 04 小时: 15 分钟: 02
 * 用户名: LiuSheng
 */

public class DefaultHandlerChain implements HandlerChain {
    private List<HandlerHolder> handlers ;
    private HandlerContext handlerContext;

    private int index = 0;

    public DefaultHandlerChain(List<HandlerHolder> handlers, HandlerContext handlerContext) {
        this.handlers = handlers;
        this.handlerContext = handlerContext;
    }

    @Override
    public void next() throws Exception {
        if (index >= handlers.size()) {
            return;
        }
        handlers.get(index++).handler().handle(handlerContext,this);
    }
}
