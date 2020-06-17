package liusheng.url.main.service;

import liusheng.url.main.MappperDownloadInfo;
import liusheng.url.pipeline.DownloadInfo;

import java.util.List;

/**
 * 年: 2020  月: 04 日: 06 小时: 11 分钟: 12
 * 用户名: LiuSheng
 */

public interface DownloadInfoService {

   void insertDownloadInfo(MappperDownloadInfo downloadInfo);

    void updateDownloadInfo(MappperDownloadInfo downloadInfo);

    MappperDownloadInfo<MappperDownloadInfo> selectDownloadInfoById(Integer id);

    List<MappperDownloadInfo<MappperDownloadInfo>> selectDownloadInfosByParentIdIsNull();
}
