package top.imoli.spider.rule.entity;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @author moli@hulai.com
 * @date 2022/2/15 9:37 AM
 */
public abstract class Parser<T> implements IParser<T> {
    //解析链


}

interface IParser<T> {
    T parser(Document document);
}

class TextParser extends Parser<String>{

    @Override
    public String parser(Document document) {
        return null;
    }
}

class ElementsParser extends Parser<Elements>{

    @Override
    public Elements parser(Document document) {
        return null;
    }
}
