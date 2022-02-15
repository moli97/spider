package top.imoli.spider.rule.entity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.imoli.spider.rule.Generic;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Function;

/**
 * @author moli@hulai.com
 * @date 2022/2/15 9:26 AM
 */
public class AbstractRule<T> {

    private final LinkedList<Rule> rules;
    private final Class<T> clazzT;

    public AbstractRule() {
        this.rules = new LinkedList<>();
        this.clazzT = (Class<T>) Generic.parse(this.getClass(), AbstractRule.class).getByName("T");
    }

    public void addRule(Rule rule) {
        if (rules.isEmpty()) {
            rules.add(rule);
        } else {
            Rule last = rules.getLast();
            rules.add(rule);
        }
    }

    public boolean check() {
        Class clazz = Document.class;
        for (Rule rule : this.rules) {
            Class<?> ruleT = Generic.parse(rule.getClass(), Rule.class).getByName("T");
            if (!ruleT.isAssignableFrom(clazz)) {
                return false;
            }
            clazz = Generic.parse(rule.getClass(), Rule.class).getByName("R");
            System.out.println(ruleT.getSimpleName() + " > " + clazz.getSimpleName());
        }
        return clazzT.isAssignableFrom(clazz);
    }

    public T apply(Document document) {
        Iterator<Rule> iterator = rules.iterator();
        Object o = document;
        while (iterator.hasNext()) {
            Rule next = iterator.next();
            o = next.apply(o);
        }
        return (T) o;
    }

    public static void main(String[] args) throws IOException {
//        String body = Jsoup.connect("https://data.imoli.top/live2d//get/?id=1-53")
//                .header("Content-Type", "application/json;charset=UTF-8")
//                .ignoreContentType(true)
//                .execute()
//                .body();

        //TODO 行为树 ？ 状态机 ？解释器模式 ？ 责任链 ？
        AbstractRule<String> rule = new AbstractRule<String>() {

        };
        rule.addRule(new ElementRule<Element>() {

            @Override
            public Element apply(Element element) {
                return element.selectFirst("body");
            }
        });
        rule.addRule(new ElementRule<Elements>() {

            @Override
            public Elements apply(Element element) {
                return element.select("div");
            }
        });
        rule.addRule(new ElementsRule<Elements>() {

            @Override
            public Elements apply(Elements elements) {
                return elements.select("div");
            }
        });
        rule.addRule(new ElementsRule<Element>() {

            @Override
            public Element apply(Elements elements) {
                return elements.select("div").get(0);
            }
        });
        rule.addRule(new ElementRule<String>() {

            @Override
            public String apply(Element element) {
                return element.attr("class");
            }
        });
        rule.addRule(new Rule<String, String>() {

            @Override
            public String apply(String s) {
                return s.replace(" ", "");
            }
        });
        System.out.println(rule.check());
        Document doc = Jsoup.connect("https://imoli.top/").get();
        String apply = rule.apply(doc);
        System.out.println(apply);
    }
}

interface Rule<T, R> extends Function<T, R> {
    R apply(T t);
}

interface ElementsRule<R> extends Rule<Elements, R> {
    @Override
    R apply(Elements elements);
}

interface ElementRule<R> extends Rule<Element, R> {
    @Override
    R apply(Element element);
}



