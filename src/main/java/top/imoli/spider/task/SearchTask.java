package top.imoli.spider.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import top.imoli.spider.config.SpiderConfig;
import top.imoli.spider.entity.Result;
import top.imoli.spider.entity.Search;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author moli@hulai.com
 * @date 2022/2/11 22:24 PM
 */
public class SearchTask implements Runnable {


    private final Search search;

    public SearchTask(String keyWord) {
        this(new Search(keyWord));
    }

    public SearchTask(Search search) {
        this.search = search;
    }

    @Override
    public void run() {
        try {
            Document post = Jsoup.connect("https://www.xbiquge.la/modules/article/waps.php")
                    .data("searchkey", search.getKeyWord())
                    .post();
            for (Element element : post.select(".even > a")) {
                String href = element.attr("href");
                String bookName = element.text();
                search.addResult(new Result(href, bookName));
            }
            System.out.println(search);
            search.getResults().stream().map(result -> new BookTask(result.getUrl())).forEach(SpiderConfig.getPool()::execute);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SearchTask("诸天世界唯一玩家").run();
    }
}
