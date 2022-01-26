package top.imoli.spider.config;

import java.util.Collection;

public interface KeyWord {

	void initKeyWord(String keyWords);

	void initKeyWord(Collection<String> keyWords);

	boolean exist(String words);

	String replace(String words);

}
