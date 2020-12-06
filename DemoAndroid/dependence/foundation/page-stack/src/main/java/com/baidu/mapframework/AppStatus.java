package com.baidu.mapframework;

/**
 * App Foreground or Background Status
 * <p>Title: AppStatus</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014 Baidu</p>
 * <p>Company: www.baidu.com</p>
 *
 * @author liguoqing
 * @version 1.0
 * @date 10/13/14
 */
public enum AppStatus {
    NONE,
    FORGROUND,
    BACKGROUND;

    private static volatile AppStatus appStatus = NONE;

    public static AppStatus get() {
        return appStatus;
    }

    public static void set(AppStatus status) {
        appStatus = status;
    }
}
