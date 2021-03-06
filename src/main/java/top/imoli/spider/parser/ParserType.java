package top.imoli.spider.parser;

import top.imoli.spider.parser.impl.Bige7Parser;
import top.imoli.spider.parser.impl.BiqukanParser;
import top.imoli.spider.parser.impl.XBiqugeParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author moli@hulai.com
 * @date 2022/1/26 10:50 AM
 */
public enum ParserType {

    BI_QU_KAN("https://www.biqukan.cc", BiqukanParser::new),
    BI_GE_7("https://www.bige7.com", Bige7Parser::new),
    X_BI_QU_GE("https://www.xbiquge.la", XBiqugeParser::new),
    ;
    public final String baseUrl;
    public final ParserFactory factory;

    ParserType(String bastUrl, ParserFactory factory) {
        this.baseUrl = bastUrl;
        this.factory = factory;
    }

    private Parser newParser() {
        return factory.newParser();
    }

    private static final Map<ParserType, Parser> cache = new HashMap<>();

    public static Parser getParser(String url) {
        for (ParserType value : values()) {
            if (url.startsWith(value.baseUrl)) {
                if (cache.containsKey(value)) {
                    return cache.get(value);
                }
                Parser newParser = value.newParser();
                cache.put(value, newParser);
                return newParser;
            }
        }
        throw new IllegalArgumentException(url + " 找不到对应的解析器");
    }

    interface ParserFactory {
        Parser newParser();
    }
}
