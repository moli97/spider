package top.imoli.spider.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import top.imoli.spider.Chapter;
import top.imoli.spider.Discard;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @author moli@hulai.com
 * @date 2022/1/24 6:03 PM
 */
public class ChapterTask implements Runnable {


    private final Chapter chapter;
    private final CountDownLatch downLatch;
    private final int total;

    public ChapterTask(Chapter chapter, CountDownLatch downLatch, int total) {
        this.chapter = chapter;
        this.downLatch = downLatch;
        this.total = total;
    }

    @Override
    public void run() {
        try {
            Document doc = Jsoup.connect(this.chapter.getUrl()).get();
            String text = Objects.requireNonNull(doc.selectFirst("#htmlContent")).text();
            this.chapter.setText(Discard.handle(text));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            print(downLatch.getCount());
            downLatch.countDown();
        }
    }

    private void print(long count) {
        if (count % 100 == 0) {
            System.out.print("\n" + String.format("进度: %s ", (total - count) * 100 / total) + "%");
        }
        System.out.print("#");
    }
}
