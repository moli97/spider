package top.imoli.spider.rule.entity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.imoli.spider.rule.ReflectUtil;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author moli@hulai.com
 * @date 2022/2/16 11:04 AM
 */
public class RuleNode {

    private Rule rule;
    private RuleNode next;

    public RuleNode(Rule<?, ?> rule) {
        this.rule = rule;
    }

    public RuleNode(RuleExpr expr) {
        this.rule = RuleType.newRule(expr);
    }

    public void setNext(RuleNode next) {
        this.next = next;
    }

    public RuleNode getNext() {
        return next;
    }

    public <T, R> R apply(T t) {
        return (R) rule.apply(t);
    }

    public boolean hasNext() {
        return Objects.nonNull(next);
    }

    public <T, R> R applyAll(T t){
        RuleNode x = this;
        Object apply = t;
        while (x != null) {
            apply = x.apply(apply);
            x = x.getNext();
        }
        return (R) apply;
    }

    public static String join(Object... objects) {
        StringBuilder builder = new StringBuilder();
        for (Object o : objects) {
            builder.append(o);
        }
        return builder.toString();
    }

    public static void main(String[] args) throws Exception {
        RuleNode headNode = new RuleNode(new RuleExpr(RuleType.NONE.code));
        RuleNode lastNode;
        headNode.setNext(lastNode = new RuleNode(new RuleExpr(RuleType.ELEMENT_SELECT_FIRST.code, "body")));
        lastNode.setNext(lastNode = new RuleNode(new RuleExpr(RuleType.ELEMENT_SELECT_FIRST.code, "div")));
        lastNode.setNext(lastNode = new RuleNode(new RuleExpr(RuleType.ELEMENT_SELECT_FIRST.code, "a")));
        lastNode.setNext(lastNode = new RuleNode(new RuleExpr(RuleType.ELEMENT_TO_TEXT.code)));
        lastNode.setNext(lastNode = new RuleNode(text -> text + "___" + "xxx"));
        lastNode.setNext(lastNode = new RuleNode(new ReflectRuleExpr("replace", new String[]{"java.lang.CharSequence", "java.lang.CharSequence"}, "___", "_")));
        lastNode.setNext(lastNode = new RuleNode(new ReflectRuleExpr("startsWith", new String[]{"java.lang.String"}, "imoli")));

        RuleNode x = headNode;
        Object apply = Jsoup.connect("https://imoli.top/").get();
        while (x != null) {
            apply = x.apply(apply);
            x = x.getNext();
        }
        System.out.println(apply);

    }

    private static void test0() throws Exception {
        RuleNode headNode = new RuleNode(doc -> doc);
        RuleNode lastNode;
        headNode.setNext(lastNode = new RuleNode(element -> ((Element) element).selectFirst("body")));
        lastNode.setNext(lastNode = new RuleNode(element -> ((Element) element).selectFirst("div")));
        lastNode.setNext(lastNode = new RuleNode(element -> ((Element) element).selectFirst("a")));
        lastNode.setNext(lastNode = new RuleNode(element -> ((Element) element).text()));
        lastNode.setNext(lastNode = new RuleNode(text -> text + "ha"));

        RuleNode x = headNode;
        Object apply = Jsoup.connect("https://imoli.top/").get();
        while (x != null) {
            apply = x.apply(apply);
            x = x.getNext();
        }
        System.out.println(apply);
    }

    // TODO Rule工厂
    interface RuleFactory {
        Rule newRule(RuleExpr expr);
    }

    public enum RuleType {
        NONE(0, expr -> o -> o),
        ELEMENT_SELECT_FIRST(1, expr -> e -> ((Element) e).selectFirst(expr.getArg0())),
        ELEMENT_TO_TEXT(2, expr -> e -> ((Element) e).text()),
        ELEMENTS_TO_TEXT(3, expr -> e -> ((Elements) e).text()),
        STRING_REPLACE(4, expr -> text -> ((String) text).replace(expr.getArg(0), expr.getArg(1))),
        REFLECT_INVOKE(5, expr -> o -> {
            if (!(expr instanceof ReflectRuleExpr)) {
                throw new RuntimeException("expr type error: " + expr.getClass().getName());
            }
            return ReflectUtil.invoke(o, (ReflectRuleExpr) expr);
        }),
        ELEMENT_SELECT(6, expr -> e -> ((Element) e).select(expr.getArg0())),

        ;
        public final int code;
        public final RuleFactory factory;

        RuleType(int code, RuleFactory factory) {
            this.code = code;
            this.factory = factory;
        }

        public static Rule newRule(RuleExpr expr) {
            for (RuleType type : values()) {
                if (type.code == expr.getType()) {
                    return type.factory.newRule(expr);
                }
            }
            throw new RuntimeException("can not find type: " + expr.getType());
        }

    }

    public static class RuleExpr {
        protected int type;
        protected String[] args;

        public RuleExpr() {
        }

        public RuleExpr(int type, String... args) {
            this.type = type;
            this.args = args;
        }

        public int getType() {
            return type;
        }

        public String[] getArgs() {
            return args;
        }

        public String getArg(int index) {
            return args[index];
        }

        public String getArg0() {
            return args[0];
        }

        public <R> R getArg0T(Function<String, R> function) {
            return function.apply(args[0]);
        }
    }

    public static class ReflectRuleExpr extends RuleExpr {

        private String methodMame;
        private String[] types;

        public ReflectRuleExpr(String methodMame, String[] types, String... args) {
            super(RuleType.REFLECT_INVOKE.code, args);
            this.methodMame = methodMame;
            this.types = types;
        }

        public String getMethodMame() {
            return methodMame;
        }

        public String[] getTypes() {
            return types;
        }
    }
}
