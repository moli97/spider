package top.imoli.spider.task;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author moli@hulai.com
 * @date 2022/1/26 5:24 PM
 */
public class SearchTask {
    /**
     * https://www.biqukan.cc/modules/article/search.php?searchkey=轮回乐园&submit=搜索
     * https://www.biqukan.cc/modules/article/search.php?searchkey=%C2%D6%BB%D8%C0%D6%D4%B0&submit=%CB%D1%CB%F7
     * GBK编码,唯一结果会重定向到目标
     */

    /**
     * https://www.bige7.com/s?q=轮回乐园
     * 可以不编码,唯一结果不会跳转
     */

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
