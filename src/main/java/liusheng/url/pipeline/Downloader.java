package liusheng.url.pipeline;

import liusheng.url.bilibili.VideoPNew;

/**
 * 年: 2020  月: 04 日: 06 小时: 17 分钟: 43
 * 用户名: LiuSheng
 */

public interface Downloader<T> {
    void download(T data, DownloadListener downloadListener) throws Exception;
}
