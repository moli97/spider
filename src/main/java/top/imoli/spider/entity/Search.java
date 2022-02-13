package top.imoli.spider.entity;

import java.util.HashSet;
import java.util.Set;

public class Search {

    private String keyWord;
    private Set<Result> results;

    public Search(String keyWord) {
        this.keyWord = keyWord;
        this.results = new HashSet<>();
    }

    public String getKeyWord() {
        return keyWord;
    }

    public Set<Result> getResults() {
        return results;
    }

    public boolean addResult(Result result) {
        return results.add(result);
    }

    @Override
    public String toString() {
        return "Search{" +
                "keyWord='" + keyWord + '\'' +
                ", results=" + results +
                '}';
    }
}
