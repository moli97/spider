package top.imoli.spider.entity;

import top.imoli.spider.search.SearchType;

import java.util.Objects;

public class Result {

    private String url;
    private String bookName;
    private String author;
    private SearchType type;

    public Result(String url, String bookName, String author, SearchType type) {
        this.url = url;
        this.bookName = bookName;
        this.author = author;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public String getBookName() {
        return bookName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return Objects.equals(url, result.url) && Objects.equals(bookName, result.bookName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, bookName);
    }

    @Override
    public String toString() {
        return "Result{" +
                "url='" + url + '\'' +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", type=" + type +
                '}';
    }
}
