package top.imoli.spider.rule;

/**
 * @author moli@hulai.com
 * @date 2022/2/17 11:22 AM
 */
public interface RuleFactory {
    Rule newRule(RuleExpr expr);
}