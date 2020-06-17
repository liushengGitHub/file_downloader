package liusheng.url;

import liusheng.url.pipeline.Pipeline;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 年: 2020  月: 04 日: 14 小时: 13 分钟: 00
 * 用户名: LiuSheng
 */

public interface HandlerContext<T> extends Executor {
    /**
     * 存储过程数据
     * @return
     */
    Map<String, Object> state();


    Pipeline<T> pipeline();

    T  source();

}
