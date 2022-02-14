package top.imoli.spider.search;

import top.imoli.spider.entity.Search;
import top.imoli.spider.search.impl.Bige7Searcher;
import top.imoli.spider.search.impl.XBiqugeSearcher;

import java.util.function.BiFunction;

/**
 * @author moli@hulai.com
 * @date 2022/2/14 3:04 PM
 */
public enum SearchType {

    BI_GE_7("https://www.bige7.com/s", Bige7Searcher::new),
    X_BI_QU_GE("https://www.xbiquge.la/modules/article/waps.php", XBiqugeSearcher::new),
//    X("test", (type, path) -> new AbstractSearcher(type, path) {
//        @Override
//        public void search(Search search) {
//
//        }
//    })
    ;
    public final String path;
    public final Searcher searcher;

    SearchType(String path, BiFunction<SearchType, String, Searcher> factory) {
        this.path = path;
        this.searcher = factory.apply(this, this.path);
    }

    public static void search(Search search) {
        for (SearchType value : values()) {
            value.searcher.search(search);
        }
    }
}
