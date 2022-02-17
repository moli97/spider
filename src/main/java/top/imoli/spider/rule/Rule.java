package top.imoli.spider.rule;

import java.util.function.Function;

/**
 * @author moli@hulai.com
 * @date 2022/2/17 11:41 AM
 */
public interface Rule<T, R> extends Function<T, R> {
    R apply(T t);
}
