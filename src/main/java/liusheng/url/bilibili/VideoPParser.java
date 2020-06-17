package liusheng.url.bilibili;

import com.google.gson.Gson;
import liusheng.url.pipeline.*;
import liusheng.url.utils.ExecutorServices;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 年: 2020  月: 03 日: 30 小时: 20 分钟: 27
 * 用户名: LiuSheng
 */

public class VideoPParser implements Handler {
    public static final String VIDEO_URL = "https://api.bilibili.com/x/player/playurl?avid=%s&cid=%s&bvid=%s&qn=%s";
    private Gson gson;
    private Downloader<VideoPNew> newDownloader;
    private Downloader<VideoPOld> oldDownloader;

    public Downloader<VideoPNew> getNewDownloader() {
        return newDownloader;
    }

    private DownloadListener downloadListener ;

    public DownloadListener getDownloadListener() {
        return downloadListener;
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public void setNewDownloader(Downloader<VideoPNew> newDownloader) {
        this.newDownloader = newDownloader;
    }

    public Downloader<VideoPOld> getOldDownloader() {
        return oldDownloader;
    }

    public void setOldDownloader(Downloader<VideoPOld> oldDownloader) {
        this.oldDownloader = oldDownloader;
    }

    public Gson getGson() {
        return Objects.isNull(gson) ? gson = new Gson() : gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    private HandlerListener handlerListener = HandlerListener.DEFAULT_LISTENER;

    public HandlerListener getHandlerListener() {
        return handlerListener;
    }

    public void setHandlerListener(HandlerListener handlerListener) {
        this.handlerListener = handlerListener;
    }

    @Override
    public void handle(UrlHandlerContext urlHandlerContext, HandlerChain handlerChain) throws Exception {
        Map<String, Object> state = urlHandlerContext.state();
        BilibiliVideoInfo videoInfo = (BilibiliVideoInfo) state.get("videoInfo");

        List<Integer> list = (List<Integer>) state.get("ps");
        String qn = (String) state.get("qn");

        if (StringUtils.isBlank(qn)) qn = "80";
        String qnFinal = qn;
        BilibiliVideoInfo.DataBean data = videoInfo.getData();
        List<BilibiliVideoInfo.DataBean.PagesBean> pages = data.getPages();
        CloseableHttpClient httpClient = HttpClients.custom().build();
        if (Objects.isNull(list)) {
            addQueue(urlHandlerContext, state, qnFinal, pages, httpClient, videoInfo.getData().getTitle());
        } else {
            pages = list.stream().map(i -> i - 1).map(pages::get)
                    .collect(Collectors.toList());
            addQueue(urlHandlerContext, state, qnFinal, pages, httpClient, videoInfo.getData().getTitle());
        }

        // handlerListener.listen(handlerContext,handlerChain);
    }

    private void addQueue(UrlHandlerContext urlHandlerContext, Map<String, Object> state, String qnFinal, List<BilibiliVideoInfo.DataBean.PagesBean> pages, CloseableHttpClient httpClient, String title) {
        pages.stream()
                .map(pagesBean -> {
                    return (Runnable) () -> {

                        String bvid = (String) state.get("bvid");
                        bvid = Objects.isNull(bvid) ? "" : bvid;
                        String avid = (String) state.get("avid");
                        avid = Objects.isNull(avid) ? "" : avid;
                        String videoUrl = String.format(VIDEO_URL, avid, pagesBean.getCid(), bvid, qnFinal);
                        try {
                            CloseableHttpResponse response = httpClient.execute(new HttpGet(videoUrl));

                            if (response.getStatusLine().getStatusCode() == 200) {
                                String json = EntityUtils.toString(response.getEntity());
                                Object o = null;
                                String message =  title  ;
                                if (json.contains("\"dash\"")) {
                                    VideoPNew videoPNew = getGson().fromJson(json, VideoPNew.class);
                                    videoPNew.setMessage(message);
                                    videoPNew.setPage(pagesBean.getPage() + "");
                                    newDownloader.download(videoPNew, downloadListener);
                                } else {
                                    VideoPOld videoPOld = getGson().fromJson(json, VideoPOld.class);
                                    videoPOld.setMessage(message);
                                    videoPOld.setPage(pagesBean.getPage() + "");
                                    oldDownloader.download(videoPOld,downloadListener);
                                }

                            }

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    };
                }).forEach(ExecutorServices.bilibiliExecuteService()::execute);
    }

}
