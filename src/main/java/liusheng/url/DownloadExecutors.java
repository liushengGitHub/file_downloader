package liusheng.url;

import liusheng.url.main.controller.MapperDownloadListener;
import liusheng.url.pipeline.DownloadInfo;
import liusheng.url.utils.HttpClientUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * 2020年:  04 月:  12 日:  17小时:  02分钟:
 * 用户名: 👨‍LiuSheng👨‍
 */

public class DownloadExecutors {
    private static HttpClient DEFAULT_HTTP_CLIENT =  HttpClients
            .custom()
            .build();


    private static MapperDownloadListener downloadListener = new MapperDownloadListener();
    private static YoutubeMergeAction youtubeMergeAction = new YoutubeMergeAction();

    private static DownloadController youtubeControlller = new DownloadController();
    private static DownloadController bilibiliControlller = new DownloadController();
    private static DownloadFailAction downloadFailAction = new DownloadFailAction();

    static {
       /* a= 1;*/
        youtubeExecutor = new DownloadExecutor.DownloadExecutorBuilder()
                .changeListener(downloadListener)
                .httpClient(HttpClientUtils.youtubeClient())
                .mergeAction(youtubeMergeAction)
                .downloadController(youtubeControlller)
                .failAction(downloadFailAction)
                .build();
        bilibibliExecutor = new DownloadExecutor.DownloadExecutorBuilder()
                .changeListener(downloadListener)
                .httpClient(HttpClientUtils.bilibiliHttpClient())
               /* .mergeAction()*/
                .downloadController(bilibiliControlller)
                .failAction(downloadFailAction)
                .build();

    }
   /* private static  int  a =0;*/
    private static DownloadExecutor youtubeExecutor;
    private static DownloadExecutor bilibibliExecutor;

/*
    public static void main(String[] args) {
        System.out.println(youtubeExecutor);
        System.out.println(a);
    }
*/

    public static void executeBilibili(DownloadInfo<? extends DownloadInfo> downloadInfo){

    }
    public static void executeYoutube(DownloadInfo<? extends DownloadInfo> downloadInfo){

    }
}
