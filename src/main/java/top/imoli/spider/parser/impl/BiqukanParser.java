package top.imoli.spider.parser.impl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.imoli.spider.config.Replace;
import top.imoli.spider.entity.Chapter;
import top.imoli.spider.parser.AbstractParser;

import java.util.Objects;

/**
 * @author moli@hulai.com
 * @date 2022/1/26 9:07 AM
 */
public class BiqukanParser extends AbstractParser {

    @Override
    public String nameParser(Document doc) {
        return Objects.requireNonNull(doc.selectFirst("#info > h1")).text();
    }

    @Override
    public String authorParser(Document doc) {
        return Objects.requireNonNull(doc.selectFirst("#info > p > a")).text();
    }

    @Override
    public Elements chapterList(Document doc) {
        return doc.select(".mulu_list > li > a");
    }

    @Override
    public void chapterParser(Document doc, Chapter chapter) {
        Element element = Objects.requireNonNull(doc.selectFirst("#htmlContent"));
        chapter.setText(Replace.handle(getText(element, " ")));
    }
}
