/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.scenefw;

/**
 * Log
 */
public class SFLog {
    public static void d(String msg) {
        android.util.Log.w("SceneFlow", msg);
    }

    public static void e(String msg) {
        android.util.Log.e("SceneFlow", msg);
    }

    public static void e(String msg, Exception e) {
        android.util.Log.e("SceneFlow", msg, e);
    }
}
