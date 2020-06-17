package liusheng.url.bilibili;

import liusheng.url.pipeline.DefaultPipeline;
import liusheng.url.pipeline.QueryParamHandler;

/**
 * 年: 2020  月: 04 日: 04 小时: 15 分钟: 33
 * 用户名: LiuSheng
 */

public class Main {
    public static void main(String[] args) throws Exception {
       /* DefaultPipeline pipeline = new DefaultPipeline();

        pipeline.use(new QueryParamHandler());
        pipeline.use(new BilibiliIdHandler());

        pipeline.use(new VideoInfoParser());
        pipeline.use(new VideoPParser());
        new Thread(()->{
            try {
                Thread.sleep(2000);

                pipeline.addData("https://www.bilibili.com/video/BV1CE411A7m2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        pipeline.start();*/
    }
}
