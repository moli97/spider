package top.imoli.spider.search.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.imoli.spider.entity.Result;
import top.imoli.spider.entity.Search;
import top.imoli.spider.search.AbstractSearcher;
import top.imoli.spider.search.SearchType;

import java.io.IOException;

/**
 * @author moli@hulai.com
 * @date 2022/2/14 3:09 PM
 */
public class XBiqugeSearcher extends AbstractSearcher {

    public XBiqugeSearcher(SearchType type, String path) {
        super(type, path);
    }

    @Override
    public void search(Search search) {
        for (int i = 0; i < TRY_COUNT; i++) {
            try {
                Document post = Jsoup.connect(path)
                        .data("searchkey", search.getKeyWord())
                        .post();
                for (Element element : post.select("tbody > tr:not([align])")) {
                    Elements select = element.select(".even");
                    if (select.size() >= 2) {
                        Element e0 = select.get(0);
                        String href = e0.attr("href");
                        String bookName = e0.text();
                        search.addResult(new Result(href, bookName, select.get(1).text(), type));
                    }
                }
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
