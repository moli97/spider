package top.imoli.spider.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author moli@hulai.com
 * @date 2022/1/24 5:42 PM
 */
public class Book {

    private String name;
    private String author;
    private String url;
    private List<Chapter> list = new ArrayList<>();

    public Book(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Chapter> getList() {
        return list;
    }

    public void setList(List<Chapter> list) {
        this.list = list;
    }

    public void addList(Chapter chapter) {
        this.list.add(chapter);
    }

    public String toText() {
        StringBuilder builder = new StringBuilder();
        builder.append(name).append("\n").append(author).append("\n");
        list.forEach(chapter -> builder.append(chapter.toText()).append("\n\n\n\n\n"));
        return builder.toString();
    }
}
