package liusheng.url.pipeline;

/**
 * 年: 2020  月: 04 日: 04 小时: 16 分钟: 48
 * 用户名: LiuSheng
 */

public interface HandlerListener {
    void listen(UrlHandlerContext urlHandlerContext, HandlerChain handlerChain) throws Exception;

    HandlerListener DEFAULT_LISTENER = new DefaultHandlerListener();
}
