package liusheng.url.main.mapper;

import liusheng.url.main.MappperDownloadInfo;
import liusheng.url.pipeline.DownloadInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 年: 2020  月: 04 日: 06 小时: 11 分钟: 12
 * 用户名: LiuSheng
 */
@Mapper
public interface DownloadInfoMapper {
    int insertDownloadInfo(MappperDownloadInfo downloadInfo);

    void updateDownloadInfo(MappperDownloadInfo downloadInfo);

    MappperDownloadInfo<MappperDownloadInfo> selectDownloadInfoById(Integer id);

    List<MappperDownloadInfo<MappperDownloadInfo>> selectDownloadInfosByParentIdIsNull();

    List<MappperDownloadInfo<MappperDownloadInfo>> selectDownloadInfosByParentId(Integer parentId);
}
