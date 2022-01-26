package top.imoli.spider.entity;

/**
 * @author moli@hulai.com
 * @date 2022/1/24 5:45 PM
 */
public class Chapter {

    private String name;
    private String url;
    private String text;

    public Chapter(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toText() {
        return name + "\n" + text + "\n";
    }
}
