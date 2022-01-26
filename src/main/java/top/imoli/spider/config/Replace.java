package top.imoli.spider.config;

import java.util.ArrayList;
import java.util.List;

public class Replace {

    public static final String DEFAULT_TARGET = "";
    public final String source;
    public final String target;

    public Replace(String source) {
        this(source, DEFAULT_TARGET);
    }

    public Replace(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public static final List<Replace> list = new ArrayList<>();

    static {
        list.add(new Replace("一秒记住【笔趣看 www.biqukan.cc】，精彩小说无弹窗免费阅读！"));
    }

    public static String handle(String text) {
        for (Replace replace : list) {
            text = text.replaceAll(replace.source, replace.target);
        }
        return text;
    }
}
