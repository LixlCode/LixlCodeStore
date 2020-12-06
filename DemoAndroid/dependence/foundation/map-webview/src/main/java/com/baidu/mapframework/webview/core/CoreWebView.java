/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.webview.core;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

//import com.baidu.baidumaps.common.crash.ErrorLog;
//import com.baidu.mapframework.common.account.AccountManager;
//import com.baidu.mapframework.common.config.GlobalConfig;
//import com.baidu.mapframework.common.customize.config.CstmConfigFunc;
//import com.baidu.mapframework.common.mapview.MapInfo;
//import com.baidu.mapframework.common.mapview.MapInfoProvider;
//import com.baidu.mapframework.location.LocationManager;
//import com.baidu.mapframework.statistics.ControlLogStatistics;
import com.baidu.mapframework.webview.WebUtil;
import com.baidu.mapframework.webview.core.websdk.IWebSDKController;
import com.baidu.mapframework.webview.core.websdk.WebSDKRuntime;
//import com.baidu.platform.comapi.JNIInitializer;
//import com.baidu.platform.comapi.map.MapStatus;
//import com.baidu.platform.comapi.util.MD5;
//import com.baidu.platform.comapi.util.MLog;
//import com.baidu.platform.comapi.util.SysOSAPIv2;
//import com.baidu.platform.comapi.util.URLEncodeUtils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * WebView内核部分，集成WebSDK通信；
 * WebChromeClient和WebViewClient代理；
 * 提供CoreClient和CoreIntercepter能力；
 * <p/>
 * User: liuda
 * Date: 3/17/15
 * Time: 5:37 PM
 */
public class CoreWebView extends WebView {

    protected static final String WEBVIEW_UA_SPEC = " baidumap_ANDR";

    private static final String TAG = CoreWebView.class.getName();

    /**
     * WebView debug开关
     */
    public static final boolean ENABLE_WEB_DEBUG = false;

    /**
     * 用户设置的WebChromeClient，本地会使用代理调用
     */
    private WebChromeClient userWebChromeClient;

    /**
     * 用户设置的WebViewClient，本地会使用代理调用
     */
    private WebViewClient userWebViewClient;

    /**
     * intercepters，提供扩展功能
     */
    private HashSet<ICoreWebViewIntercepter> intercepters = new HashSet<ICoreWebViewIntercepter>();

    /**
     * 统计webview的页面加载时间是成功率
     */
    private long mPageStartTime;
    private String mStartUrl;
    private Timer mLoadingTimer;

    // 预置的分享内容,由Fe在加载页面时先传给NA
    protected JSONObject shareContent;

    /**
     * 提供给内核插件的Web能力
     */
    protected final ICoreWebViewClient coreWebViewClient = new ICoreWebViewClient() {
        @Override
        public void loadUrl(/*@Nullable*/ final String url) {
            // MLog.d(TAG, "IMapWebViewClient loadUrl", url);
            if (TextUtils.isEmpty(url)) {
                return;
            }
            CoreWebView.this.loadUrl(url);
        }

        @Override
       /* @NotNull*/
        public Context getContext() {
            return CoreWebView.this.getContext();
        }
    };

    private final WebSDKRuntime webSDKRuntime = new WebSDKRuntime(coreWebViewClient);

    public CoreWebView(Context context) {
        super(context);
    }

