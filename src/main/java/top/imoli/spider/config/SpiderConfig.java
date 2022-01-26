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
    private static final KeyWord KEY_WORD = new DFAKeyWord(true);

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
        initKeyWord();
    }

    private static void initKeyWord() {
        KEY_WORD.initKeyWord(getProperty(Constant.IGNORE_ERROR_KEY, ""));
    }

    static void load(String config) {
        try {
            load(ClassLoader.getSystemResourceAsStream(config));
            String configFile = PROPERTIES.getProperty(Constant.CONFIG_FILE);
            if (configFile != null && configFile.length() > 0) {
                try {
                    load(new FileInputStream(configFile));
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                    load(new FileInputStream(System.getProperty(Constant.USER_HOME) + "/spider/spider.properties"));
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
        return getProperty(Constant.SAVE_PATH, System.getProperties().getProperty(Constant.USER_HOME)) + "/" + getFormat();
    }

    public static String getTimeFormat() {
        return getProperty(Constant.TIME_FORMAT, "yyyy-MM-dd");
    }

    public static boolean isAsync() {
        String async = getProperty(Constant.ASYNC, "false");
        return Boolean.parseBoolean(async);
    }

    public static boolean isForceThreads() {
        String async = getProperty(Constant.FORCE_THREADS, "false");
        return Boolean.parseBoolean(async);
    }

    public static String getFormat() {
        return getProperty(Constant.FORMAT, "{name}-{author}.txt");
    }

    public static Collection<String> getTargets() {
        String[] urls = getProperty(Constant.TASK_URLS, "").split(",");
        if (urls.length == 0) {
            return Collections.emptyList();
        }
        String[] taskGroup = getProperty(Constant.TASK_GROUP, "").split(":");
        if (taskGroup.length == 0) {
            return Collections.emptyList();
        }
        int min = Math.min(urls.length, taskGroup.length);
        Set<String> set = new HashSet<>();
        for (int i = 0; i < min; i++) {
            String url = urls[i];
            String tasks = taskGroup[i];
            if (tasks.length() == 0) {
                continue;
            }
            String[] split = tasks.split(",");
            Set<String> collect = Arrays.stream(split).map(id -> URLUtil.format(url + "/" + id + "/")).collect(Collectors.toSet());
            set.addAll(collect);
        }
        return set;
    }

    private static int getThreads() {
        String threads = getProperty(Constant.THREADS, "0");
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

    public static boolean isExist(String words) {
        return KEY_WORD.exist(words);
    }
}
