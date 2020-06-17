package liusheng.url.pipeline;

/**
 * 年: 2020  月: 04 日: 04 小时: 16 分钟: 52
 * 用户名: LiuSheng
 */

public class DefaultHandlerListener implements HandlerListener {
    @Override
    public void listen(UrlHandlerContext urlHandlerContext, HandlerChain handlerChain) throws Exception {
        handlerChain.next();
    }
}
