package top.imoli.spider.search.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.imoli.spider.entity.Result;
import top.imoli.spider.entity.Search;
import top.imoli.spider.search.AbstractSearcher;
import top.imoli.spider.search.SearchType;
import top.imoli.spider.task.TryObtain;

/**
 * @author moli@hulai.com
 * @date 2022/2/14 3:09 PM
 */
public class XBiqugeSearcher extends AbstractSearcher {
    /**
     * https://www.xbiquge.la/modules/article/waps.php
     * POST请求 searchkey: 诸天世界唯一玩家
     */
    public XBiqugeSearcher(SearchType type, String path) {
        super(type, path);
    }

    @Override
    public void search(Search search) {
        try {
            Document document = TryObtain.tryPost(Jsoup.connect(path).data("searchkey", search.getKeyWord()));
            for (Element element : document.select("tbody > tr:not([align])")) {
                Elements select = element.select(".even");
                rule0(search, select, baseUrl, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
