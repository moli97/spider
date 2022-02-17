package top.imoli.spider.rule;

/**
 * @author moli@hulai.com
 * @date 2022/2/17 11:17 AM
 */
public class RuleExpr {
    private RuleType type;
    private String param;
    private String[] args;

    public RuleExpr() {
    }

    public RuleExpr(RuleType type, String... args) {
        this.type = type;
        this.args = args;
    }

    public RuleExpr(String param, RuleType type, String... args) {
        this.type = type;
        this.param = param;
        this.args = args;
    }

    public RuleType getType() {
        return type;
    }

    public void setType(RuleType type) {
        this.type = type;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public Rule newRule() {
        return type.newRule(this);
    }

    public String getArg(int index) {
        return this.args[index];
    }
}
