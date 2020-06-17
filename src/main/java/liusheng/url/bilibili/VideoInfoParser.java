package liusheng.url.bilibili;

import com.google.gson.Gson;
import liusheng.url.pipeline.Handler;
import liusheng.url.pipeline.HandlerChain;
import liusheng.url.pipeline.UrlHandlerContext;
import liusheng.url.pipeline.HandlerListener;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Map;
import java.util.Objects;

/**
 * 年: 2020  月: 03 日: 30 小时: 20 分钟: 01
 * 用户名: LiuSheng
 */

public class VideoInfoParser implements Handler {
    public static final String INFO = "https://api.bilibili.com/x/web-interface/view?bvid=%s&aid=%s";
    private Gson gson;

    private HandlerListener handlerListener = HandlerListener.DEFAULT_LISTENER;

    public HandlerListener getHandlerListener() {
        return handlerListener;
    }

    public void setHandlerListener(HandlerListener handlerListener) {
        this.handlerListener = handlerListener;
    }

    public Gson getGson() {
        return Objects.isNull(gson) ? gson = new Gson() : gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }


    @Override
    public void handle(UrlHandlerContext urlHandlerContext, HandlerChain handlerChain) throws Exception {
        Map<String, Object> state = urlHandlerContext.state();

        String infoUrl = process(state);

        CloseableHttpClient client = HttpClients.custom().build();

        CloseableHttpResponse response = client.execute(new HttpGet(infoUrl));

        if (response.getStatusLine().getStatusCode() == 200) {
            String json = EntityUtils.toString(response.getEntity());
            BilibiliVideoInfo videoInfo = getGson().fromJson(json, BilibiliVideoInfo.class);
            state.put("videoInfo", videoInfo);
            state.put("pn", "16");
        }

        handlerListener.listen(urlHandlerContext, handlerChain);
    }

    private String process(Map<String, Object> map) {
        String bvid = (String) map.get("bvid");
        String avid = (String) map.get("avid");
        return String.format(INFO, StringUtils.isBlank(bvid) ? "" : bvid, StringUtils.isBlank(avid) ? "" : avid);
    }
}
