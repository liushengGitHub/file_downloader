package liusheng.url.utils;

import liusheng.url.main.constans.SystemConstants;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 年: 2020  月: 04 日: 05 小时: 13 分钟: 39
 * 用户名: LiuSheng
 */

public class HttpClientUtils {
    public static HttpClient bilibiliHttpClient() {
        return HttpClients
                .custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(60000)
                        .setSocketTimeout(60000)
                        .setConnectionRequestTimeout(60000)
                        .build())
                .setDefaultHeaders(Arrays.asList(
                        new BasicHeader(HttpHeaders.REFERER
                        ,"https://www.bilibili.com")))
                .build();
    }

    private static HttpClient httpClient;
    private static CookieStore cookieStore = new BasicCookieStore();
    private static List<Header> defaultHeader = new LinkedList<>();

    public static HttpClient youtubeClient() {
        return httpClient;
    }

    static {
        try {
            /**
             * 创建cookieStore
             */

            /**
             * 从配置文件中加载cookie的数据
             */
            loadCookies(cookieStore);

            /**
             * 加载默认请求头
             */
            loadHeaders(defaultHeader);

            /**
             * 创建httpClient
             */
            httpClient = HttpClients.custom()
                    .setDefaultCookieStore(cookieStore)
                    .setDefaultHeaders(defaultHeader)
                    //.setDefaultRequestConfig()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setSocketTimeout(120000)
                            .setConnectTimeout(120000)
                            .setConnectionRequestTimeout(120000)
                            .build())
                    .build();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * 从配置文件中加载默认请求头
     * @param defaultHeader
     */

    private static void loadHeaders(List<Header> defaultHeader) {
        String headerFile = SystemConfig.getPropertyOrDefault("headerFile", SystemConstants.DEFAULT_HEADER_NAME);

        try {
            Path path = Paths.get(headerFile);
            if (!Files.exists(path)) return;;
            Files.lines(path)
                    .forEach(line->{
                        String[] kvs = line.split("[=:]",2);
                        if (kvs.length == 2) {
                            defaultHeader.add(
                                    new BasicHeader(kvs[0].trim(),kvs[1].trim())
                            );
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件中加载cookie
     * @param cookieStore
     */

    private static void loadCookies(CookieStore cookieStore) {
        String cookieFile = SystemConfig.getPropertyOrDefault("cookieFile", SystemConstants.COOKIE_FILE_NAME);

        try {
            Path path = Paths.get(cookieFile);
            if (!Files.exists(path)) return;;
            Files.lines(path)
                    .forEach(line->{
                        String[] kvs = line.split("[=:]",2);
                        if (kvs.length == 2) {
                            cookieStore.addCookie(
                                    new BasicClientCookie(kvs[0].trim(),kvs[1].trim())
                            );
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
