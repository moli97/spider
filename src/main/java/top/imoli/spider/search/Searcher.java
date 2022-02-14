package top.imoli.spider.search;

import top.imoli.spider.entity.Search;

/**
 * @author moli@hulai.com
 * @date 2022/2/14 3:06 PM
 */
public interface Searcher {

    int TRY_COUNT = 5;

    void search(Search search);

}
