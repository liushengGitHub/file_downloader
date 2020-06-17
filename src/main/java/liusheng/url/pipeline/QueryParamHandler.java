package liusheng.url.pipeline;

import java.util.HashMap;
import java.util.Map;

/**
 * 年: 2020  月: 04 日: 04 小时: 15 分钟: 58
 * 用户名: LiuSheng
 */

public class QueryParamHandler implements Handler {
    @Override
    public void handle(UrlHandlerContext urlHandlerContext, HandlerChain handlerChain) throws Exception {
        Map<String, Object> state = urlHandlerContext.state();

        String queryString = urlHandlerContext.queryString();
        String[] strings = queryString.split("&");
        Map<String, String> params = new HashMap<>();
        for (String str : strings
        ) {
            String[] kv = str.split("=", 2);
            if (kv.length == 2) {
                params.put(kv[0], kv[1]);
            }
        }
        state.put("params", params);
        handlerChain.next();
    }
}
