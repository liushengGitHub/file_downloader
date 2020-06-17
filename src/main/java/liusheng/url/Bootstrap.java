package liusheng.url;

import liusheng.url.pipeline.Handler;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 * 2020年:  04 月:  13 日:  10小时:  42分钟:
 * 用户名: 👨‍LiuSheng👨‍
 */

public class Bootstrap<T> {

    private DefaultExecutorLoopGroup defaultExecutorLoopGroup;

    private Target<T> target;

    public Bootstrap() {
        defaultExecutorLoopGroup = new DefaultExecutorLoopGroup();
    }

    public  void handle(Handler handler) {

    }

   /* class BootstrapTarget implements Target<T> {

        public void add(T t) {
            ExecutorLoop executorLoop = defaultExecutorLoopGroup.next();
            //executorLoop.execute();
            executorLoop.register();
        }


    }*/

    public  void register(Source<T> source) {
        defaultExecutorLoopGroup.next().register(source);
    }
    public  void  start(){

    }

    public static void main(String[] args) {
        Bootstrap<String> bootstrap = new Bootstrap<>();

        LinkedList<String> list = new LinkedList<>();

        bootstrap.register(new QueueSource<>(list));
        Scanner scanner = new Scanner(System.in);
        while (true) {

            String line = scanner.nextLine();

            System.out.println(line);
        }

    }


}
