package top.imoli.spider;

import top.imoli.spider.task.BookTask;

/**
 * @author moli@hulai.com
 * @date 2022/1/24 5:12 PM
 */
public class Bootstrap {

    public static void main(String[] args) {
       // String bookUrl = "https://www.biqukan.cc/article/21304/";//轮回乐园
        String bookUrl = "https://www.biqukan.cc/article/56098/";//小阁老
        Book book = new Book(bookUrl);
        new BookTask(book).run();
    }


}
