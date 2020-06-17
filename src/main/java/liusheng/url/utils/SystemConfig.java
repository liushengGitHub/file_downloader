package liusheng.url.utils;

/**
 * 年: 2020  月: 03 日: 22 小时: 16 分钟: 35
 * 用户名: LiuSheng
 */



import liusheng.url.main.constans.SystemConstants;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/***
 * 从配置文件中获取值 ,如果不存在,那么从系统变量中获取
 */
public class SystemConfig {
    private static  Properties configProperties = new Properties();
    static {
        try {
            Path configPath = Paths.get(SystemConstants.CONFIG_DIR_PATH);
            /**
             * 如果 路径不存在 ,那么就创建 ,存在就进行取值
             */
            if (!Files.exists(configPath)) {
                    Files.createDirectories(configPath);
            } else {
                /**
                 * 获取系统变量
                 */
                Properties systemProperties = System.getProperties();


                /**
                 * 放入系统变量
                 */
                configProperties.putAll(systemProperties);
                /**
                 * 加载 proeperties
                 */
                configProperties.load(Files.newInputStream(configPath.resolve(SystemConstants.CONFIG_FILE_NAME)));


            }
        }catch (Throwable throwable ) {
            throwable.printStackTrace();
        }
    }


    /**
     *  获取key 对应的value ,如果没有则使用默认值
     * @param name
     * @param defaultValue
     * @return
     */
    public static  String getPropertyOrDefault(String  name ,String defaultValue) {
        return  configProperties.getProperty(name,defaultValue);
    }

    /**
     *  获取key 对应的value ,如果没有则返回null
     * @param name
     * @return
     */
    public static  String getProperty(String  name ) {
        return  configProperties.getProperty(name);
    }


}
