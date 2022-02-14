package top.imoli.spider.task;

import top.imoli.spider.entity.Result;
import top.imoli.spider.entity.Search;
import top.imoli.spider.search.SearchType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author moli@hulai.com
 * @date 2022/2/11 22:24 PM
 */
public class SearchTask implements Runnable {


    private final Search search;

    public SearchTask(String keyWord) {
        this(new Search(keyWord));
    }

    public SearchTask(Search search) {
        this.search = search;
    }

    @Override
    public void run() {
        SearchType.search(search);
    }

    public Search getSearch() {
        return search;
    }

    public static void main(String[] args) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long startTime = System.currentTimeMillis();
        System.out.println("笔趣阁搜索下载程序已启动,开始时间: " + format.format(new Date(startTime)));
        startup();
        long endTime = System.currentTimeMillis();
        System.out.println("所有任务处理完成,结束时间: " + format.format(new Date(endTime)) + "\t总耗时: " + (endTime - startTime) / 1000 + "s");
    }

    private static void startup() {
        Scanner scanner = new Scanner(System.in);
        SearchTask task = new SearchTask(scanner.next());
        task.run();
        Search search = task.getSearch();
        System.out.println("任务 " + search.getKeyWord() + " 搜索到一下结果:\n请选择下载index(逗号分隔)");
        List<Result> results = search.getList();
        for (int i = 0; i < results.size(); i++) {
            System.out.println(i + "\t" + results.get(i));
        }
        //String next = scanner.next();
        //String[] split = next.split(",");
//        for (String s : split) {
//            Result result = results.get(Integer.parseInt(s));
//            BookTask bookTask = new BookTask(result.getUrl());
//            bookTask.call();
//        }
        results.forEach(result -> new BookTask(result.getUrl()).call());
        System.out.println("是否结束？(Y/N)");
        if (Objects.equals("Y", scanner.next())) {
            return;
        }
        startup();
    }
}
