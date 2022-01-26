package top.imoli.spider.exception;

/**
 * @author moli@hulai.com
 * @date 2022/1/26 2:47 PM
 */
public class ParserException extends RuntimeException {

    public ParserException() {
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserException(Throwable cause) {
        super(cause);
    }
}
