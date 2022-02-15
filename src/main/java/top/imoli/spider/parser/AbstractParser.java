package top.imoli.spider.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.imoli.spider.entity.Book;
import top.imoli.spider.entity.Chapter;
import top.imoli.spider.exception.ParserException;

/**
 * @author moli@hulai.com
 * @date 2022/1/26 10:36 AM
 */
public abstract class AbstractParser implements Parser {

    @Override
    public void bookParser(Document doc, Book book) {
        try {
            book.setName(nameParser(doc));
            book.setAuthor(authorParser(doc));
            for (Element element : chapterList(doc)) {
                String href = element.attr("abs:href");
                book.addList(new Chapter(element.text(), href));
            }
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    public abstract String nameParser(Document doc);

    public abstract String authorParser(Document doc);

    public abstract Elements chapterList(Document doc);

}
