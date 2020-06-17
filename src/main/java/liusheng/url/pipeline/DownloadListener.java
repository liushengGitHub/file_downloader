package liusheng.url.pipeline;

import java.util.function.Consumer;

/**
 * 年: 2020  月: 04 日: 05 小时: 12 分钟: 56
 * 用户名: LiuSheng
 */

public interface DownloadListener<T extends DownloadInfo>  extends Consumer<T> {
    void listen(T downloadInfo);

    @Override
    default void accept(T t) {
        listen(t);
    }
}
