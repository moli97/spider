package top.imoli.spider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author moli@hulai.com
 * @date 2022/1/25 11:31 AM
 */
public class SpiderConfig {

    private static final Properties PROPERTIES;
    private static ExecutorService pool;

    private SpiderConfig() {
    }

    static {
        PROPERTIES = new Properties();
        load("spider.properties");
        initPool();
    }

    static void load(String config) {
        InputStream is = ClassLoader.getSystemResourceAsStream(config);
        try {
            PROPERTIES.load(is);
            if (is != null) {
                is.close();
            }
            String configFile = PROPERTIES.getProperty("configFile");
            if (configFile != null && configFile.length() > 0) {
                try {
                    is = new FileInputStream(configFile);
                    PROPERTIES.load(is);
                    is.close();
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
            is = new FileInputStream(System.getProperty("user.home") + "/spider/spider.properties");
            PROPERTIES.load(is);
            is.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static void initPool() {
        int threads = getThreads();
        int processors = Runtime.getRuntime().availableProcessors();
        if (threads < processors || threads > processors * 4) {
            threads = processors * 2;
        }
        pool = Executors.newFixedThreadPool(threads);
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }

    public static String getSaveFormat() {
        return getProperty("savePath", System.getProperties().getProperty("user.home")) + "/" + getFormat();
    }

    public static boolean isAsync() {
        String async = getProperty("async", "false");
        return Boolean.parseBoolean(async);
    }

    public static String getFormat() {
        return getProperty("format", "%s-%s.txt");
    }

    public static String getPrefixUrl() {
        return getProperty("prefixUrl", "https://www.biqukan.cc/article/");
    }

    public static Collection<String> getTargets() {
        String targetIds = getProperty("targetIds", "");
        String[] split = targetIds.split(",");
        String prefix = getPrefixUrl();
        return Arrays.stream(split).map(id -> prefix + "/" + id + "/").collect(Collectors.toSet());
    }

    private static int getThreads() {
        String threads = getProperty("threads", "0");
        return Integer.parseInt(threads);
    }

    public static ExecutorService getPool() {
        return pool;
    }

    public static void shutdown() {
        if (pool != null) {
            if (pool.isShutdown()) {
                return;
            }
            pool.shutdown();
        }
    }
}
