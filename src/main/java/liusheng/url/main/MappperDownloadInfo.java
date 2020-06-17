package liusheng.url.main;

import liusheng.url.pipeline.DownloadInfo;

/**
 * 年: 2020  月: 04 日: 09 小时: 14 分钟: 47
 * 用户名: LiuSheng
 */

public interface MappperDownloadInfo<T extends MappperDownloadInfo> extends DownloadInfo<T> {
    Integer id();
    Integer parentId();
    Integer sourceId();
}
