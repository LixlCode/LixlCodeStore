/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.webview;

//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import com.baidu.platform.comapi.map.config.Preferences;
//import com.baidu.platform.comapi.util.MLog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * 检测WebView中Console.log是否可用
 * <p/>
 * User: liuda
 * Date: 3/18/15
 * Time: 2:00 PM
 */
public class ConsoleLogTester {

    private static final String TAG = ConsoleLogTester.class.getName();

    /**
     * 测试实效时间
     */
    private static final long TEST_FAILURE_TIME = 1000 * 60 * 60 * 24;

    /**
     * 单次测试超时
     */
    private static final long TEST_TIMEOUT = 1000 * 60 * 15;

    /**
     * 测试消息内容
     */
    private static final String TEST_MESSAGE_CONTENT = "testConsoleLog";

    private interface TestPreference {
        String KEY_LAST_CHECK_STATUE = "last_check_statue";
        String KEY_LAST_CHECK_TIME   = "last_check_time";
        String VALUE_UNCHECKED       = "unchecked";

        String VALUE_SUPPORT   = "support";
        String VALUE_UNSUPPORT = "unsupport";
    }

    //private final Preferences preferences;

    /*@Nullable*/
    private WebView testWebView;

    public ConsoleLogTester(/*@NotNull*/ Context context) {
        //preferences = Preferences.build(context, "Preferences_WebViewConsoleLogTester");
        try {
            testWebView = new WebView(context);
        } catch (Throwable e) {
            testWebView = null;
        }
    }

    /*public void testConsoleLog(*//*@NotNull*//* final TestCallback callback) {
        if (testWebView == null) {
            return;
        }

        final String lastCheck = preferences.getString(TestPreference.KEY_LAST_CHECK_STATUE,
          TestPreference.VALUE_UNCHECKED);
        final long lastCheckTime = preferences.getLong(TestPreference.KEY_LAST_CHECK_TIME, 0);

        if (TestPreference.VALUE_SUPPORT.equals(lastCheck)) {
            if (System.currentTimeMillis() - lastCheckTime > TEST_FAILURE_TIME) {
                //MLog.d(TAG, "testConsoleLog", "support but expire");
                checkInWebView(callback);
            } else {
                //MLog.d(TAG, "testConsoleLog", "support good");
                callback.onSupport();
            }
        } else if (TestPreference.VALUE_UNCHECKED.equals(lastCheck)) {
            //MLog.d(TAG, "testConsoleLog", "uncheck");
            checkInWebView(callback);
        } else {
            //MLog.d(TAG, "testConsoleLog", "not support");
        }
    } lxl */

    private void checkInWebView(/*@NotNull*/ final TestCallback callback) {
        if (testWebView == null) {
            return;
        }

        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable timeoutCallback = new Runnable() {
            @Override
            public void run() {
                //MLog.d(TAG, "checkInWebView unsupport");
                /*preferences.putString(TestPreference.KEY_LAST_CHECK_STATUE,
                  TestPreference.VALUE_UNSUPPORT);*/
            }
        };

        testWebView.getSettings().setJavaScriptEnabled(true);
        WebChromeClient testWebChromeClient = new WebChromeClient() {
            @SuppressLint("NewApi")
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                if (TEST_MESSAGE_CONTENT.equals(consoleMessage.message())) {
                    /*preferences.putString(TestPreference.KEY_LAST_CHECK_STATUE,
                      TestPreference.VALUE_SUPPORT);
                    preferences.putLong(TestPreference.KEY_LAST_CHECK_TIME,
                      System.currentTimeMillis());*/
                    callback.onSupport();
                    handler.removeCallbacks(timeoutCallback);
                    //MLog.d(TAG, "checkInWebView support");
                }
                return true;
            }
        };
        // Android客户端WebView控件，addJavascriptInterface接口存在远程命令执行高危漏洞，
        // 可导致大量移动用户在使用百度安卓客户端时被挂马。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            testWebView.removeJavascriptInterface("searchBoxJavaBridge_");
            testWebView.removeJavascriptInterface("accessibility");
            testWebView.removeJavascriptInterface("accessibilityTraversal");
        }
        try {
            // 这行抛出异常,catch住,不走console log
            testWebView.setWebChromeClient(testWebChromeClient);
        } catch (NoSuchMethodError error) {
            return;
        }
        testWebView.loadUrl("javascript:console.log('" + TEST_MESSAGE_CONTENT + "')");

        handler.postDelayed(timeoutCallback, TEST_TIMEOUT);
    }

    public interface TestCallback {
        void onSupport();
    }
}