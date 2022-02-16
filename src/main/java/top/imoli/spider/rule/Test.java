package top.imoli.spider.rule;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.imoli.spider.config.Format;
import top.imoli.spider.config.Replace;
import top.imoli.spider.config.SpiderConfig;
import top.imoli.spider.entity.Book;
import top.imoli.spider.entity.Chapter;
import top.imoli.spider.exception.ParserException;
import top.imoli.spider.parser.AbstractParser;
import top.imoli.spider.parser.Parser;
import top.imoli.spider.parser.ParserType;
import top.imoli.spider.rule.entity.RuleNode;
import top.imoli.spider.rule.entity.RuleNode.RuleExpr;
import top.imoli.spider.rule.entity.RuleNode.RuleType;
import top.imoli.spider.task.BookTask;
import top.imoli.spider.task.ChapterTask;
import top.imoli.spider.task.TryObtain;
import top.imoli.spider.util.ProgressUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {

    public static void main(String[] args) {
        RuleNode name = new RuleNode(new RuleNode.RuleExpr(RuleType.NONE.code));
        RuleNode lastNode;
        name.setNext(lastNode = new RuleNode(new RuleExpr(RuleType.ELEMENT_SELECT_FIRST.code, ".info > h1")));
        lastNode.setNext(lastNode = new RuleNode(new RuleExpr(RuleType.ELEMENT_TO_TEXT.code)));

        RuleNode author = new RuleNode(new RuleNode.RuleExpr(RuleType.NONE.code));
        author.setNext(lastNode = new RuleNode(new RuleExpr(RuleType.ELEMENT_SELECT_FIRST.code, ".info > .small > span")));
        lastNode.setNext(lastNode = new RuleNode(new RuleExpr(RuleType.ELEMENT_TO_TEXT.code)));
        lastNode.setNext(lastNode = new RuleNode(new RuleNode.ReflectRuleExpr("replace", new String[]{"java.lang.CharSequence", "java.lang.CharSequence"}, "作者：", "")));

        RuleNode chapterList = new RuleNode(new RuleNode.RuleExpr(RuleType.NONE.code));
        chapterList.setNext(lastNode = new RuleNode(new RuleExpr(RuleType.ELEMENT_SELECT_FIRST.code, ".listmain > dl")));
        lastNode.setNext(lastNode = new RuleNode(new RuleExpr(RuleType.ELEMENT_SELECT.code, "dd:not(.more) > a")));

        RuleNode chapter = new RuleNode(new RuleNode.RuleExpr(RuleType.NONE.code));
        chapter.setNext(new RuleNode(new RuleExpr(RuleType.ELEMENT_SELECT_FIRST.code, "#chaptercontent")));

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

        private RuleNode nameRuleNode;
        private RuleNode authorRuleNode;
        private RuleNode chapterListRuleNode;
        private RuleNode chapterRuleNode;

        public TestParser(RuleNode nameRuleNode, RuleNode authorRuleNode, RuleNode chapterListRuleNode, RuleNode chapterRuleNode) {
            this.nameRuleNode = nameRuleNode;
            this.authorRuleNode = authorRuleNode;
            this.chapterListRuleNode = chapterListRuleNode;
            this.chapterRuleNode = chapterRuleNode;
        }

        @Override
        public String nameParser(Document doc) {
            return nameRuleNode.applyAll(doc);
        }

        @Override
        public String authorParser(Document doc) {
            return authorRuleNode.applyAll(doc);
        }

        @Override
        public Elements chapterList(Document doc) {
            return chapterListRuleNode.applyAll(doc);
        }

        @Override
        public void chapterParser(Document doc, Chapter chapter) {
            Element element = chapterRuleNode.applyAll(doc);
            chapter.setText(Replace.handle(getText(element)));
        }
    }

}
