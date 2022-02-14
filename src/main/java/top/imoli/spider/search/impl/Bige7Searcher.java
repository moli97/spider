package top.imoli.spider.search.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import top.imoli.spider.entity.Result;
import top.imoli.spider.entity.Search;
import top.imoli.spider.search.AbstractSearcher;
import top.imoli.spider.search.SearchType;

import java.io.IOException;

/**
 * @author moli@hulai.com
 * @date 2022/2/14 3:09 PM
 */
public class Bige7Searcher extends AbstractSearcher {

    public Bige7Searcher(SearchType type, String path) {
        super(type, path);
    }

    @Override
    public void search(Search search) {
        for (int i = 0; i < TRY_COUNT; i++) {
            try {
                Document post = Jsoup.connect(path)
                        .data("q", search.getKeyWord())
                        .get();
                for (Element element : post.select(".bookinfo")) {
                    String bookName = element.select(".bookname").text();
                    String href = splitJoint(element.select(".bookname > a").attr("href"));
                    String author = element.select(".author").text().replace("作者：", "");
                    search.addResult(new Result(href, bookName, author, type));
                }
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
