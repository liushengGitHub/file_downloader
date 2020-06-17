package liusheng.url.utils;

import liusheng.url.pipeline.DownloadInfo;
import liusheng.url.pipeline.DownloadInfoList;
import liusheng.url.pipeline.DownloadListener;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.bytedeco.javacv.FrameFilter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 2020年:  04 月:  10 日:  21小时:  36分钟:
 * 用户名: 👨‍LiuSheng👨‍
 */

public class DownloadUtils {
    public static void handleUrl(HttpClient client, DownloadInfo<? extends DownloadInfo> downloadInfoList, DownloadInfo downloadInfo
            , DownloadListener downloadListener, Consumer<? super DownloadInfo> mergeAction
   ) throws Exception {
        String filePathStr = downloadInfo.target();
        String url = downloadInfo.source();
        HttpGet get = new HttpGet(url);
        long total = downloadInfo.total();
        if (total > 0) {
            get.addHeader("Range", "bytes=" + downloadInfo.current() + "-" + (total - 1));
        }
        HttpResponse response = client.execute(get);
        if (response.getStatusLine().getStatusCode() != 200) {
            /// 应该加入到任务队列
            // 从数据中移除任务
          /*  failAction.accept(downloadInfo);*/
            return;
        }
        InputStream audioInputStream = response.getEntity().getContent();
        RandomAccessFile file = new RandomAccessFile(filePathStr, "rw");
        String contentLength = response.getFirstHeader(HttpHeaders.CONTENT_LENGTH).getValue();
        long length1 = Long.parseLong(contentLength);
        if (total <= 0) {
            downloadInfoList.addTotal(length1);
            downloadInfo.addTotal(length1);
            file.setLength(downloadInfo.total());
        }
        file.seek(downloadInfo.current());
        retryTransfer(downloadInfoList, downloadInfo, downloadListener, audioInputStream, file);
        downloadInfo.state(1);
        downloadListener.listen(downloadInfo);
        if (downloadInfoList.total() <= downloadInfoList.current() && complete(downloadInfoList)) {
            if (Objects.nonNull(mergeAction)) {
                mergeAction.accept(downloadInfoList);
                downloadInfoList.state(1);
                downloadListener.listen(downloadInfoList);
                downloadInfoList.downloadInfos().stream().forEach(t -> {
                    try {
                        Files.delete(Paths.get(t.target()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

    }

    public static void retryTransfer(DownloadInfo<? extends DownloadInfo> downloadInfoList, DownloadInfo downloadInfo, DownloadListener downloadListener, InputStream audioInputStream, RandomAccessFile file) throws IOException {
        for (int i = 0; i < 3; i++) {
            try {
                transfer(downloadInfoList, downloadInfo, downloadListener, audioInputStream, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void transfer(DownloadInfo<? extends DownloadInfo> downloadInfoList, DownloadInfo downloadInfo, DownloadListener downloadListener, InputStream audioInputStream, RandomAccessFile file) throws IOException {
        byte[] bytes = new byte[1024000];
        int length = -1;
        while ((length = audioInputStream.read(bytes)) != -1) {
            file.write(bytes, 0, length);
            downloadInfo.addCurrent(length);
            downloadInfoList.addCurrent(length);
            if (Objects.nonNull(downloadListener)) {
                downloadListener.listen(downloadInfoList);
                downloadListener.listen(downloadInfo);
            }
        }
        downloadInfoList.countIncrement();
        audioInputStream.close();
        file.close();
    }

    private static boolean complete(DownloadInfo<?> downloadInfoList) {
        return downloadInfoList.downloadInfos().size() == downloadInfoList.count();
    }
}
