package top.imoli.spider.task;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @author moli@hulai.com
 * @date 2022/2/14 5:09 PM
 */
public class TryObtain {

    private static final int TRY_COUNT = 5;

    public static final Document tryGet(Connection connection) throws IOException {
        return try0(connection, Connection.Method.GET);
    }

    public static final Document tryPost(Connection connection) throws IOException {
        return try0(connection, Connection.Method.POST);
    }

    private static final Document try0(Connection connection, Connection.Method method) throws IOException {
        for (int i = 1; i <= TRY_COUNT; i++) {
            try {
                switch (method) {
                    case GET:
                        return connection.get();
                    case POST:
                        return connection.post();
                }
            } catch (IOException e) {
                if (i >= TRY_COUNT) {
                    throw e;
                }
            } catch (Exception e) {
                System.out.println(i + "\t");
            }
        }
        throw new RuntimeException("Multiple attempts failed");
    }

}
