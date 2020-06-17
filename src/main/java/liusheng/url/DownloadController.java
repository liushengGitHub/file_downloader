package liusheng.url;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 2020年:  04 月:  12 日:  16小时:  41分钟:
 * 用户名: 👨‍LiuSheng👨‍
 */

public class DownloadController {
    private AtomicInteger state = new AtomicInteger();

    public void state(int state) {
        this.state.set(state);
    }
    public int state() {
        return  state.get();
    }

    public void pause() {
        state.set(-1);
    }
    public void cancel() {
        state.set(-2);
    }

}
