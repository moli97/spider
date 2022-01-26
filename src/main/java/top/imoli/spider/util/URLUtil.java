package top.imoli.spider.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author moli@hulai.com
 * @date 2022/1/26 10:26 AM
 */
public class URLUtil {

    public static String getDomain(String url) {
        try {
            URL base = new URL(url);
            String[] split = base.getHost().split("\\.");
            return split[split.length - 2];
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String format(String url) {
        return url.replace("//", "/").replace(":/", "://");
    }
}
