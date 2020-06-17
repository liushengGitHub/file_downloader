package liusheng.url.pipeline;

import java.util.*;
import java.util.concurrent.*;

/**
 * 年: 2020  月: 04 日: 04 小时: 14 分钟: 50
 * 用户名: LiuSheng
 */

public class DefaultPipeline implements Pipeline {
    private BlockingQueue<String> urlQueues = new LinkedBlockingQueue<String>();

    private ListHashMap<String, Handler> handlerMap = new ListHashMap<>();


    private List<Handler> handlers = new ArrayList<>(128);

    int threadNumber = Runtime.getRuntime().availableProcessors();
    private ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);

    public void addData(String url) {
        try {
            urlQueues.put(url);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }

    public synchronized void use(String route, Handler handler) {
        handlerMap.add(route, handler);
        handlers.add(handler);
    }

    public void start() throws Exception {
        for (int i = 0; i < threadNumber; i++) {
            LinkedBlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
            executorService.execute(() -> {
                while (true) {
                    try {
                        Runnable runnable;
                        while (Objects.nonNull(runnable = blockingQueue.poll())) {
                            try {
                                runnable.run();
                            } catch (Throwable throwable) {
                                throw new RuntimeException(throwable);
                            }
                        }
                        String urlOrigin = urlQueues.take();

                        DefaultHandlerContext handlerContext = new DefaultHandlerContext(urlOrigin, blockingQueue);
                        String url = handlerContext.origin();
                        List<Handler> handlers = handleChain(url);
                        handlers.addAll(handleChain("*"));
                        if (Objects.isNull(handlers) || handlers.isEmpty()) return;
                        handlers.sort(Comparator.comparing(handlers::indexOf));
                       /* HandlerChain handlerChain = new DefaultHandlerChain(handlers, handlerContext);
                        handlerChain.next();*/
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            });
        }
    }

    private String handle(String url) {
        int i = url.indexOf("?");

        return url.substring(0, i == -1 ? url.length() : i);
    }

    private synchronized List<Handler> handleChain(String url) {
        List<Handler> handlers = handlerMap.get(url);
        return Objects.isNull(handlers) ? new ArrayList<>(8) : handlers;
    }

    @Override
    public Executor execute() {
        return null;
    }

    @Override
    public Pipeline addHandlerLast(Handler handler) {
        return null;
    }

    @Override
    public Pipeline addHandlerLast(String name, Handler handler) {
        return null;
    }

    @Override
    public Pipeline addHandlerFirst(Handler handler) {
        return null;
    }

    @Override
    public Pipeline addHandlerFirst(String name, Handler handler) {
        return null;
    }

    @Override
    public void handle(Object o) {

    }

    @Override
    public void removeHandler(Handler handler) {

    }
}
