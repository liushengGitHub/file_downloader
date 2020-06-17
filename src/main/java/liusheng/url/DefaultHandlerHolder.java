package liusheng.url;

import liusheng.url.pipeline.Handler;

/**
 * 2020年:  04 月:  13 日:  12小时:  21分钟:
 * 用户名: 👨‍LiuSheng👨‍
 */

public class DefaultHandlerHolder implements HandlerHolder{


    private Handler handler;
    private HandlerInfo handleInfo;
    private int order;

    public DefaultHandlerHolder(Handler handler, HandlerInfo handleInfo) {
        this(handler,handleInfo,Order.DEFAULT_ORDER);
    }
    public DefaultHandlerHolder(Handler handler, HandlerInfo handleInfo, int order) {
        this.handler = handler;
        this.handleInfo = handleInfo;
        this.order = order;
    }

    @Override
    public Handler handler() {
        return handler;
    }

    @Override
    public HandlerInfo handleInfo() {
        return handleInfo;
    }
/*
    @Override
    public String antPattern() {
        return antPattern;
    }*/

    @Override
    public int order() {
        return order;
    }

}
