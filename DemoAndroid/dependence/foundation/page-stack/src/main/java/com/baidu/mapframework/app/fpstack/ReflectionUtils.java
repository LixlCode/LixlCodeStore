package com.baidu.mapframework.app.fpstack;

/**
 * <p>ReflectionUtils</p>
 * <p>Copyright: Copyright (c) 2014 Baidu</p>
 * <p>Company: www.baidu.com</p>
 *
 * @author liguoqing
 * @version 1.0
 * @date 7/9/15
 */
class ReflectionUtils {

    public static Class<?> forName(ClassLoader classLoader, String className) throws ClassNotFoundException {
        if (classLoader != null) {
            return classLoader.loadClass(className);
        } else {
            // current default class loader
            return Class.forName(className);
        }
    }

    public static Object newInstance(ClassLoader classLoader, String className)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> clz = forName(classLoader, className);
        if (clz != null) {
            return clz.newInstance();
        }
        return null;
    }
}
