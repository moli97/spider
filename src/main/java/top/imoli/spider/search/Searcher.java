package top.imoli.spider.search;

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

}
