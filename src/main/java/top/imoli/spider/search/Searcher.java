package top.imoli.spider.search;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.imoli.spider.entity.Result;
import top.imoli.spider.entity.Search;

/**
 * @author moli@hulai.com
 * @date 2022/2/14 3:06 PM
 */
public interface Searcher {

    void search(Search search);

    default void rule0(Search search, Elements select, SearchType type) {
        if (select.size() >= 2) {
            Element e0 = select.get(0);
            String href = e0.select("a").attr("abs:href");
            String bookName = e0.text();
            search.addResult(new Result(href, bookName, select.get(1).text(), type));
        }
    }

}
