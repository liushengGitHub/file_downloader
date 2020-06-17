package liusheng.url.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.expression.Expression;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringValueResolver;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 2020年:  04 月:  10 日:  17小时:  36分钟:
 * 用户名: 👨‍LiuSheng👨‍
 */

public class ExecutorServices {



    private static int bilibiliThreadNumber;
    private static ExecutorService bilibiliThreadService;
    private static ExecutorService youtubeThreadService;

    public synchronized static ExecutorService bilibiliExecuteService() {

        if (Objects.isNull(bilibiliThreadService)) {
            bilibiliThreadService = Executors.newFixedThreadPool(5);
        }
        return  bilibiliThreadService;
    }

    public static ExecutorService youtubeExecuteService() {
        if (Objects.isNull(youtubeThreadService)) {
            youtubeThreadService = Executors.newFixedThreadPool(3);
        }
        return  youtubeThreadService;
    }
}
