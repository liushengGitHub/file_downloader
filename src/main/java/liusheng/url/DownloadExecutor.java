package liusheng.url;

import liusheng.url.pipeline.DownloadInfo;
import liusheng.url.pipeline.DownloadListener;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 2020年:  04 月:  12 日:  16小时:  34分钟:
 * 用户名: 👨‍LiuSheng👨‍
 */

public class DownloadExecutor {
    private final Consumer<? super DownloadInfo> changeListener;
    private final Consumer<? super DownloadInfo> mergeAction;
    private final HttpClient httpClient;
    private final Consumer<? super DownloadInfo> failAction;
    private final DownloadController downloadController;

    public DownloadExecutor(Consumer<? super DownloadInfo> changeListener, Consumer<? super DownloadInfo> mergeAction, HttpClient httpClient, Consumer<? super DownloadInfo> failAction, DownloadController downloadController) {
        this.changeListener = changeListener;
        this.mergeAction = mergeAction;
        this.httpClient = httpClient;
        this.failAction = failAction;
        this.downloadController = downloadController;
    }

    public Consumer<? super DownloadInfo> getChangeListener() {
        return changeListener;
    }

    public Consumer<? super DownloadInfo> getMergeAction() {
        return mergeAction;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public Consumer<? super DownloadInfo> getFailAction() {
        return failAction;
    }

    public DownloadController getDownloadController() {
        return downloadController;
    }

    public static class DownloadExecutorBuilder {
        private Consumer<? super DownloadInfo> changeListener;
        private Consumer<? super DownloadInfo> mergeAction;
        private HttpClient httpClient;
        private Consumer<? super DownloadInfo> failAction;
        private DownloadController downloadController;

        public DownloadExecutorBuilder changeListener(Consumer<? super DownloadInfo> changeListener) {
            this.changeListener = changeListener;
            return this;
        }

        public DownloadExecutorBuilder mergeAction(Consumer<? super DownloadInfo> mergeAction) {
            this.mergeAction = mergeAction;
            return this;
        }

        public DownloadExecutorBuilder failAction(Consumer<? super DownloadInfo> failAction) {
            this.failAction = failAction;
            return this;
        }

        public DownloadExecutorBuilder downloadController(DownloadController downloadController) {
            this.downloadController = downloadController;
            return this;
        }

        public DownloadExecutorBuilder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public  DownloadExecutor build() {
            return new DownloadExecutor(changeListener,mergeAction,httpClient,failAction,downloadController);
        }
    }

    public  void execute(DownloadInfo<? extends DownloadInfo> downloadInfo,DownloadController downloadController) throws Exception {
        DownloadInfo<? extends DownloadInfo> downloadInfoList = downloadInfo.parent();
        String filePathStr = downloadInfo.target();
        String url = downloadInfo.source();
        HttpGet get = new HttpGet(url);
        long total = downloadInfo.total();

        if (total > 0) {
            get.addHeader("Range", "bytes=" + downloadInfo.current() + "-" + (total - 1));
        }

        HttpResponse response = httpClient.execute(get);
        if (response.getStatusLine().getStatusCode() != 200) {
            /// 应该加入到任务队列
            // 从数据中移除任务
            failAction.accept(downloadInfo);
            return;
        }
        InputStream audioInputStream = response.getEntity().getContent();
        RandomAccessFile file = new RandomAccessFile(filePathStr, "rw");
        String contentLength = response.getFirstHeader(HttpHeaders.CONTENT_LENGTH).getValue();
        long length1 = Long.parseLong(contentLength);

        if (total <= 0) {
            downloadInfoList.addTotal(length1);
            changeListener.accept(downloadInfoList);
            downloadInfo.addTotal(length1);
            changeListener.accept(downloadInfo);
            file.setLength(downloadInfo.total());
        }
        file.seek(downloadInfo.current());

        retryTransfer(downloadInfoList, downloadInfo, changeListener, audioInputStream, file);
        downloadInfo.state(1);
        changeListener.accept(downloadInfo);


        if (downloadInfoList.total() <= downloadInfoList.current() && complete(downloadInfoList)) {
            if (Objects.nonNull(mergeAction)) {
                mergeAction.accept(downloadInfoList);
                downloadInfoList.state(1);
                changeListener.accept(downloadInfoList);
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
    public  void retryTransfer(DownloadInfo<? extends DownloadInfo> downloadInfoList, DownloadInfo downloadInfo,
                               Consumer<? super DownloadInfo> downloadListener, InputStream audioInputStream, RandomAccessFile file) throws IOException {
        for (int i = 0; i < 3; i++) {
            try {
                transfer(downloadInfoList, downloadInfo, downloadListener, audioInputStream, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void transfer(DownloadInfo<? extends DownloadInfo> downloadInfoList, DownloadInfo downloadInfo, Consumer<? super DownloadInfo>
            changeListener, InputStream audioInputStream, RandomAccessFile file) throws IOException {
        byte[] bytes = new byte[1024000];
        int length = -1;
        while ((length = audioInputStream.read(bytes)) != -1) {
            file.write(bytes, 0, length);
            downloadInfo.addCurrent(length);
            downloadInfoList.addCurrent(length);
            if (Objects.nonNull(changeListener)) {
                changeListener.accept(downloadInfo);
                changeListener.accept(downloadInfo);
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
