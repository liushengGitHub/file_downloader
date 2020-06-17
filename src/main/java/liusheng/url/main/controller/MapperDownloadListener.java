package liusheng.url.main.controller;

import liusheng.url.main.MappperDownloadInfo;
import liusheng.url.main.service.DownloadInfoService;
import liusheng.url.pipeline.DownloadInfo;
import liusheng.url.pipeline.DownloadListener;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 年: 2020  月: 04 日: 09 小时: 14 分钟: 37
 * 用户名: LiuSheng
 */

public class MapperDownloadListener implements DownloadListener<DownloadInfo> {
    private DownloadInfoService downloadInfoService;


    private long size = 1024000;

    @Override
    public void listen(DownloadInfo downloadInfo) {
        MappperDownloadInfo<MappperDownloadInfo>  mappperDownloadInfoMappperDownloadInfo= (MappperDownloadInfo<MappperDownloadInfo>) downloadInfo;
        Integer id = mappperDownloadInfoMappperDownloadInfo.id();
        if (Objects.isNull(id)) {
            downloadInfoService.insertDownloadInfo(mappperDownloadInfoMappperDownloadInfo);
        } else {
            MappperDownloadInfo<MappperDownloadInfo> mappperDownloadInfo = downloadInfoService.selectDownloadInfoById(id);
            long currentOrigin = mappperDownloadInfo.current();
            long current = downloadInfo.current();


            if (current - currentOrigin >= size || current >= currentOrigin) {
                downloadInfoService.updateDownloadInfo(mappperDownloadInfoMappperDownloadInfo);
                System.out.println(Thread.currentThread().getName() + "=" +downloadInfo);
            }
        }


    }

    public DownloadInfoService getDownloadInfoService() {
        return downloadInfoService;
    }

    public void setDownloadInfoService(DownloadInfoService downloadInfoService) {
        this.downloadInfoService = downloadInfoService;
    }


}
