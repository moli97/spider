package top.imoli.spider.rule;

import top.imoli.spider.rule.entity.RuleNode.ReflectRuleExpr;

import java.lang.reflect.Method;

/**
 * @author moli@hulai.com
 * @date 2022/2/16 4:17 PM
 */
public class ReflectUtil {

    public static void main(String[] args) throws Exception {
        Object invoke = invoke("mo__li", "replace", new String[]{"java.lang.CharSequence", "java.lang.CharSequence"}, new String[]{"__", "_"});
        System.out.println(invoke);
    }

    public static void test0() throws Exception {
        Class<?> clazz = getClass("java.lang.String");
        Method method = clazz.getMethod("replace", getClass("java.lang.CharSequence"), getClass("java.lang.CharSequence"));
        String text = "mo__li";
        Object invoke = method.invoke(text, "__", "_");
        System.out.println(invoke);
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

    public static Object invoke(Object o, ReflectRuleExpr expr) {
        return invoke(o, expr.getMethodMame(), expr.getTypes(), expr.getArgs());
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
}
