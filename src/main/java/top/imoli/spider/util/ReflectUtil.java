package top.imoli.spider.util;

import org.apache.commons.lang3.reflect.MethodUtils;
import top.imoli.spider.exception.ParserException;

import java.lang.reflect.Method;

/**
 * @author moli@hulai.com
 * @date 2022/2/16 4:17 PM
 */
public class ReflectUtil {

    private ReflectUtil() {
    }

    public static Object invoke(Object o, String methodMame, String[] parameterTypes, Object... args) {
        try {
            Class<?>[] types = new Class[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                types[i] = getClass(parameterTypes[i]);
            }
            Method method = o.getClass().getMethod(methodMame, types);
            return method.invoke(o, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

    public static Class getClass(String className) {
        switch (className) {
            case "int":
                return int.class;
            case "long":
                return long.class;
            case "short":
                return short.class;
            case "byte":
                return byte.class;
            case "boolean":
                return boolean.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            case "char":
                return char.class;
        }
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("");
        }
    }

    public static Object invoke(Object object, String methodName, Object... args) {
        try {
            return MethodUtils.invokeMethod(object, methodName, args);
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        Object invoke = invoke("mo__li", "replace", new String[]{"java.lang.CharSequence", "java.lang.CharSequence"}, new String[]{"__", "_"});
        System.out.println(invoke);
    }
}
