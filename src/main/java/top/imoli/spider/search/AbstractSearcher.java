package top.imoli.spider.search;

import top.imoli.spider.util.URLUtil;

/**
 * @author moli@hulai.com
 * @date 2022/1/26 10:36 AM
 */
public abstract class AbstractSearcher implements Searcher {

    protected final SearchType type;
    protected final String path;
    protected final String baseUrl;

    public AbstractSearcher(SearchType type, String path) {
        this.type = type;
        this.path = path;
        this.baseUrl = URLUtil.getBaseUrl(this.path);
    }

    public String splitJoint(String href) {
        return splitJoint(baseUrl, href);
    }
}
