package liusheng.url;

import liusheng.url.pipeline.Handler;

/**
 * 年: 2020  月: 04 日: 13 小时: 09 分钟: 14
 * 用户名: LiuSheng
 */

public interface HandlerHolder extends Order{


    /**
     * 执行的插件
     * @return
     */
    Handler handler();

    /**
     * 插件的名字 ，没有设置，默认uuid
     */
    HandlerInfo handleInfo();


   /* *//**
     * 插件的路径匹配
     *//*
    String antPattern();*/
}
