package top.imoli.spider;

import java.util.ArrayList;
import java.util.List;

public class Discard {

    public static final String DEFAULT_TARGET = "";
    public final String source;
    public final String target;

    public Discard(String source) {
        this(source, DEFAULT_TARGET);
    }

    public Discard(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public static final List<Discard> list = new ArrayList<>();

    static {
        list.add(new Discard("一秒记住【笔趣看 www.biqukan.cc】，精彩小说无弹窗免费阅读！"));
        list.add(new Discard("\\s{2,}", "\n"));
    }

    public static String handle(String text) {
        for (Discard discard : list) {
            text = text.replaceAll(discard.source, discard.target);
        }
        return text;
    }
}
