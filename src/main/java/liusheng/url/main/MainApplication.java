package liusheng.url.main;

import liusheng.url.main.config.MainConfiguration;
import liusheng.url.main.mapper.DownloadInfoMapper;
import liusheng.url.main.service.DownloadInfoService;
import liusheng.url.pipeline.DefaultPipeline;
import liusheng.url.pipeline.DownloadInfo;
import liusheng.url.pipeline.DownloadListener;
import liusheng.url.utils.DownloadUtils;
import liusheng.url.utils.HttpClientUtils;
import liusheng.url.utils.ProcessBuilderUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 年: 2020  月: 04 日: 06 小时: 10 分钟: 46
 * 用户名: LiuSheng
 */

public class MainApplication {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfiguration.class);
        DefaultPipeline defaultPipeline = applicationContext.getBean("bilibiliPipeline", DefaultPipeline.class);
        defaultPipeline.start();

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                defaultPipeline.addData(scanner.nextLine().trim());
               /* System.out.println(applicationContext.getBean(PropertySourcesPlaceholderConfigurer.class).getAppliedPropertySources()
                        .get("jdbc.url"));*/
                /* System.out.println(applicationContext.getEnvironment().getProperty("jdbc.url"));*/
            }
        }).start();

        DownloadInfoService downloadInfoService = applicationContext.getBean(DownloadInfoService.class);
        DownloadListener downloadListener = applicationContext.getBean(DownloadListener.class);
        Object ffmpeg = applicationContext.getBean("ffmpeg");
        downloadInfoService.selectDownloadInfosByParentIdIsNull()
                .forEach(s->
                        {
                            Consumer<? super DownloadInfo> mergeAction = downloadInfo -> {
                                try {
                                    merge(s,ffmpeg.toString());
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            };

                            s.downloadInfos().forEach(d->{
                             /*   if (!Files.exists(Paths.get(d.target()))) {
                                    d.addTotal(-d.total());
                                    d.addCurrent(-d.current());
                                }*/
                                if (d.current() < d.total() || d.total() == 0  ) {
                                    try {
                                        DownloadUtils.handleUrl(HttpClientUtils.youtubeClient(),s,d,downloadListener,mergeAction);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    s.countIncrement();
                                }
                            });
                            if (s.count() == s.downloadInfos().size() && !Files.exists(Paths.get(s.target()))) {
                                mergeAction.accept(s);
                            }


                        }
                );
    }
    private  static void merge(MappperDownloadInfo<MappperDownloadInfo> s,String string) throws Exception {
        List<MappperDownloadInfo> downloadInfos = s.downloadInfos();
        String target = s.target();
        List<String> param = new ArrayList<>();
        param.addAll(Arrays.asList(string, "-y"));
        List<String> params = downloadInfos.stream()
                .flatMap(downloadInfo -> {
                    String target1 = downloadInfo.target();
                    return Arrays.asList("-i", "\"" + target1
                            + "\"").stream();
                }).collect(Collectors.toList());
        param.addAll(params);
        param.add("\"" + target + "\"");
        ProcessBuilderUtils.executeAndDiscardOuput(param.toArray(new String[0]));
        downloadInfos.stream().forEach(t-> {
            try {
                Files.delete(Paths.get(t.target()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