    public CoreWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoreWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CoreWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void init() {
        // MLog.d(TAG, "init");

        final WebSettings settings = getSettings();
        try {
            // crash日志上看，红米手机上会报空指针
            settings.setAllowFileAccess(true);
            settings.setDomStorageEnabled(true);
            settings.setJavaScriptEnabled(true);

            if (Build.VERSION.SDK_INT < 18) {
                getSettings().setSavePassword(false);
            }

            super.setWebViewClient(webViewClient);
            super.setWebChromeClient(webChromeClient);

            // Android客户端WebView控件，addJavascriptInterface接口存在远程命令执行高危漏洞，可导致大量移动用户在使用百度安卓客户端时被挂马。
            removeJavascriptInterface("searchBoxJavaBridge_");
            removeJavascriptInterface("accessibility");
            removeJavascriptInterface("accessibilityTraversal");

            if (ENABLE_WEB_DEBUG) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    setWebContentsDebuggingEnabled(true);
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            // cookie 相关
            CookieManager.getInstance().setAcceptCookie(true);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
            }
        } catch (Exception e) {
            // MLog.e(TAG, "init exception", e);
            // ErrorLog.exceptionLog(e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for (Object listener : intercepters) {
            if (listener instanceof ICoreWebViewIntercepterEx) {
                ((ICoreWebViewIntercepterEx) listener).onWebViewResume();
            }
        }
    }

    /*@NotNull*/
    protected IWebSDKController getWebSDKController() {
        return webSDKRuntime.getWebSDKController();
    }

   /* @NotNull*/
    public ICoreWebViewClient getCoreWebViewClient() {
        return coreWebViewClient;
    }

    protected void addCoreWebViewIntercepter(/*@Nullable*/ final ICoreWebViewIntercepter intercepter) {
        intercepters.add(intercepter);
    }

    protected void removeCoreWebViewIntercepter(/*@Nullable*/ final ICoreWebViewIntercepter intercepter) {
        intercepters.remove(intercepter);
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        userWebChromeClient = client;
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        userWebViewClient = client;
    }

    private final WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {

            // 接收WebSDK发送的消息
            if (webSDKRuntime.getWebSDKChannel().onUrlOutput(url)) {
                return true;
            }

            /*if (CstmConfigFunc.isGooglePlayChannel(JNIInitializer.getCachedContext())
                    && !TextUtils.isEmpty(url) && url.endsWith(".apk")) {
                return true;
            }*/

            if (url.startsWith("http://") || url.startsWith("https://")) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            if (url.startsWith("bdapi://setShareContent")) {
                try {
                    String str = URLDecoder.decode(url.substring(url.indexOf("?") + 1), "UTF-8");
                    shareContent = new JSONObject(str);
                } catch (Exception e) {
                    // ignore
                }
                return true;
            }

            if (!WebUtil.isUrlBaiduDomain(getUrl())) {
                if (WebUtil.isUrlOpenApi(url)) {
                    // 非百度域,只能走OpenApi逻辑
                    for (final ICoreWebViewIntercepter intercepter : intercepters) {
                        if (intercepter.getIntercepterType() == ICoreWebViewIntercepter.TYPE_OPENAPI && intercepter
                                .onLoadUrl(url)) {
                            return true;
                        }
                    }
                }
                if (url.startsWith("weixin://")
                        || url.startsWith("alipays://")) {
                    for (final ICoreWebViewIntercepter intercepter : intercepters) {
                        if (intercepter.getIntercepterType() == ICoreWebViewIntercepter.TYPE_WALLET && intercepter.onLoadUrl
                                (url)) {
                            return true;
                        }
                    }
                }
                // 非百度域,非OpenApi,系统默认返回
                if (userWebViewClient != null) {
                    return userWebViewClient.shouldOverrideUrlLoading(view, url);
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

            if (url.startsWith("bdapi://getNativeInfo")) {
                Uri uri = Uri.parse(url);
                String callback = uri.getQueryParameter("callback");
                doJsCallback(callback);
                return true;
            }

            if (url.startsWith("bdapi://signOpra")) {
                try {
                    Uri uri = Uri.parse(url);
                    String json = uri.getQueryParameter("params");
                    String query = "";
                    JSONObject jsonObject = new JSONObject(json);
                    Iterator<String> iterator = jsonObject.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        //query = query + key + "=" + URLEncodeUtils.urlEncode(jsonObject.getString(key)) + "&";
                    }

                    //giveSignOpra(MD5.signOpra(query));
                } catch (Exception e) {
                    // ignore
                }
                return true;
            }

            for (final ICoreWebViewIntercepter intercepter : intercepters) {
                if (intercepter.onLoadUrl(url)) {
                    return true;
                }
            }

            if (userWebViewClient != null) {
                return userWebViewClient.shouldOverrideUrlLoading(view, url);
            } else {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // 页面加载完成，注入WebSDK通讯模块
            //webSDKRuntime.getWebSDKInjector().refreshInject(coreWebViewClient);
            // 统计
            if (!TextUtils.isEmpty(mStartUrl) && mStartUrl.equals(url)) {
                long endTime = System.currentTimeMillis();
                logWebTime("success", endTime - mPageStartTime, url);
            }
            if (userWebViewClient != null) {
                userWebViewClient.onPageFinished(view, url);
            } else {
                super.onPageFinished(view, url);
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mPageStartTime = System.currentTimeMillis();
            mStartUrl = url;
            initTimeOutTimer(view, url);

            if (userWebViewClient != null) {
                userWebViewClient.onPageStarted(view, url, favicon);
            } else {
                super.onPageStarted(view, url, favicon);
            }
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            if (userWebViewClient != null) {
                userWebViewClient.onLoadResource(view, url);
            } else {
                super.onLoadResource(view, url);
            }
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if (userWebViewClient != null) {
                return userWebViewClient.shouldInterceptRequest(view, url);
            } else {
                return super.shouldInterceptRequest(view, url);
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (userWebViewClient != null) {
                return userWebViewClient.shouldInterceptRequest(view, request);
            } else {
                return super.shouldInterceptRequest(view, request);
            }
        }

        @Override
        public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
            if (userWebViewClient != null) {
                userWebViewClient.onTooManyRedirects(view, cancelMsg, continueMsg);
            } else {
                super.onTooManyRedirects(view, cancelMsg, continueMsg);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (!TextUtils.isEmpty(mStartUrl) && mStartUrl.equals(failingUrl)) {
                logWebTime("fail", 0, failingUrl);
            }
            if (userWebViewClient != null) {
                userWebViewClient.onReceivedError(view, errorCode, description, failingUrl);
            } else {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            if (userWebViewClient != null) {
                userWebViewClient.onFormResubmission(view, dontResend, resend);
            } else {
                super.onFormResubmission(view, dontResend, resend);
            }
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            if (userWebViewClient != null) {
                userWebViewClient.doUpdateVisitedHistory(view, url, isReload);
            } else {
                super.doUpdateVisitedHistory(view, url, isReload);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (userWebViewClient != null) {
                userWebViewClient.onReceivedSslError(view, handler, error);
            } else {
                super.onReceivedSslError(view, handler, error);
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
            if (userWebViewClient != null) {
                userWebViewClient.onReceivedClientCertRequest(view, request);
            } else {
                super.onReceivedClientCertRequest(view, request);
            }
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            if (userWebViewClient != null) {
                userWebViewClient.onReceivedHttpAuthRequest(view, handler, host, realm);
            } else {
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
            }
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            if (userWebViewClient != null) {
                return userWebViewClient.shouldOverrideKeyEvent(view, event);
            } else {
                return super.shouldOverrideKeyEvent(view, event);
            }
        }

        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
            if (userWebViewClient != null) {
                userWebViewClient.onUnhandledKeyEvent(view, event);
            } else {
                super.onUnhandledKeyEvent(view, event);
            }
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            if (userWebViewClient != null) {
                userWebViewClient.onScaleChanged(view, oldScale, newScale);
            } else {
                super.onScaleChanged(view, oldScale, newScale);
            }
        }

        @Override
        public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
            if (userWebViewClient != null) {
                userWebViewClient.onReceivedLoginRequest(view, realm, account, args);
            } else {
                super.onReceivedLoginRequest(view, realm, account, args);
            }
        }
    };

    private final WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public boolean onJsAlert(WebView view, final String url, final String message, JsResult result) {
            if (userWebChromeClient != null) {
                return userWebChromeClient.onJsAlert(view, url, message, result);
            } else {
                return super.onJsAlert(view, url, message, result);
            }
        }

        @Override
        public boolean onJsConfirm(WebView view, final String url, final String message, JsResult result) {
            if (userWebChromeClient != null) {
                return userWebChromeClient.onJsConfirm(view, url, message, result);
            } else {
                return super.onJsConfirm(view, url, message, result);
            }
        }

        @Override
        public boolean onJsPrompt(WebView view, final String url, final String message, String defaultValue,
                                  JsPromptResult result) {
            for (final ICoreWebViewIntercepter intercepter : intercepters) {
                if (intercepter.onJsPrompt(url, message, defaultValue, result)) {
                    return true;
                }
            }

            if (userWebChromeClient != null) {
                return userWebChromeClient.onJsPrompt(view, url, message, defaultValue, result);
            } else {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        }

        @Override
        public boolean onConsoleMessage(/*@NotNull*/ final ConsoleMessage consoleMessage) {
            //接收WebSDK发送的消息
            webSDKRuntime.getWebSDKChannel().onJSOutput(consoleMessage.message());

            if (userWebChromeClient != null) {
                return userWebChromeClient.onConsoleMessage(consoleMessage);
            } else {
                return super.onConsoleMessage(consoleMessage);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (userWebChromeClient != null) {
                userWebChromeClient.onProgressChanged(view, newProgress);
            } else {
                super.onProgressChanged(view, newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (userWebChromeClient != null) {
                userWebChromeClient.onReceivedTitle(view, title);
            } else {
                super.onReceivedTitle(view, title);
            }
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            if (userWebChromeClient != null) {
                userWebChromeClient.onReceivedIcon(view, icon);
            } else {
                super.onReceivedIcon(view, icon);
            }
        }

        @Override
        public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
            if (userWebChromeClient != null) {
                userWebChromeClient.onReceivedTouchIconUrl(view, url, precomposed);
            } else {
                super.onReceivedTouchIconUrl(view, url, precomposed);
            }
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            if (userWebChromeClient != null) {
                userWebChromeClient.onShowCustomView(view, callback);
            } else {
                super.onShowCustomView(view, callback);
            }
        }

        @Override
        public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
            if (userWebChromeClient != null) {
                userWebChromeClient.onShowCustomView(view, requestedOrientation, callback);
            } else {
                super.onShowCustomView(view, requestedOrientation, callback);
            }
        }

        @Override
        public void onHideCustomView() {
            if (userWebChromeClient != null) {
                userWebChromeClient.onHideCustomView();
            } else {
                super.onHideCustomView();
            }
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            if (userWebChromeClient != null) {
                return userWebChromeClient.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
            } else {
                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
            }
        }

        @Override
        public void onRequestFocus(WebView view) {
            if (userWebChromeClient != null) {
                userWebChromeClient.onRequestFocus(view);
            } else {
                super.onRequestFocus(view);
            }
        }

        @Override
        public void onCloseWindow(WebView window) {
            if (userWebChromeClient != null) {
                userWebChromeClient.onCloseWindow(window);
            } else {
                super.onCloseWindow(window);
            }
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            if (userWebChromeClient != null) {
                return userWebChromeClient.onJsBeforeUnload(view, url, message, result);
            } else {
                return super.onJsBeforeUnload(view, url, message, result);
            }
        }

        @Override
        public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota,
                                            long estimatedDatabaseSize, long totalQuota,
                                            WebStorage.QuotaUpdater quotaUpdater) {
            if (userWebChromeClient != null) {
                userWebChromeClient.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize,
                        totalQuota, quotaUpdater);
            } else {
                super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota,
                        quotaUpdater);
            }
        }

        @Override
        public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
            if (userWebChromeClient != null) {
                userWebChromeClient.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
            } else {
                super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
            }
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            if (userWebChromeClient != null) {
                userWebChromeClient.onGeolocationPermissionsShowPrompt(origin, callback);
            } else {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            if (userWebChromeClient != null) {
                userWebChromeClient.onGeolocationPermissionsHidePrompt();
            } else {
                super.onGeolocationPermissionsHidePrompt();
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onPermissionRequest(PermissionRequest request) {
            if (userWebChromeClient != null) {
                userWebChromeClient.onPermissionRequest(request);
            } else {
                super.onPermissionRequest(request);
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onPermissionRequestCanceled(PermissionRequest request) {
            if (userWebChromeClient != null) {
                userWebChromeClient.onPermissionRequestCanceled(request);
            } else {
                super.onPermissionRequestCanceled(request);
            }
        }

        @Override
        public boolean onJsTimeout() {
            if (userWebChromeClient != null) {
                return userWebChromeClient.onJsTimeout();
            } else {
                return super.onJsTimeout();
            }
        }

        @Override
        public void onConsoleMessage(String message, int lineNumber, String sourceID) {
            if (userWebChromeClient != null) {
                userWebChromeClient.onConsoleMessage(message, lineNumber, sourceID);
            } else {
                super.onConsoleMessage(message, lineNumber, sourceID);
            }
        }

        @Override
        public Bitmap getDefaultVideoPoster() {
            if (userWebChromeClient != null) {
                return userWebChromeClient.getDefaultVideoPoster();
            } else {
                return super.getDefaultVideoPoster();
            }
        }

        @Override
        public View getVideoLoadingProgressView() {
            if (userWebChromeClient != null) {
                return userWebChromeClient.getVideoLoadingProgressView();
            } else {
                return super.getVideoLoadingProgressView();
            }
        }

        @Override
        public void getVisitedHistory(ValueCallback<String[]> callback) {
            if (userWebChromeClient != null) {
                userWebChromeClient.getVisitedHistory(callback);
            } else {
                super.getVisitedHistory(callback);
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {
            if (userWebChromeClient != null) {
                return userWebChromeClient.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            } else {
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }
        }
    };

    private void initTimeOutTimer(final WebView webView, final String url) {
        if (mLoadingTimer != null) {
            cancelLoadingTimer();
        }
        mLoadingTimer = new Timer();
        mLoadingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (webView != null && webView.getVisibility() == VISIBLE) {
                    if (!TextUtils.isEmpty(mStartUrl)) {
                        logWebTime("timeOut", 5000, url);
                    }
                }
            }
        }, 5000);
    }

    private synchronized void cancelLoadingTimer() {
        if (mLoadingTimer != null) {
            mLoadingTimer.cancel();
            mLoadingTimer.purge();
            mLoadingTimer = null;
        }
    }

    // 统计字段跟iphone保持一致
    private void logWebTime(String type, long time, String url) {
        cancelLoadingTimer();
        try {
            JSONObject args = new JSONObject();
            args.put("result", type);
            args.put("time", time);
            args.put("url", URLEncoder.encode(url, "UTF-8"));
            // ControlLogStatistics.getInstance().addLogWithArgs("BaseWebPG.webInfo", args);
        } catch (Exception e) {
            // ignore
        }

        mStartUrl = "";
    }

    @SuppressLint("DefaultLocale")
    private void doJsCallback(String callback) {
        if (TextUtils.isEmpty(callback)) {
            return;
        }
        if (!WebUtil.isUrlBaiduDomain(getUrl())) {
            return;
        }
        String jsfunc =
                "javascript:" + callback
                        + "('{\"loc_x\":\"%d\",\"loc_y\":\"%d\",\"c\":\"%d\",\"cuid\":\"%s\",\"os\":\"%s\","
                        + "\"mb\":\"%s\",\"ov\":\"%s\",\"sv\":\"%s\",\"bduss\":\"%s\",\"xdpi\":\"%s\",\"ydpi\":\"%s\","
                        + "\"net\":\"%s\",\"ua\":\"%s\",\"market\":\"%s\",\"zid\":\"%s\",\"bs\":\"%s\",\"zoom\":\"%s\","
                        + "\"b\":\"%s\",\"mapcenter\":\"%s\",\"loc_cityname\":\"%s\",\"roam_c\":\"%d\","
                        + "\"roam_cityname\":\"%s\",\"roam_citytype\":\"%d\"";

        /*String zid = AccountManager.getInstance().getSafeFacadeZid();
        final MapInfo mapInfo = MapInfoProvider.getMapInfo();
        float zoomLevel = mapInfo.getMapLevel();
        MapStatus.GeoBound mapBound = mapInfo.getMapStatus().geoRound;
        String bound = String.format("(%s,%s,%s,%s)", mapBound.left, mapBound.bottom, mapBound.right, mapBound.top);

        String center = String.format("(%s,%s)", mapInfo.getMapCenter().getLongitude(),
                mapInfo.getMapCenter().getLatitude());
        int roamCityid = GlobalConfig.getInstance().getRoamCityId();
        int roamType = GlobalConfig.getInstance().getRoamCityType();
        String roamCityName = GlobalConfig.getInstance().getRoamCityName();
        String locCityName = GlobalConfig.getInstance().getLastLocationCityName();

        int cid = GlobalConfig.getInstance().getLastLocationCityCode();
        jsfunc =
                String.format(jsfunc,
                        (int)LocationManager.getInstance().getCurLocation(null).longitude,
                        (int)LocationManager.getInstance().getCurLocation(null).latitude,
                        cid == 1 ? 0 : cid,
                        SysOSAPIv2.getInstance().getCuid(),
                        SysOSAPIv2.getInstance().getOSVersion(),
                        SysOSAPIv2.getInstance().getPhoneType(),
                        SysOSAPIv2.getInstance().getOSVersion(),
                        SysOSAPIv2.getInstance().getVersionName(),
                        AccountManager.getInstance().isLogin() ? AccountManager.getInstance().getBduss() : "",
                        String.valueOf(SysOSAPIv2.getInstance().getXDpi()),
                        String.valueOf(SysOSAPIv2.getInstance().getYDpi()),
                        SysOSAPIv2.getInstance().getNetType(),
                        WEBVIEW_UA_SPEC,
                        CstmConfigFunc.getMarket(JNIInitializer.getCachedContext()),
                        zid, LocationManager.getInstance().getLocInfo(), zoomLevel, bound, center, locCityName, roamCityid,
                        roamCityName, roamType);*/

        jsfunc += "}')";

        loadUrl(jsfunc);
    }

    private void giveSignOpra(String value) {
        String jsFunc = "javascript:signOpra('{\"sign\":\"%s\"}')";
        jsFunc = String.format(jsFunc, value);
        loadUrl(jsFunc);
    }

}