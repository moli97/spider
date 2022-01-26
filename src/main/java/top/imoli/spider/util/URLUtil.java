package top.imoli.spider.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author moli@hulai.com
 * @date 2022/1/26 10:26 AM
 */
public class URLUtil {

    public static String getBaseUrl(String url) {
        URL base = null;
        try {
            base = com.sun.deploy.util.URLUtil.getBase(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return base.getProtocol() + "://" + base.getHost();
    }

    public static String getHost(String url) {
        URL base = null;
        try {
            base = com.sun.deploy.util.URLUtil.getBase(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return base.getHost();
    }

    public static String getDomain(String url) {
        String[] split = getHost(url).split("\\.");
        return split[split.length - 2];
    }

    public static String format(String url) {
        return url.replace("//", "/").replace(":/", "://");
    }
}
