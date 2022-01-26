package top.imoli.spider;


import top.imoli.spider.config.SpiderConfig;
import top.imoli.spider.entity.Book;
import top.imoli.spider.task.BookTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author moli@hulai.com
 * @date 2022/1/24 5:12 PM
 */
public class Bootstrap {

    public static void main(String[] args) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long startTime = System.currentTimeMillis();
        System.out.println("笔趣阁下载程序已启动,开始时间: " + format.format(new Date(startTime)));
        startup();
        long endTime = System.currentTimeMillis();
        System.out.println("所有任务处理完成,结束时间: " + format.format(new Date(endTime)) + "\t总耗时: " + (endTime - startTime) / 1000 + "s");
    }

    public static void startup() {
        boolean async = SpiderConfig.isAsync();
        if (async) {
            ExecutorService pool = SpiderConfig.getPool();
            List<Future<Book>> futures = SpiderConfig.getTargets()
                    .stream()
                    .map(url -> pool.submit(() -> new BookTask(url).call()))
                    .collect(Collectors.toList());
            for (Future<Book> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (String target : SpiderConfig.getTargets()) {
                new BookTask(target).call();
            }
        }
        SpiderConfig.shutdown();
    }
}
