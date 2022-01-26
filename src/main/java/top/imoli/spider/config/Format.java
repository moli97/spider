package top.imoli.spider.config;

import top.imoli.spider.util.URLUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Function;

/**
 * @author moli@hulai.com
 * @date 2022/1/26 3:11 PM
 */
public enum Format {
    NAME("name"),
    AUTHOR("author"),
    SOURCE("source", URLUtil::getDomain),
    TIME("time", s -> DateTimeFormatter.ofPattern(s).format(LocalDateTime.now())),
    ;
    public final String key;
    public final Function<String, String> function;

    Format(String key) {
        this(key, s -> s);
    }

    Format(String key, Function<String, String> function) {
        this.key = "{" + key + "}";
        this.function = function;
    }

    public String apply(String t) {
        return function.apply(t);
    }

    public static String format(Map<String, String> param) {
        String format = SpiderConfig.getSaveFormat();
        for (Format value : values()) {
            if (param.containsKey(value.key)) {
                format = format.replace("{" + value.key + "}", value.function.apply(param.get(value.key)));
            }
        }
        return format;
    }

    public static String format(String bookName, String author, String bookUrl) {
        String format = SpiderConfig.getSaveFormat();
        format = format.replace(NAME.key, NAME.apply(bookName));
        format = format.replace(AUTHOR.key, AUTHOR.apply(author));
        format = format.replace(SOURCE.key, SOURCE.apply(bookUrl));
        format = format.replace(TIME.key, TIME.apply(SpiderConfig.getTimeFormat()));
        return format;
    }
}
