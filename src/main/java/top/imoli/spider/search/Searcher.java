package top.imoli.spider.search;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.imoli.spider.entity.Result;
import top.imoli.spider.entity.Search;
import top.imoli.spider.util.URLUtil;

/**
 * @author moli@hulai.com
 * @date 2022/2/14 3:06 PM
 */
public interface Searcher {

    void search(Search search);

    default String splitJoint(String baseUrl, String href) {
        if (href.startsWith("/")) {
            return URLUtil.format(baseUrl + href);
        }
        return href;
    }

    default void rule0(Search search, Elements select, String baseUrl, SearchType type) {
        if (select.size() >= 2) {
            Element e0 = select.get(0);
            String href = splitJoint(baseUrl, e0.select("a").attr("href"));
            String bookName = e0.text();
            search.addResult(new Result(href, bookName, select.get(1).text(), type));
        }
    }

}
