package liusheng.url.youtube;

import liusheng.url.main.MappperDownloadInfo;
import liusheng.url.pipeline.*;
import liusheng.url.utils.DownloadUtils;
import liusheng.url.utils.ExecutorServices;
import liusheng.url.utils.HttpClientUtils;
import liusheng.url.utils.ProcessBuilderUtils;
import liusheng.url.youtube.entity.Format;
import liusheng.url.youtube.entity.VideoDetails;
import liusheng.url.youtube.entity.VideoMeta;
import liusheng.url.youtube.entity.YtFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 2020年:  04 月:  10 日:  21小时:  32分钟:
 * 用户名: 👨‍LiuSheng👨‍
 */

public class YoutubeVideoDownloadHandler implements Handler {
    private String ffmpeg;

    public String getFfmpeg() {
        return ffmpeg;
    }

    public void setFfmpeg(String ffmpeg) {
        this.ffmpeg = ffmpeg;
    }

    private DownloadListener<MappperDownloadInfo> downloadInfoDownloadListener;

    public DownloadListener<MappperDownloadInfo> getDownloadInfoDownloadListener() {
        return downloadInfoDownloadListener;
    }

    public void setDownloadInfoDownloadListener(DownloadListener<MappperDownloadInfo> downloadInfoDownloadListener) {
        this.downloadInfoDownloadListener = downloadInfoDownloadListener;
    }

    @Override
    public void handle(UrlHandlerContext urlHandlerContext, HandlerChain handlerChain) throws Exception {
        Map<String, Object> map = urlHandlerContext.state();

        VideoDetails videoDetails = (VideoDetails) map.get("videoDetails");

        VideoMeta videoMeta = videoDetails.getVideoMeta();

        Map<Integer, YtFile> ytFileMapOrigin = videoDetails.getYtFileMap();

        List<Integer> ps = (List<Integer>) map.get("ps");
        int size = ps.size();
        if (Objects.isNull(ps) || ps.isEmpty()) {
            ps = Arrays.asList(-1, 1080);
        }
        if (size > 2) {
            ps = ps.subList(0, 2);
        }
        Map<Integer, YtFile> ytFileMap = convert(ytFileMapOrigin);
        List<YtFile> ytFiles = ps.stream()
                .map(height -> {
                    YtFile ytFile = ytFileMap.get(height);
                    if (Objects.isNull(ytFile)) {
                        ytFile = ytFileMap.get( ytFileMap.keySet()
                                .stream().mapToInt(Integer::intValue)
                                .filter(h-> h != height)
                                .max().orElse(height));
                    }
                    return ytFile;
                }).filter(Objects::nonNull).collect(Collectors.toList());
        List<String> urls = ytFiles.stream().map(YtFile::getUrl).collect(Collectors.toList());
        String title = videoMeta.getTitle();
        if (StringUtils.isBlank(title)) {
            title = UUID.randomUUID().toString();
        }

        HttpClient httpClient = HttpClientUtils.youtubeClient();
        title = liusheng.url.pipeline.StringUtils.fileNameHandle(title) + "." + getExt(ytFiles);

        Path file = Paths.get("file", title);

        DownloadInfoList downloadInfoList = new DownloadInfoList();
        downloadInfoList.setSource(StringUtils.join(urls));
        downloadInfoList.setType(1);
        String target = file.toAbsolutePath().toString();
        downloadInfoList.setTarget(target + ".mp4");
        downloadInfoDownloadListener.listen(downloadInfoList);
        Consumer<? super DownloadInfo> mergeAction = downloadInfo -> {
            try {
                merge(downloadInfoList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        if (size == 1) {
            handleUrl(urls, httpClient, downloadInfoList, target, mergeAction);
            return;
        }

        int[] n = new int[]{0};
        urls.forEach(url -> {
            handUrl0(url, downloadInfoList, target + "_" + (n[0] ++));
        });

        downloadInfoList.downloadInfos()
                .stream().map(downloadInfo ->  (Runnable)()->{
                    try {
                        downloadInfo.setParentDownloadInfo(downloadInfoList);
                        DownloadUtils.handleUrl(httpClient, downloadInfoList, downloadInfo, downloadInfoDownloadListener, mergeAction);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).forEach(ExecutorServices.youtubeExecuteService()::execute);
    }

    private void merge(DownloadInfoList downloadInfoList) throws Exception {
        List<DownloadInfoList> downloadInfos = downloadInfoList.downloadInfos();
        String target = downloadInfoList.target();
        List<String> param = new ArrayList<>();
        param.addAll(Arrays.asList(ffmpeg, "-y"));
        List<String> params = downloadInfos.stream()
                .flatMap(downloadInfo -> {
                    String target1 = downloadInfo.target();
                    return Arrays.asList("-i", "\"" + target1
                            + "\"").stream();
                }).collect(Collectors.toList());
        param.addAll(params);
        param.add("\"" + target + "\"");
        ProcessBuilderUtils.executeAndDiscardOuput(param.toArray(new String[0]));
    }

    private void handleUrl(List<String> urls, HttpClient httpClient, DownloadInfoList downloadInfoList, String target, Consumer<? super DownloadInfo> mergeAction) throws Exception {
        DownloadInfoList downloadInfo = handUrl0(urls.get(0), downloadInfoList, target);

        DownloadUtils.handleUrl(httpClient, downloadInfoList, downloadInfo, downloadInfoDownloadListener, mergeAction);
    }

    private DownloadInfoList handUrl0(String url, DownloadInfoList downloadInfoList, String target) {
        DownloadInfoList downloadInfo = new DownloadInfoList();
        downloadInfo.setParentId(downloadInfoList.getId());
        downloadInfo.setSource(url);
        downloadInfo.setType(1);
        downloadInfo.setTarget(target + ".temp");
        downloadInfoList.addDownloadInfo(downloadInfo);
        downloadInfoDownloadListener.listen(downloadInfo);
        return downloadInfo;
    }

    private String getExt(List<YtFile> ytFiles) {
        String ext = "";
        for (YtFile ytFile : ytFiles) {
            Format format = ytFile.getFormat();
            if (format.getHeight() != -1) {
                return format.getExt();
            } else {
                ext = format.getExt();
            }
        }
        return ext;
    }

    private Map<Integer, YtFile> convert(Map<Integer, YtFile> ytFileMap) {

        return ytFileMap
                .entrySet()
                .stream()
                .collect(HashMap::new, (hashMap, integerYtFileEntry) -> {
                    YtFile value = integerYtFileEntry.getValue();

                    int height = value.getFormat().getHeight();
                    YtFile ytFile = hashMap.get(height);
                    if (Objects.isNull(ytFile)) {
                        hashMap.put(height, value);
                        return;
                    }
                    if (Objects.nonNull(ytFile) && (value.getFormat().getExt().equals("webm"))) {
                        hashMap.put(height, value);
                    }

                }, Map::putAll);

    }
}
