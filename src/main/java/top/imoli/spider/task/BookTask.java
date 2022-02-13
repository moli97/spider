package top.imoli.spider.task;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import top.imoli.spider.config.Format;
import top.imoli.spider.config.SpiderConfig;
import top.imoli.spider.entity.Book;
import top.imoli.spider.entity.Chapter;
import top.imoli.spider.exception.ParserException;
import top.imoli.spider.parser.Parser;
import top.imoli.spider.parser.ParserType;
import top.imoli.spider.util.ProgressUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
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
            Parser parser = ParserType.getParser(bookUrl);
            parser.bookParser(doc, book);
            String bookName = book.getName();
            System.out.println("小说名: 《" + bookName + "》\t 作者: " + book.getAuthor());
            int total = book.getList().size();
            CountDownLatch downLatch = new CountDownLatch(total);
            Executor pool = SpiderConfig.getPool();
            AtomicInteger progress = new AtomicInteger();
            ProgressUtil.print(bookName, "下载中: ", total, progress);
            for (Chapter chapter : book.getList()) {
                pool.execute(new ChapterTask(chapter, downLatch, progress, parser));
            }
            downLatch.await();
            Thread.sleep(200);
            System.out.println("小说名: 《" + bookName + "》" + "爬取完成");
            saveAsFileWriter(Format.format(bookName, book.getAuthor(), bookUrl), book.toText());
        } catch (ParserException | HttpStatusException e) {
            System.out.println(e.getMessage());
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
