package liusheng.url;

import liusheng.url.pipeline.DownloadInfo;
import liusheng.url.pipeline.DownloadListener;

/**
 * 年: 2020  月: 04 日: 05 小时: 15 分钟: 43
 * 用户名: LiuSheng
 */

public class DefaultDownloadListener implements DownloadListener<DownloadInfo> {
    @Override
    public void listen(DownloadInfo downloadInfo) {
        System.out.println(downloadInfo.current() + " = " + downloadInfo.total() + "=" + downloadInfo.target());
    }

    @Override
    public void accept(DownloadInfo downloadInfo) {

    }
}
