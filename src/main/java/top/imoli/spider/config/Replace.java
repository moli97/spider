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
        list.add(new Replace("亲,点击进去,给个好评呗,分数越高更新越快,据说给新笔趣阁打满分的最后都找到了漂亮的老婆哦!"));
        list.add(new Replace("手机站全新改版升级地址：https://wap.xbiquge.la，数据和书签与电脑站同步，无广告清新阅读！"));
    }

    public static String handle(String text) {
        for (Replace replace : list) {
            text = text.replaceAll(replace.source, replace.target);
        }
        return text;
    }
}
