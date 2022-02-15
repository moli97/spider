package top.imoli.spider.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import top.imoli.spider.entity.Book;
import top.imoli.spider.entity.Chapter;

/**
 * @author moli@hulai.com
 * @date 2022/1/26 9:02 AM
 */
public interface Parser {
    void bookParser(Document doc, Book book);

    void chapterParser(Document doc, Chapter chapter);

    default String getText(Element parentElement) {
        return getText(parentElement, "");
    }

    default String getText(Element parentElement, String prefix) {
        StringBuilder working = new StringBuilder(prefix);
        for (Node child : parentElement.childNodes()) {
            if (child instanceof TextNode) {
                working.append(((TextNode) child).text());
            }
            if (child instanceof Element) {
                Element childElement = (Element) child;
                if (childElement.tag().getName().equalsIgnoreCase("br")) {
                    working.append("\n").append(prefix);
                }
                working.append(getText(childElement, prefix));
            }
        }
        return working.toString();
    }

}
