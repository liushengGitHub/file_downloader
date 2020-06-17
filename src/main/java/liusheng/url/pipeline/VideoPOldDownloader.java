package liusheng.url.pipeline;

import liusheng.url.bilibili.VideoPOld;
import liusheng.url.utils.DownloadUtils;
import liusheng.url.utils.HttpClientUtils;
import liusheng.url.utils.ProcessBuilderUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 年: 2020  月: 04 日: 06 小时: 00 分钟: 09
 * 用户名: LiuSheng
 */

public class VideoPOldDownloader implements Downloader<VideoPOld> {

    private String ffmpeg;

    public String getFfmpeg() {
        return ffmpeg;
    }

    public void setFfmpeg(String ffmpeg) {
        this.ffmpeg = ffmpeg;
    }

    public void download(VideoPOld videoPOld, DownloadListener downloadListener) throws Exception {
        String name = StringUtils.fileNameHandle(videoPOld.getMessage());
        Path dirPath = Paths.get("file", name);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        Path namePath = dirPath.resolve(videoPOld.getPage() + "_" + name + ".mp4");
        List<String> urls = videoPOld.getData().getDurl().stream()
                .map(VideoPOld.DataBean.DurlBean::getUrl)
                .collect(Collectors.toList());
        DownloadInfoList downloadInfoList = new DownloadInfoList();
        downloadInfoList.setTarget(namePath.toAbsolutePath().toString());
        downloadInfoList.setSource(org.apache.commons.lang3.StringUtils.join(urls, "###"));
        downloadInfoList.setType(0);
        downloadListener.listen(downloadInfoList);


        int[] n = new int[1];

        urls.stream()
                .forEach(url -> {
                    DownloadInfoList downloadInfo = new DownloadInfoList();
                    downloadInfo.setSource(url);
                    String tmpFilePath = Paths.get(downloadInfoList.target() + (n[0]++) + ".temp").toAbsolutePath().toString();
                    downloadInfo.setTarget(tmpFilePath);
                    downloadInfo.setType(0);
                    downloadInfoList.addDownloadInfo(downloadInfo);
                    downloadListener.listen(downloadInfo);
                });


        // 进行处理
        HttpClient client = HttpClientUtils.bilibiliHttpClient();

        for (DownloadInfoList d : downloadInfoList.downloadInfos()) {
            DownloadUtils.handleUrl(client, downloadInfoList, d, downloadListener, downloadInfo -> {
                try {
                    this.mergeVideo(downloadInfo);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            handleUrl(client, downloadInfoList, d, downloadListener);
        }

    }

    private void mergeVideo(DownloadInfo<? extends DownloadInfo> downloadInfoList) throws Exception {
        // 合成 视频
        Path namePath = Paths.get(downloadInfoList.target());
        List<String> partsList = downloadInfoList.downloadInfos().stream()
                .map(DownloadInfo::target).collect(Collectors.toList());
        Path fileList = Paths.get(namePath + ".list");

        BufferedWriter bufferedWriter = Files.newBufferedWriter(fileList);

        for (String filePath : partsList) {
            bufferedWriter.write("file  " + filePath.replace("\\", "\\\\"));
            bufferedWriter.newLine();
        }
        bufferedWriter.close();

        ProcessBuilderUtils.executeAndDiscardOuput(ffmpeg, "-y", "-f", "concat", "-safe", "0", "-i", "\"" + fileList.toAbsolutePath().toString()
                + "\"", "-c", "copy", "\"" + namePath.toAbsolutePath().toString() + "\"");

        Files.delete(fileList);
        downloadInfoList.downloadInfos()
                .forEach(downloadInfo -> {
                    try {
                        Files.delete(Paths.get(downloadInfo.target()));
                    } catch (IOException e) {
                        throw new RuntimeException();
                    }
                });
    }
/*
    public static void main(String[] args) throws Exception {
        String ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);
        ProcessBuilderUtils.executeAndDiscardOuput(ffmpeg, "-y", "-f", "concat", "-safe", "0", "-i", "\"" + "F:\\NodeProject\\file_downloader\\file\\【JOJO】【MMD】荒木庄的各位Gentleman【60FPS】.mp4.list".toString()
                + "\"", "-c", "copy", "\"" + "F:\\NodeProject\\file_downloader\\file\\【JOJO】【MMD】荒木庄的各位Gentleman【60FPS】.mp4".toString() + "\"");
    }*/

    private void handleUrl(HttpClient client, DownloadInfoList downloadInfoList, DownloadInfo downloadInfo, DownloadListener downloadListener) throws Exception {

        String filePathStr = downloadInfo.target();
        String url = downloadInfo.source();
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        InputStream audioInputStream = response.getEntity().getContent();

        Path filePath = Paths.get(filePathStr);
        OutputStream outputStream = Files.newOutputStream(filePath);
        String contentLength = response.getFirstHeader(HttpHeaders.CONTENT_LENGTH).getValue();
        long length1 = Long.parseLong(contentLength);
        downloadInfoList.addTotal(length1);

        downloadInfo.addTotal(length1);

        byte[] bytes = new byte[1024000];
        int length = -1;
        while ((length = audioInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, length);
            downloadInfo.addCurrent(length);
            downloadInfoList.addCurrent(length);
            if (Objects.nonNull(downloadListener)) {
                downloadListener.listen(downloadInfoList);
                downloadListener.listen(downloadInfo);
            }
        }

        if (downloadInfoList.getTotal() <= downloadInfoList.current()) {
            mergeVideo(downloadInfoList);
        }
        audioInputStream.close();
        outputStream.close();
    }
}
