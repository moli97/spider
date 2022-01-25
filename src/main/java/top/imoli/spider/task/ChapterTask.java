package top.imoli.spider.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import top.imoli.spider.Chapter;
import top.imoli.spider.Replace;

import java.util.Objects;
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

    public ChapterTask(Chapter chapter, CountDownLatch downLatch,  AtomicInteger progress) {
        this.chapter = chapter;
        this.downLatch = downLatch;
        this.progress = progress;
    }

    @Override
    public void run() {
        try {
            Document doc = Jsoup.connect(this.chapter.getUrl()).get();
            Element element = Objects.requireNonNull(doc.selectFirst("#htmlContent"));
            String text = getText(element);
            this.chapter.setText(Replace.handle(text));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            progress.incrementAndGet();
            downLatch.countDown();
        }
    }

    private String getText(Element parentElement) {
        StringBuilder working = new StringBuilder("  ");
        for (Node child : parentElement.childNodes()) {
            if (child instanceof TextNode) {
                working.append(((TextNode) child).text());
            }
            if (child instanceof Element) {
                Element childElement = (Element) child;
                if (childElement.tag().getName().equalsIgnoreCase("br")) {
                    working.append("\n  ");
                }
                working.append(getText(childElement));
            }
        }
        return working.toString();
    }
}
