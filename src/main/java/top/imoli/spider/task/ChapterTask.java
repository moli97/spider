package top.imoli.spider.task;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import top.imoli.spider.config.SpiderConfig;
import top.imoli.spider.entity.Chapter;
import top.imoli.spider.parser.Parser;

import java.net.SocketTimeoutException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author moli@hulai.com
 * @date 2022/1/24 6:03 PM
 */
public class ChapterTask implements Runnable {

    private final Chapter chapter;
    private final CountDownLatch downLatch;
    private final AtomicInteger progress;
    private final Parser parser;

    public ChapterTask(Chapter chapter, CountDownLatch downLatch, AtomicInteger progress, Parser parser) {
        this.chapter = chapter;
        this.downLatch = downLatch;
        this.progress = progress;
        this.parser = parser;
    }

    @Override
    public void run() {
        try {
            parser();
        } finally {
            progress.incrementAndGet();
            downLatch.countDown();
        }
    }

    private static final Random random = new Random();

    private void parser() {
        try {
            Document doc = TryObtain.tryGet(Jsoup.connect(chapter.getUrl()));
            parser.chapterParser(doc, chapter);
        } catch (HttpStatusException e) {
            if (SpiderConfig.isExist(chapter.getName())) {
                chapter.setText("");
            } else {
                System.out.println(chapter.getName() + e.getMessage());
                chapter.setText("章节错误");
            }
        } catch (SocketTimeoutException e) {
            chapter.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
