package top.imoli.spider.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import top.imoli.spider.Book;
import top.imoli.spider.Chapter;
import top.imoli.spider.SpiderConfig;
import top.imoli.util.ProgressUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author moli@hulai.com
 * @date 2022/1/24 5:54 PM
 */
public class BookTask implements Runnable {


    private final String bookUrl;
    private final Book book;

    public BookTask(String bookUrl) {
        this.bookUrl = bookUrl;
        this.book = new Book(bookUrl);
    }

    @Override
    public void run() {
        call();
    }

    public Book call() {
        try {
            System.out.println("正在解析url: " + bookUrl);
            Document doc = Jsoup.connect(bookUrl).get();
            String name = Objects.requireNonNull(doc.selectFirst("#info > h1")).text();
            String author = Objects.requireNonNull(doc.selectFirst("#info > p > a")).text();
            this.book.setName(name);
            this.book.setAuthor(author);
            System.out.println("小说名: 《" + name + "》\t 作者: " + author);
            for (Element element : doc.select(".mulu_list > li > a")) {
                this.book.addList(new Chapter(element.text(), bookUrl + element.attr("href")));
            }
            int total = this.book.getList().size();
            CountDownLatch downLatch = new CountDownLatch(total);
            Executor pool = SpiderConfig.getPool();
            AtomicInteger progress = new AtomicInteger();
            ProgressUtil.print(name, "下载中: ", total, progress);
            for (Chapter chapter : this.book.getList()) {
                pool.execute(new ChapterTask(chapter, downLatch, progress));
            }
            downLatch.await();
            Thread.sleep(200);
            System.out.println("小说名: 《" + name + "》" + "爬取完成");
            String filePath = String.format(SpiderConfig.getSaveFormat(), this.book.getName(), this.book.getAuthor());
            saveAsFileWriter(filePath, this.book.toText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }

    private static void saveAsFileWriter(String filePath, String content) {
        System.out.println("开始写入文件");
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath);
            writer.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                assert writer != null;
                writer.flush();
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("写入完成! 文件路径: " + filePath);
    }
}
