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
 * @date 2022/2/11 20:44 AM
 */
public class XBiqugeParser extends AbstractParser {

    @Override
    public String nameParser(Document doc) {
        return Objects.requireNonNull(doc.selectFirst("#info > h1")).text();
    }

    @Override
    public String authorParser(Document doc) {
        return Objects.requireNonNull(doc.selectFirst("#info > p"))
                .text()
                .replaceAll(" ","")
                .replace("作者：", "");
    }

    @Override
    public Elements chapterList(Document doc) {
        return doc.select("#list > dl > dd > a");
    }

    @Override
    public void chapterParser(Document doc, Chapter chapter) {
        Element element = Objects.requireNonNull(doc.selectFirst("#content"));
        chapter.setText(Replace.handle(getText(element, " ")));
    }
}
