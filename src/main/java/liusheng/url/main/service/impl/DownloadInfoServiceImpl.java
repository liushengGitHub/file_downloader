package liusheng.url.main.service.impl;

import liusheng.url.main.MappperDownloadInfo;
import liusheng.url.main.mapper.DownloadInfoMapper;
import liusheng.url.main.service.DownloadInfoService;

import java.util.List;

/**
 * 2020年:  04 月:  10 日:  15小时:  33分钟:
 * 用户名: 👨‍LiuSheng👨‍
 */

public class DownloadInfoServiceImpl  implements DownloadInfoService {
    private DownloadInfoMapper downloadInfoMapper ;

    public DownloadInfoMapper getDownloadInfoMapper() {
        return downloadInfoMapper;
    }

    public void setDownloadInfoMapper(DownloadInfoMapper downloadInfoMapper) {
        this.downloadInfoMapper = downloadInfoMapper;
    }

    @Override
    public void insertDownloadInfo(MappperDownloadInfo downloadInfo) {
        downloadInfoMapper.insertDownloadInfo(downloadInfo);
    }

    @Override
    public void updateDownloadInfo(MappperDownloadInfo downloadInfo) {
        downloadInfoMapper.updateDownloadInfo(downloadInfo);
    }

    @Override
    public MappperDownloadInfo<MappperDownloadInfo> selectDownloadInfoById(Integer id) {
       return downloadInfoMapper.selectDownloadInfoById(id);
    }

    @Override
    public List<MappperDownloadInfo<MappperDownloadInfo>> selectDownloadInfosByParentIdIsNull() {
        return downloadInfoMapper.selectDownloadInfosByParentIdIsNull();
    }
}
