package top.imoli.spider.search.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import top.imoli.spider.entity.Result;
import top.imoli.spider.entity.Search;
import top.imoli.spider.search.AbstractSearcher;
import top.imoli.spider.search.SearchType;
import top.imoli.spider.task.TryObtain;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author moli@hulai.com
 * @date 2022/1/26 9:07 AM
 */
public class BiqukanSearcher extends AbstractSearcher {

    /**
     * https://www.biqukan.cc/modules/article/search.php?searchkey=轮回乐园&submit=搜索
     * https://www.biqukan.cc/modules/article/search.php?searchkey=%C2%D6%BB%D8%C0%D6%D4%B0&submit=%CB%D1%CB%F7
     * GBK编码,唯一结果会重定向到目标
     */
    public BiqukanSearcher(SearchType type, String path) {
        super(type, path);
    }

    @Override
    public void search(Search search) {
        try {
            Document document = TryObtain.tryGet(Jsoup.connect(path)
                    .data("searchkey", encode(search.getKeyWord()))
                    .data("submit", "%CB%D1%CB%F7"));
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

    private String encode(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "GBK");
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        //将application/x-www-form-urlencoded字符串转换成普通字符串
        //采用UTF-8字符集进行解码
        System.out.println(URLDecoder.decode("%C2%D6%BB%D8%C0%D6%D4%B0", "UTF-8"));
        //采用GBK字符集进行解码
        System.out.println(URLDecoder.decode("%C2%D6%BB%D8%C0%D6%D4%B0", "GBK"));
        // 将普通字符串转换成application/x-www-form-urlencoded字符串
        //采用utf-8字符集
        System.out.println(URLEncoder.encode("轮回乐园", "UTF-8"));
        //采用GBK字符集
        System.out.println(URLEncoder.encode("轮回乐园", "GBK"));
        System.out.println(URLEncoder.encode("大主宰", "GBK"));
    }
}
