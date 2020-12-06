/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.webview.core;

import android.webkit.JsPromptResult;

/**
 * WebView事件拦截器，提供拦截消息能力
 * <p/>
 * User: liuda
 * Date: 3/17/15
 * Time: 4:46 PM
 */
public interface ICoreWebViewIntercepter {

    int TYPE_LOGIN = 0;
    int TYPE_WALLET = 1;
    int TYPE_OPENAPI = 2;

    int getIntercepterType();

    /**
     * 拦截url载入，
     * 注意该函数在UI线程执行
     */
    boolean onLoadUrl(String url);

    /**
     * 拦截js prompt，
     * 注意该函数在UI线程执行
     */
    boolean onJsPrompt(String url, String message, String defaultValue, JsPromptResult result);
}