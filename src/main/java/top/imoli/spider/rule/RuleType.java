package top.imoli.spider.rule;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.imoli.spider.util.ReflectUtil;

/**
 * @author moli@hulai.com
 * @date 2022/2/17 11:14 AM
 */
public enum RuleType {

    NONE(0, expr -> o -> o),
    ELEMENT_SELECT_FIRST(1, expr -> e -> ((Element) e).selectFirst(expr.getParam())),
    ELEMENT_SELECT(2, expr -> e -> ((Element) e).select(expr.getParam())),
    ELEMENT_TO_TEXT(3, expr -> e -> ((Element) e).text()),
    ELEMENTS_SELECT(4, expr -> e -> ((Elements) e).select(expr.getParam())),
    ELEMENTS_TO_TEXT(5, expr -> e -> ((Elements) e).text()),
    STRING_REPLACE(6, expr -> s -> ((String) s).replace(expr.getArg(0), expr.getArg(1))),
    STRING_FORMAT(7, expr -> s -> String.format(expr.getParam(), s)),
    REFLECT_INVOKE(8, expr -> o -> ReflectUtil.invoke(o, expr.getParam(), expr.getArgs())),
    ;
    public final int code;
    public final RuleFactory factory;

    RuleType(int code, RuleFactory factory) {
        this.code = code;
        this.factory = factory;
    }

    public Rule newRule(RuleExpr expr) {
        return factory.newRule(expr);
    }

    public static RuleType valueOf(int code) {
        for (RuleType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new RuntimeException("can not find type: " + code);
    }
}