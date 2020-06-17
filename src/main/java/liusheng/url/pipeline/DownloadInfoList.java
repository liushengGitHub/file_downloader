package liusheng.url.pipeline;

import liusheng.url.main.MappperDownloadInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 年: 2020  月: 04 日: 05 小时: 14 分钟: 00
 * 用户名: LiuSheng
 */

public class DownloadInfoList implements MappperDownloadInfo<DownloadInfoList> {
    private List<DownloadInfoList> downloadInfos = new ArrayList<>();

    private Integer id;
    private String target;
    private long total;
    private long current;
    private String source;
    private Integer parentId;
    private Integer type;

    private int state;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<DownloadInfoList> getDownloadInfos() {
        return downloadInfos;
    }

    public void setDownloadInfos(List<DownloadInfoList> downloadInfos) {
        this.downloadInfos = downloadInfos;
    }

    public String getTarget() {
        return target;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public String getSource() {
        return source;
    }


    public void addTotal(long length) {
        total += length;
    }

    public void addCurrent(long length) {
        current += length;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void addDownloadInfo(DownloadInfoList downloadInfo) {
        /* this.downloadInfo = downloadInfo;*/
        downloadInfo.setParentId(this.getId());
        downloadInfos.add(downloadInfo);
    }

    @Override
    public List<DownloadInfoList> downloadInfos() {
        return getDownloadInfos();
    }

    private AtomicInteger countInteger = new AtomicInteger();

    @Override
    public int count() {
        return getCount();
    }

    public void countIncrement() {
        countInteger.getAndIncrement();
    }

    private Integer sourceId;

    @Override
    public int type() {
        return getType();
    }

    @Override
    public int state() {
        return state;
    }

    @Override
    public void state(int state) {
        this.state = state;
    }
    private DownloadInfo<DownloadInfoList> parentDownloadInfo;

    public AtomicInteger getCountInteger() {
        return countInteger;
    }

    public void setCountInteger(AtomicInteger countInteger) {
        this.countInteger = countInteger;
    }

    public DownloadInfo<DownloadInfoList> getParentDownloadInfo() {
        return parentDownloadInfo;
    }

    public void setParentDownloadInfo(DownloadInfo<DownloadInfoList> parentDownloadInfo) {
        this.parentDownloadInfo = parentDownloadInfo;
    }

    @Override
    public DownloadInfo<DownloadInfoList> parent() {
        return parentDownloadInfo;
    }

    public Integer getCount() {
        return countInteger.get();
    }

    public void setCount(Integer count) {
        this.countInteger.set(count);
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public long total() {
        return total;
    }

    @Override
    public long current() {
        return current;
    }

    @Override
    public String source() {
        if (downloadInfos.isEmpty()) return source;
        return source = downloadInfos.stream()
                .map(DownloadInfo::source).collect(Collectors.joining("###"));
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String target() {
        return target;
    }

    @Override
    public Integer id() {
        return id;
    }

    @Override
    public Integer parentId() {
        return parentId;
    }

    @Override
    public Integer sourceId() {
        return sourceId;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public String toString() {
        return "DownloadInfoList{" +
                "downloadInfos=" + downloadInfos +
                ", id=" + id +
                ", target='" + target + '\'' +
                ", total=" + total +
                ", current=" + current +
                ", source='" + source + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}
