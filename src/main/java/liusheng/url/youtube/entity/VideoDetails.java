package liusheng.url.youtube.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 年: 2020  月: 02 日: 04 小时: 22 分钟: 38
 * 用户名: LiuSheng
 */

public class VideoDetails {
    private Map<Integer, YtFile> ytFileMap = new HashMap<>();

    private VideoMeta videoMeta;

    public VideoMeta getVideoMeta() {
        return videoMeta;
    }

    public void setVideoMeta(VideoMeta videoMeta) {
        this.videoMeta = videoMeta;
    }

    public Map<Integer, YtFile> getYtFileMap() {
        return ytFileMap;
    }

    public void setYtFileMap(Map<Integer, YtFile> ytFileMap) {
        this.ytFileMap = ytFileMap;
    }
}
