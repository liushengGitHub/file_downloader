package liusheng.url.pipeline;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 年: 2020  月: 04 日: 04 小时: 15 分钟: 06
 * 用户名: LiuSheng
 */

public class DefaultHandlerContext implements UrlHandlerContext {

    public static final String TAG = "://";
    private String url;
    private String origin;
    private String path;
    private String queryString;

    private Map<String, Object> state = new HashMap<>();

    //

    private BlockingQueue<Runnable> executeRunnableQueue ;

    public DefaultHandlerContext(String url, LinkedBlockingQueue<Runnable> blockingQueue) {
        this.executeRunnableQueue = blockingQueue;
        this.url = url;
        int i = url.indexOf(TAG);
        int end = url.indexOf("/", i + TAG.length());
        if (end == -1) {
            origin = url;
            path = "";
            queryString = "";
        } else {
            origin = url.substring(0, end);
            int queryIndex = url.indexOf("?");
            if (queryIndex == -1) {
                path = url.substring(end);
                queryString = "";
            }else {
                queryString = url.substring(queryIndex + 1);
                path = url.substring(end,queryIndex);
            }
        }
    }

    @Override
    public Map<String, Object> state() {
        return state;
    }

    @Override
    public Pipeline pipeline() {
        return null;
    }

    @Override
    public String href() {
        return url;
    }

    @Override
    public String path() {

        return path;
    }

    @Override
    public String origin() {
        return origin;
    }

    @Override
    public String queryString() {
        return queryString;
    }

    @Override
    public void execute(Runnable command) {
        try {
            executeRunnableQueue.put(command);
        } catch (InterruptedException e) {
            throw  new RuntimeException(e);
        }
    }
}
