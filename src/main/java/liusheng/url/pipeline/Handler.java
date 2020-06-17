package liusheng.url.pipeline;

import liusheng.url.HandlerContext;

/**
 * 年: 2020  月: 04 日: 04 小时: 14 分钟: 43
 * 用户名: LiuSheng
 */

public interface Handler {
    void handle(HandlerContext handlerContext, HandlerChain handlerChain) throws Exception;


}
