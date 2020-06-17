package liusheng.url.bilibili;

import liusheng.url.pipeline.Handler;
import liusheng.url.pipeline.HandlerChain;
import liusheng.url.pipeline.UrlHandlerContext;

import java.util.Map;

/**
 * 年: 2020  月: 04 日: 04 小时: 16 分钟: 09
 * 用户名: LiuSheng
 */

public class BilibiliIdHandler implements Handler {
    @Override
    public void handle(UrlHandlerContext urlHandlerContext, HandlerChain handlerChain) throws Exception {

        String path = urlHandlerContext.path();
        Map<String, Object> map = urlHandlerContext.state();
        int endIndex = path.lastIndexOf("/");
        String substring = path.substring(endIndex + 1);
        if (substring.startsWith("av")) {
            map.put("avid", substring.substring(2));
        } else if (substring.startsWith("BV")) {
            map.put("bvid", substring);
        } else {
            throw new UnsupportedOperationException();
        }
        handlerChain.next();
    }
}
