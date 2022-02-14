package top.imoli.spider.search.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import top.imoli.spider.entity.Result;
import top.imoli.spider.entity.Search;
import top.imoli.spider.search.AbstractSearcher;
import top.imoli.spider.search.SearchType;
import top.imoli.spider.task.TryObtain;

/**
 * @author moli@hulai.com
 * @date 2022/2/14 3:09 PM
 */
public class Bige7Searcher extends AbstractSearcher {
    /**
     * https://www.bige7.com/s?q=轮回乐园
     * 可以不编码,唯一结果不会跳转
     */
    public Bige7Searcher(SearchType type, String path) {
        super(type, path);
    }

    @Override
    public void search(Search search) {
        try {
            Document document = TryObtain.tryGet(Jsoup.connect(path).data("q", search.getKeyWord()));
            for (Element element : document.select(".bookinfo")) {
                String bookName = element.select(".bookname").text();
                String href = splitJoint(element.select(".bookname > a").attr("href"));
                String author = element.select(".author").text().replace("作者：", "");
                search.addResult(new Result(href, bookName, author, type));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
