package top.imoli.spider;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
        String targetIds = System.getProperty("targetIds");
        if (targetIds != null && targetIds.length() > 0) {
            PROPERTIES.setProperty("targetIds", targetIds);
        }
        System.out.println(PROPERTIES);
        initPool();
    }

    static void load(String config) {
        try {
            load(ClassLoader.getSystemResourceAsStream(config));
            String configFile = PROPERTIES.getProperty("configFile");
            if (configFile != null && configFile.length() > 0) {
                try {
                    load(new FileInputStream(configFile));
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                    load(new FileInputStream(System.getProperty("user.home") + "/spider/spider.properties"));
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void load(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        PROPERTIES.load(reader);
        reader.close();
        is.close();
    }

    static void initPool() {
        int threads = getThreads();
        int processors = Runtime.getRuntime().availableProcessors();
        boolean force = isForceThreads();
        if (!force && (threads < processors || threads > processors * 4)) {
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

    public static boolean isForceThreads() {
        String async = getProperty("forceThreads", "false");
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
        if (pool != null && !pool.isShutdown()) {
            pool.shutdown();
        }
    }
}
