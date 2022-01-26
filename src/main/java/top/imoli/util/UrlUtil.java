package top.imoli.util;

import com.sun.deploy.util.URLUtil;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author moli@hulai.com
 * @date 2022/1/26 10:26 AM
 */
public class UrlUtil {

    public static String getBaseUrl(String url) {
        URL base = null;
        try {
            base = URLUtil.getBase(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return base.getProtocol() + "://" + base.getHost();
    }

    public static String format(String url) {
        return url.replace("//", "/").replace(":/", "://");
    }
}
