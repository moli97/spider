package top.imoli.spider.search.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.imoli.spider.entity.Result;
import top.imoli.spider.entity.Search;
import top.imoli.spider.parser.AbstractParser;
import top.imoli.spider.parser.Parser;
import top.imoli.spider.parser.ParserType;
import top.imoli.spider.search.AbstractSearcher;
import top.imoli.spider.search.SearchType;
import top.imoli.spider.task.TryObtain;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Objects;

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
            Document document = TryObtain.tryGet(Jsoup.connect(getUrl(search.getKeyWord())));
            if (pre(document, search)) {
                return;
            }
            for (Element element : document.select("tbody > tr:not([align])")) {
                Elements select = element.select(".odd");
                rule0(search, select, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean pre(Document document, Search search) {
        try {
            String href = Objects.requireNonNull(document.selectFirst("link")).attr("abs:href");
            Parser parser = ParserType.getParser(href);
            String bookName;
            String author;
            if (parser instanceof AbstractParser) {
                author = ((AbstractParser) parser).authorParser(document);
                bookName = ((AbstractParser) parser).nameParser(document);
            } else {
                author = Objects.requireNonNull(document.selectFirst("#info > p > a")).text();
                bookName = Objects.requireNonNull(document.selectFirst("#info > h1")).text();
            }
            search.addResult(new Result(href, bookName, author, type));
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * <meta property="og:image" content="https://www.biqukan.cc/files/article/image/40/40141/40141s.jpg">
     * <meta property="og:novel:category" content="玄幻小说">
     * <meta property="og:novel:author" content="明少江南">
     * <meta property="og:novel:book_name" content="诸天世界自由行">
     * <meta property="og:novel:read_url" content="https://www.biqukan.cc/article/40141/">
     * <meta property="og:url" content="https://www.biqukan.cc/article/40141/">
     */


    private String getUrl(String keyWord) throws UnsupportedEncodingException {
        return path + "?searchkey=" + encode(keyWord) + "&submit=%CB%D1%CB%F7";
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
        System.out.println(URLEncoder.encode("诸天世界", "GBK"));
        System.out.println(URLEncoder.encode("搜索", "GBK"));
    }
}
