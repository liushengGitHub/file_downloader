package liusheng.url.pipeline;

import liusheng.url.HandlerContext;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 年: 2020  月: 04 日: 04 小时: 14 分钟: 44
 * 用户名: LiuSheng
 */

public interface UrlHandlerContext extends HandlerContext {

    String href();

    String path();

    String origin();

    String queryString();

}
