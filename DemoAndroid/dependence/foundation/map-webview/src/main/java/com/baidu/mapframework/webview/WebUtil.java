/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.webview;

import java.net.MalformedURLException;
import java.net.URL;

import android.text.TextUtils;

/**
 * Created by lichenyu on 11/12/15.
 */
public class WebUtil {

    public static final String BAIDU = "baidu.com";
    public static final String NUOMI = "nuomi.com";
    public static final String PASS = "wappass.baidu.com";

    public static boolean isUrlBaiduDomain(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (url.startsWith("http") || url.startsWith("https")) {
            try {
                String host = "";
                URL s = new URL(url);
                host = s.getHost();
                if (isHostBaiduDomain(host)) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static boolean isHostBaiduDomain(String host) {
        return !TextUtils.isEmpty(host) && (host.endsWith(BAIDU) || host.endsWith(NUOMI));
    }

    public static boolean isUrlPassDomain(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (url.startsWith("http") || url.startsWith("https")) {
            try {
                String host = "";
                URL s = new URL(url);
                host = s.getHost();
                if (isHostPassDomain(host)) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static boolean isHostPassDomain(String host) {
        return !TextUtils.isEmpty(host) && host.endsWith(PASS);
    }

    public static boolean isUrlLegal(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        try {
            String scheme = "";
            URL s = new URL(url);
            scheme = s.getProtocol();
            if (("http".equals(scheme) || "https".equals(scheme))) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        // It's fuckin' illegal
        return false;
    }

    public static boolean isUrlOpenApi(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return url.startsWith("bdapp://map") || url.startsWith("baidumap://map");
    }

    public static String getHost(String url) {
        String domain = "";
        try {
            URL s = new URL(url);
            domain = s.getHost();
        } catch (MalformedURLException e) {
            // do nothing
        }
        return domain;
    }

}
