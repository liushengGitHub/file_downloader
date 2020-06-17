package liusheng.url.pipeline;

import liusheng.url.bilibili.VideoPNew;
import liusheng.url.utils.HttpClientUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.bytedeco.ffmpeg.ffmpeg;
import org.bytedeco.javacpp.Loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * 年: 2020  月: 04 日: 05 小时: 12 分钟: 54
 * 用户名: LiuSheng
 */

public class VideoPNewDownloader  implements Downloader<VideoPNew>{

    private String ffmpeg;

    public String getFfmpeg() {
        return ffmpeg;
    }

    public void setFfmpeg(String ffmpeg) {
        this.ffmpeg = ffmpeg;
    }

    public void download(VideoPNew videoPNew, DownloadListener downloadListener) throws Exception {
        String name = StringUtils.fileNameHandle(videoPNew.getMessage());
        Path dirPath = Paths.get("file",name);
        if (!Files.exists(dirPath)){
            Files.createDirectories(dirPath);
        }
        Path namePath = dirPath.resolve(videoPNew.getPage() + "_" +name + ".mp4");
        VideoPNew.DataBean.DashBean dash = videoPNew.getData()
                .getDash();
        List<VideoPNew.DataBean.DashBean.AudioBean> audio = dash.getAudio();
        List<VideoPNew.DataBean.DashBean.VideoBean> video = dash.getVideo();

        VideoPNew.DataBean.DashBean.AudioBean audioBean = audio.get(0);
        VideoPNew.DataBean.DashBean.VideoBean videoBean = video.get(0);

        String audioUrl = audioBean.getBaseUrl();

        String videoUrl = videoBean.getBaseUrl();

        HttpClient client = HttpClientUtils.bilibiliHttpClient();
        DownloadInfoList downloadInfoList = new DownloadInfoList();
        downloadInfoList.setTarget(namePath.toString());
        downloadInfoList.setSource(org.apache.commons.lang3.StringUtils.join(videoUrl,audioUrl));
        downloadListener.listen(downloadInfoList);

        addDownloadInfo(client,downloadListener, namePath.toString() + ".mp3", audioUrl, downloadInfoList);

        addDownloadInfo(client,downloadListener, namePath.toString() + ".mp4", videoUrl, downloadInfoList);

    }

    private void  addDownloadInfo(HttpClient httpClient,DownloadListener downloadListener,String filePath, String audioUrl, DownloadInfoList downloadInfoList) throws IOException {
        DownloadInfoList downloadInfo = new DownloadInfoList();
        downloadInfo.setSource(audioUrl);
        downloadInfo.setTarget(filePath);
        downloadInfo.setType(0);
        downloadInfo.setParentId(downloadInfoList.getId());
        downloadListener.listen(downloadInfo);

        downloadInfoList.addDownloadInfo(downloadInfo);
        handle(httpClient, downloadInfoList, downloadInfo,downloadListener);
    }

    private void handle(HttpClient client, DownloadInfoList downloadInfoList,DownloadInfoList downloadInfo, DownloadListener downloadListener) throws IOException {
        String audioUrl = downloadInfo.source();
        HttpGet get = new HttpGet(audioUrl);
        HttpResponse response = client.execute(get);
        InputStream audioInputStream = response.getEntity().getContent();
        String name = downloadInfo.target();

        Path filePath = Paths.get(name);
        OutputStream outputStream = Files.newOutputStream(filePath);
        String contentLength = response.getFirstHeader(HttpHeaders.CONTENT_LENGTH).getValue();
        long total = Long.parseLong(contentLength);
        downloadInfo.addTotal(total);
        downloadInfoList.addTotal(total);
        byte[] bytes = new byte[1024000];
        int length = -1;
        while ((length = audioInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, length);
            downloadInfo.addCurrent(length);
            downloadInfoList.addCurrent(length);
            if (Objects.nonNull(downloadListener)) {
                downloadListener.listen(downloadInfo);
                downloadListener.listen(downloadInfoList);
            }
        }
        audioInputStream.close();
        outputStream.close();
    }
}
