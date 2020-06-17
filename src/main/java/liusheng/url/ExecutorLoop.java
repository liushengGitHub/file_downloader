package liusheng.url;

import java.util.concurrent.Executor;

/**
 * 年: 2020  月: 04 日: 13 小时: 10 分钟: 47
 * 用户名: LiuSheng
 */

public interface ExecutorLoop<T> extends Runnable, Executor {
    void register(Source<T> source);
}
