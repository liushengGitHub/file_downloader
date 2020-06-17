package liusheng.url;

import java.util.Queue;

/**
 * 2020年:  04 月:  13 日:  17小时:  24分钟:
 * 用户名: 👨‍LiuSheng👨‍
 */

public class QueueSource<T> implements Source<T> {
    private Queue<T> queue;

    public QueueSource(Queue<T> queue) {
        this.queue = queue;
    }

    @Override
    public T get() {
        return queue.poll();
    }
}
