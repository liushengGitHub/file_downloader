package liusheng.url.pipeline;

import java.util.concurrent.Executor;

/**
 * 年: 2020  月: 04 日: 04 小时: 14 分钟: 42
 * 用户名: LiuSheng
 */

public interface Pipeline<T> {
    /**
     * 类似过滤器
     * @param handler
     */

/*
   default void use(Handler handler){
       use("*",handler);
   }

    void use(String route ,Handler handler);

    void start() throws Exception;*/
    //**当前现在的执行器
    Executor execute();

    /**
     * 添加处理器
     * @param handler
     * @return
     */
    Pipeline addHandlerLast(Handler handler);
    // 添加有名字的
    Pipeline addHandlerLast(String name ,Handler handler);

    Pipeline addHandlerFirst(Handler handler);
    // 添加有名字的
    Pipeline addHandlerFirst(String name ,Handler handler);

    void handle(T t) throws Exception;

    void removeHandler(Handler handler);

}
