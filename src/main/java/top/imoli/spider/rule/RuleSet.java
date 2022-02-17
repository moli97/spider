package top.imoli.spider.rule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author moli@hulai.com
 * @date 2022/2/17 11:18 AM
 */
public class RuleSet {

    private List<Rule> rules;

    public RuleSet() {
        this.rules = new ArrayList<>();
    }

    public RuleSet(List<Rule> rules) {
        this.rules = rules;
    }

    public void addRule(Rule rule) {
        this.rules.add(rule);
    }

    public void addRule(RuleExpr expr) {
        this.rules.add(expr.newRule());
    }

    public void addRules(List<RuleExpr> exprList) {
        exprList.forEach(this::addRule);
    }


    public <R> R apply(Object o) {
        Iterator<Rule> iterator = this.rules.iterator();
        while (iterator.hasNext()) {
            o = iterator.next().apply(o);
        }
        return (R) o;
    }

    public static final class Builder {
        private List<Rule> rules;

        private Builder() {
            this.rules = new ArrayList<>();
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder rules(List<Rule> rules) {
            this.rules = rules;
            return this;
        }

        public Builder addRule(Rule rule) {
            this.rules.add(rule);
            return this;
        }

        public Builder addRule(RuleExpr expr) {
            this.rules.add(expr.newRule());
            return this;
        }

        public Builder addExprList(List<RuleExpr> exprList) {
            exprList.forEach(this::addRule);
            return this;
        }

        public Builder addRules(List<Rule> rules) {
            this.rules.addAll(rules);
            return this;
        }

        public RuleSet build() {
            return new RuleSet(rules);
        }
    }
}
