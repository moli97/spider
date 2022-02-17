package top.imoli.spider.rule;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.imoli.spider.config.Replace;
import top.imoli.spider.config.SpiderConfig;
import top.imoli.spider.entity.Book;
import top.imoli.spider.entity.Chapter;
import top.imoli.spider.exception.ParserException;
import top.imoli.spider.parser.AbstractParser;
import top.imoli.spider.parser.Parser;
import top.imoli.spider.task.ChapterTask;
import top.imoli.spider.task.TryObtain;
import top.imoli.spider.util.ProgressUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author moli@hulai.com
 * @date 2022/2/15 9:25 AM
 */
public class Bootstrap {
    public static void main(String[] args) {
        RuleSet name = RuleSet.Builder.builder()
                .addRule(new RuleExpr(".info > h1", RuleType.ELEMENT_SELECT_FIRST))
                .addRule(new RuleExpr(RuleType.ELEMENT_TO_TEXT))
                .build();
        RuleSet author = RuleSet.Builder.builder()
                .addRule(new RuleExpr(".info > .small > span", RuleType.ELEMENT_SELECT_FIRST))
                .addRule(new RuleExpr(RuleType.ELEMENT_TO_TEXT))
                .addRule(new RuleExpr("replace", RuleType.REFLECT_INVOKE, "作者：", ""))
                .build();
        RuleSet chapterList = RuleSet.Builder.builder()
                .addRule(new RuleExpr(".listmain > dl", RuleType.ELEMENT_SELECT_FIRST))
                .addRule(new RuleExpr("dd:not(.more) > a", RuleType.ELEMENT_SELECT))
                .build();
        RuleSet chapter = RuleSet.Builder.builder()
                .addRule(new RuleExpr("#chaptercontent", RuleType.ELEMENT_SELECT_FIRST))
                .build();
        TestParser parser = new TestParser(name, author, chapterList, chapter);
        call("https://www.bige7.com/book/58561", parser);
    }

    public static Book call(String bookUrl, Parser parser) {
        Book book = new Book(bookUrl);
        try {
            System.out.println("正在解析url: " + bookUrl);
            Document doc = TryObtain.tryGet(Jsoup.connect(bookUrl));
            parser.bookParser(doc, book);
            String bookName = book.getName();
            System.out.println("小说名: 《" + bookName + "》\t 作者: " + book.getAuthor());
            int total = book.getList().size();
            CountDownLatch downLatch = new CountDownLatch(total);
            Executor pool = SpiderConfig.getPool();
            AtomicInteger progress = new AtomicInteger();
            ProgressUtil.print(bookName, "下载中: ", total, progress);
            for (Chapter chapter : book.getList()) {
                pool.execute(new ChapterTask(chapter, downLatch, progress, parser));
            }
            downLatch.await();
            Thread.sleep(200);
            System.out.println("小说名: 《" + bookName + "》" + "爬取完成");
            //saveAsFileWriter(Format.format(bookName, book.getAuthor(), bookUrl), book.toText());
        } catch (ParserException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }

    static class TestParser extends AbstractParser {

        private final RuleSet nameRuleNode;
        private final RuleSet authorRuleNode;
        private final RuleSet chapterListRuleNode;
        private final RuleSet chapterRuleNode;

        public TestParser(RuleSet nameRuleNode, RuleSet authorRuleNode, RuleSet chapterListRuleNode, RuleSet chapterRuleNode) {
            this.nameRuleNode = nameRuleNode;
            this.authorRuleNode = authorRuleNode;
            this.chapterListRuleNode = chapterListRuleNode;
            this.chapterRuleNode = chapterRuleNode;
        }

        @Override
        public String nameParser(Document doc) {
            return nameRuleNode.apply(doc);
        }

        @Override
        public String authorParser(Document doc) {
            return authorRuleNode.apply(doc);
        }

        @Override
        public Elements chapterList(Document doc) {
            return chapterListRuleNode.apply(doc);
        }

        @Override
        public void chapterParser(Document doc, Chapter chapter) {
            Element element = chapterRuleNode.apply(doc);
            chapter.setText(Replace.handle(getText(element)));
        }
    }
}
