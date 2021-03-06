package com.baidu.mapframework.app.fpstack;

import android.text.TextUtils;

/**
 * 页面注册注解处理器
 *
 * @author liguoqing
 * @version 1.0.0
 * @date 2013-6-29
 */
public class RegisterProcessor {

    public static void process(java.lang.Class<?>[] clzArray) {
        final TaskManager taskManager = TaskManagerFactory.getTaskManager();
        for (java.lang.Class<?> pageClz : clzArray) {
            RegisterPage register = pageClz.getAnnotation(RegisterPage.class);
            if (register == null) {
                continue;
            }
            java.lang.String taskName = register.taskName();
            if (taskName != null && !TextUtils.isEmpty(taskName)) {
                try {
                    java.lang.Class<?> taskClz = Class.forName(taskName);
                    taskManager.registerPage(taskClz, pageClz);
                } catch (java.lang.ClassNotFoundException e) {

                }
            }
        }
    }
}
