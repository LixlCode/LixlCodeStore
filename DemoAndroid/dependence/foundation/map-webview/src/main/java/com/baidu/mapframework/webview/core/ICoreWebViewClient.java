package com.baidu.mapframework.webview.core;

import android.content.Context;

/**
 * 提供给内核插件的WebView能力
 * <p/>
 * User: liuda
 * Date: 3/17/15
 * Time: 5:35 PM
 */
public interface ICoreWebViewClient {

    /**
     * 加载页面
     */
    void loadUrl(String url);

    /**
     * 获取上下文
     */
    Context getContext();
}