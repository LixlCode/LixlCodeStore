package com.baidu.mapframework.webview.core.websdk;

import java.util.LinkedList;

//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;

import com.baidu.mapframework.webview.core.ICoreWebViewClient;
//import com.baidu.platform.comapi.util.MLog;

import android.text.TextUtils;

/**
 * 注入WebSDK JS代码，当前此类只是保证JS注入成功后，才对内发送消息
 * <p/>
 * User: liuda
 * Date: 3/25/15
 * Time: 11:36 AM
 */
public class WebSDKInjector {

    private static final String TAG = WebSDKInjector.class.getName();

    /**
     * WebSDK 加载完成消息，可以由上层或者SDK内部发送
     */
    private static final String INJECT_READY_SIGNAL = "'webSdkReady',{'inject': 'ready'}";

    private boolean webSDKReady = false;
    private LinkedList<IEnsureInjectCallback> injectCallbacks = new LinkedList<IEnsureInjectCallback>();

    /**
     * 刷新回调检测（当有新页面加载时）
     */
    public synchronized void refreshInject(/*@NotNull*/ final ICoreWebViewClient client) {
        // MLog.d(TAG, "refreshInject");
        webSDKReady = false;
        client.loadUrl("javascript:BMapCom.Kernel.notify(" + INJECT_READY_SIGNAL + ");");
    }

    /**
     * 拦截注入信号
     */
    public synchronized boolean interceptInjectSignal(/*@Nullable*/ WebSDKMessage message) {
        if (message != null && TextUtils.equals(INJECT_READY_SIGNAL, message.invokeEvent)) {
            // MLog.d(TAG, "interceptInjectSignal webSDKReady");
            for (IEnsureInjectCallback callback : injectCallbacks) {
                callback.onEnsureFinish();
            }
            injectCallbacks.clear();
            webSDKReady = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 确认在注入成功后调用
     */
    public synchronized void ensureInjectFinish(/*@NotNull*/ final IEnsureInjectCallback callback) {
  /*      if (webSDKReady) {
            callback.onEnsureFinish();
        } else {
            injectCallbacks.add(callback);
        }*/
        callback.onEnsureFinish();

    }

    public interface IEnsureInjectCallback {
        void onEnsureFinish();
    }
}