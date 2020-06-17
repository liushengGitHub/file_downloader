package liusheng.url.youtube;


import liusheng.url.youtube.entity.VideoDetails;

/**
 * 年: 2020  月: 02 日: 04 小时: 22 分钟: 36
 * 用户名: LiuSheng
 */

public interface YouTubeExtractor {
    VideoDetails extract(String youtubeLink);
}
