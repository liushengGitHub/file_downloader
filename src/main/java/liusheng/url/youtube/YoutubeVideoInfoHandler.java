package liusheng.url.youtube;

import liusheng.url.pipeline.Handler;
import liusheng.url.pipeline.HandlerChain;
import liusheng.url.pipeline.UrlHandlerContext;
import liusheng.url.youtube.entity.VideoDetails;

import java.util.Arrays;
import java.util.Map;

/**
 * 2020年:  04 月:  10 日:  21小时:  01分钟:
 * 用户名: 👨‍LiuSheng👨‍
 */

public class YoutubeVideoInfoHandler implements Handler {

    @Override
    public void handle(UrlHandlerContext urlHandlerContext, HandlerChain handlerChain) throws Exception {
        DefaultYouTubeExtractor defaultYouTubeExtractor = new DefaultYouTubeExtractor();
        String href = urlHandlerContext.href();
        VideoDetails videoDetails = defaultYouTubeExtractor.extract(href);
        String responseStr = defaultYouTubeExtractor.getResponseStr();
        Map<String, Object> state = urlHandlerContext.state();
        state.put("responseStr", responseStr);
        state.put("videoDetails", videoDetails);
        state.put("ps", Arrays.asList(-1, 1080));
        handlerChain.next();
    }
}
