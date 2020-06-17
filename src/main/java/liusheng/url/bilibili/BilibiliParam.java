package liusheng.url.bilibili;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * 年: 2020  月: 03 日: 30 小时: 20 分钟: 06
 * 用户名: LiuSheng
 */

public class BilibiliParam {
    private String avid;
    private String bvid;
    private String qn;
    private String cid;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getAvid() {
        return avid;
    }

    public void setAvid(String avid) {
        this.avid = avid;
    }

    public String getBvid() {
        return bvid;
    }

    public void setBvid(String bvid) {
        this.bvid = bvid;
    }

    public String getQn() {
        return qn;
    }

    public void setQn(String qn) {
        this.qn = qn;
    }

    @Override
    public String toString() {
        HashMap<String, String> map = new HashMap<>();
        map.put("avid", avid);
        map.put("cid", cid);
        map.put("bvid", bvid);
        map.put("qn", qn);
        return map.entrySet()
                .stream()
                .map(entry -> {
                    if (StringUtils.isAnyBlank()) {
                        return "";
                    } else {
                        return entry.getKey() + "=" + entry.getValue();
                    }
                }).filter(StringUtils::isNotBlank).collect(Collectors.joining("&"));
    }
}
