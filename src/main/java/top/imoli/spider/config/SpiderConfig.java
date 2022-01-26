package top.imoli.spider.config;

import top.imoli.spider.util.URLUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
        PROPERTIES.keySet().forEach(key -> {
            if (key instanceof String) {
                String property = System.getProperty((String) key);
                if (property != null && property.length() > 0) {
                    PROPERTIES.setProperty((String) key, property);
                }
            }
        });
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

    public static String getTimeFormat() {
        return getProperty("timeFormat", "yyyy-MM-dd");
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
        return getProperty("format", "{name}-{author}.txt");
    }

    public static Collection<String> getTargets() {
        String[] urls = getProperty("taskUrls", "").split(",");
        if (urls.length == 0) {
            return Collections.emptyList();
        }
        String[] taskGroup = getProperty("taskGroup", "").split(":");
        if (taskGroup.length == 0) {
            return Collections.emptyList();
        }
        int min = Math.min(urls.length, taskGroup.length);
        Set<String> set = new HashSet<>();
        for (int i = 0; i < min; i++) {
            String url = urls[i];
            String tasks = taskGroup[i];
            if (tasks.length() == 0) {
                //continue;
            }
            String[] split = tasks.split(",");
            Set<String> collect = Arrays.stream(split).map(id -> URLUtil.format(url + "/" + id + "/")).collect(Collectors.toSet());
            set.addAll(collect);
        }
        return set;
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
