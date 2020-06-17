package liusheng.url.pipeline;

import java.util.List;

/**
 * 年: 2020  月: 04 日: 05 小时: 13 分钟: 45
 * 用户名: LiuSheng
 *
 *  下载的信息，表示一个文件的下载信息，下载进度
 *  */

public interface DownloadInfo<T extends DownloadInfo> {
    long total();

    long current();

    String source();

    String target();

    void addTotal(long length);

    void addCurrent(long length);

    void addDownloadInfo(T downloadInfo);

    List<T> downloadInfos();

    int count();

    void countIncrement();

    int type();

    int state();

    void state( int state);

    DownloadInfo<T> parent();
}
