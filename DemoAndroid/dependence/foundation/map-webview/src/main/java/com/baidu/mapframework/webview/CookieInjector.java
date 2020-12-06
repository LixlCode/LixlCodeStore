/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.webview;

//import com.baidu.mapframework.common.account.AccountManager;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;

public class CookieInjector {

    public static final String SAPI_BDUSS_COOKIE_URL = "http://map.baidu.com";
    public static final String SAPI_NUOMI_BDUSS_COOKIE_URL = "http://m.nuomi.com";
    // 注：这个常量里面首字母的空格不能删，是故意留的
    public static final String WEBVIEW_UA_SPEC = " baidumap_ANDR";

    /**
     * loadurl的时候种cookies
     */
    public static void injectCookie(Context context, WebSettings ws) {
        if (context == null || ws == null) {
            return;
        }
        // configAccountCookie(context, AccountManager.getInstance().getBduss()); lxl
        // 加入自定义的UA，注：此逻辑不能删
        String currUA = ws.getUserAgentString();
        if (currUA == null) {
            currUA = System.getProperty("http.agent");
        }
        if (currUA != null && !currUA.contains(WEBVIEW_UA_SPEC)) {
            ws.setUserAgentString(currUA + WEBVIEW_UA_SPEC);
        }
    }

    /**
     * 将本地账号信息同步到web页面
     *
     * @param context
     */
    private static void configAccountCookie(final Context context, String bduss) {

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        //注：此句不要修改，如有改动会导致取bduss失败
        // baidu域cookie
        String bdussPart = "BDUSS=" + bduss;
        String baiduCookie = bdussPart + ";domain=baidu.com;httponly;path=/";
        cookieManager.setCookie(SAPI_BDUSS_COOKIE_URL, baiduCookie);
        // nuomi域cookie
        String nuomiCookie = bdussPart + ";domain=nuomi.com;httponly;path=/";
        cookieManager.setCookie(SAPI_NUOMI_BDUSS_COOKIE_URL, nuomiCookie);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
            cookieSyncManager.sync();
        }
    }

}
