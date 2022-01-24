package top.imoli.spider.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import top.imoli.spider.Book;
import top.imoli.spider.Chapter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author moli@hulai.com
 * @date 2022/1/24 5:54 PM
 */
public class BookTask implements Runnable {

    private static final String SAVE_PATH = "/home/moli/文档/novel/%s-%s.txt";


    private final Book book;

    public BookTask(Book book) {
        this.book = book;
    }

    @Override
    public void run() {
        String bookUrl = book.getUrl();
        ExecutorService pool = null;
        try {
            int processors = Runtime.getRuntime().availableProcessors();
            Document doc = Jsoup.connect(bookUrl).get();
            String name = Objects.requireNonNull(doc.selectFirst("#info > h1")).text();
            String author = Objects.requireNonNull(doc.selectFirst("#info > p > a")).text();
            book.setName(name);
            book.setAuthor(author);
            for (Element element : doc.select(".mulu_list > li > a")) {
                book.addList(new Chapter(element.text(), bookUrl + element.attr("href")));
            }
            int total = book.getList().size();
            CountDownLatch downLatch = new CountDownLatch(total);
            pool = Executors.newFixedThreadPool(processors * 2);
            for (Chapter chapter : book.getList()) {
                pool.execute(new ChapterTask(chapter, downLatch, total));
            }
            downLatch.await();
            System.out.println("爬取完成啦");
            String filePath = String.format(SAVE_PATH, book.getName(), book.getAuthor());
            saveAsFileWriter(filePath, book.toText());
            System.out.println("写入完成!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pool != null) {
                pool.shutdown();
            }
        }
    }

    private static void saveAsFileWriter(String filePath, String content) {
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
    }
}
