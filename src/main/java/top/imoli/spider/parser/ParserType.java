package top.imoli.spider.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author moli@hulai.com
 * @date 2022/1/26 10:50 AM
 */
public enum ParserType {

    BI_QU_KAN("https://www.biqukan.cc", BiqukanParser::new),
    BI_GE_7("https://www.bige7.com", Bige7Parser::new),
    ;
    public final String baseUrl;
    public final ParserFactory factory;

    ParserType(String bastUrl, ParserFactory factory) {
        this.baseUrl = bastUrl;
        this.factory = factory;
    }

    private Parser newParser() {
        return factory.newParser(baseUrl);
    }

    private static Map<String, Parser> cache = new HashMap<>();

    public static Parser getParser(String url) {
        if (cache.containsKey(url)) {
            return cache.get(url);
        }
        for (ParserType value : values()) {
            if (url.startsWith(value.baseUrl)) {
                Parser newParser = value.newParser();
                cache.put(url, newParser);
                return newParser;
            }
        }
        throw new IllegalArgumentException(url + " 找不到对应的解析器");
    }

    interface ParserFactory {
        Parser newParser(String url);
    }
}
