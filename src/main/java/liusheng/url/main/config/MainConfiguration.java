package liusheng.url.main.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.gson.Gson;
import liusheng.url.bilibili.*;
import liusheng.url.main.MappperDownloadInfo;
import liusheng.url.main.SqliteLockInterceptor;
import liusheng.url.main.controller.MapperDownloadListener;
import liusheng.url.main.mapper.DownloadInfoMapper;
import liusheng.url.main.service.DownloadInfoService;
import liusheng.url.main.service.impl.DownloadInfoServiceImpl;
import liusheng.url.pipeline.*;
import liusheng.url.utils.ApplicationContextUtils;
import liusheng.url.utils.DownloadUtils;
import liusheng.url.utils.HttpClientUtils;
import liusheng.url.utils.ProcessBuilderUtils;
import liusheng.url.youtube.YoutubeVideoDownloadHandler;
import liusheng.url.youtube.YoutubeVideoInfoHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.bytedeco.ffmpeg.ffmpeg;
import org.bytedeco.javacpp.Loader;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.PropertyPlaceholderHelper;

import javax.jnlp.DownloadService;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 年: 2020  月: 04 日: 06 小时: 10 分钟: 47
 * 用户名: LiuSheng
 */
@Configuration
/*@PropertySource("filesystem:config/config.properties")*/
public class MainConfiguration implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    public ApplicationContextUtils applicationContextUtils() {
        return new ApplicationContextUtils();
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(ApplicationContext applicationContext) {
        PropertySourcesPlaceholderConfigurer placeholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        placeholderConfigurer.setLocations(new FileSystemResource("config/config.properties"));
        return placeholderConfigurer;
    }

    @DependsOn("propertySourcesPlaceholderConfigurer")
    @Bean
    public DataSource dataSource(@Value("${jdbc.url}") String url, @Value("${jdbc.driverClass}") String className) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:sqlite:data/data.db");
        druidDataSource.setDriverClassName("org.sqlite.JDBC");
        return druidDataSource;
    }



    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource, ApplicationContext applicationContext) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setTypeAliasesPackage("liusheng.url.main.bean");
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath*:mapper/*.xml"));
        return sqlSessionFactoryBean;
    }

    @Bean
    public SqliteLockInterceptor sqliteLockInterceptor(SqlSessionFactory sqlSessionFactory) {
        SqliteLockInterceptor interceptor = new SqliteLockInterceptor();
        sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
        return interceptor;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
        mapperScannerConfigurer.setBasePackage("liusheng.url.main.mapper");
        return mapperScannerConfigurer;
    }

    @Bean
    public Pipeline bilibiliPipeline() {
        DefaultPipeline pipeline = new DefaultPipeline();
        pipeline.use("https://www.bilibili.com", bilibiliIdHandler());
        pipeline.use("https://www.bilibili.com", queryParamHandler());
        pipeline.use("https://www.bilibili.com", videoInfoParser());
        pipeline.use("https://www.bilibili.com", videoPParser());
        pipeline.use("https://www.youtube.com", youtubeVideoInfoHandler());
        pipeline.use("https://www.youtube.com", youtubeVideoDownloadHandler());
        return pipeline;
    }

    private Handler youtubeVideoDownloadHandler() {
        YoutubeVideoDownloadHandler downloadHandler = new YoutubeVideoDownloadHandler();
        downloadHandler.setFfmpeg(ffmpeg());
        downloadHandler.setDownloadInfoDownloadListener(downloadListener());
        return downloadHandler;
    }

    private Handler youtubeVideoInfoHandler() {
        return new YoutubeVideoInfoHandler();
    }

    /* @Bean
     public Pipeline youtubePipeline() {
         DefaultPipeline pipeline = new DefaultPipeline();
         pipeline.use(bilibiliIdHandler());
         pipeline.use(queryParamHandler());
         pipeline.use(videoInfoParser());
         pipeline.use(videoPParser());
         return pipeline;
     }*/
    @Bean
    public void proxy() {

    }

    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean
    public VideoInfoParser videoInfoParser() {
        VideoInfoParser videoInfoParser = new VideoInfoParser();
        videoInfoParser.setGson(gson());
        return videoInfoParser;
    }

    @DependsOn("downloadInfoMapper")
    @Bean()
    public DownloadListener downloadListener() {
        MapperDownloadListener mapperDownloadListener = new MapperDownloadListener();
        mapperDownloadListener.setDownloadInfoService(downloadService());


        return mapperDownloadListener;
    }




    @Bean
    public DownloadInfoService downloadService() {

        DownloadInfoServiceImpl downloadInfoService = new DownloadInfoServiceImpl();

        downloadInfoService.setDownloadInfoMapper(applicationContext.getBean(DownloadInfoMapper.class));
        return downloadInfoService;
    }


    @Bean
    public VideoPParser videoPParser() {
        VideoPParser videoPParser = new VideoPParser();
        videoPParser.setGson(gson());
        videoPParser.setDownloadListener(downloadListener());
        videoPParser.setNewDownloader(videoPNewDownloader());
        videoPParser.setOldDownloader(videoPOldDownloader());
        return videoPParser;
    }

    @Bean
    public BilibiliIdHandler bilibiliIdHandler() {
        return new BilibiliIdHandler();
    }

    @Bean
    public QueryParamHandler queryParamHandler() {
        return new QueryParamHandler();
    }

    @Bean
    public Downloader<VideoPNew> videoPNewDownloader() {
        VideoPNewDownloader videoPNewDownloader = new VideoPNewDownloader();
        videoPNewDownloader.setFfmpeg(ffmpeg());
        return videoPNewDownloader;
    }

    @Bean
    public Downloader<VideoPOld> videoPOldDownloader() {
        VideoPOldDownloader videoPOldDownloader = new VideoPOldDownloader();
        videoPOldDownloader.setFfmpeg(ffmpeg());
        return videoPOldDownloader;
    }

    @Bean
    public String ffmpeg() {
        return Loader.load(ffmpeg.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
