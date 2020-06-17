package liusheng.url.bilibili;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 年: 2020  月: 04 日: 09 小时: 13 分钟: 41
 * 用户名: LiuSheng
 */

public class Main1 {
    public static void main(String[] args) throws IOException {
        String file = "F:\\NodeProject\\file_downloader\\src\\main\\java\\liusheng\\url\\bilibili\\2.txt";

        Double reduce = Files.lines(Paths.get(file))
                .map(l -> {


                    String[] split = l.split("[\t ]+");
                    double v = Double.parseDouble(split[5]);
                    if (v > 10) {
                        System.out.println(l);
                    }
                        if (Double.parseDouble( split[4]) >= 60) {
                            return v;
                        }else {
                            return  0.0;
                        }

                }).reduce(0.0, Double::sum, Double::sum);

        System.out.println(reduce + 26);
    }
}
