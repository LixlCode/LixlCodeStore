package com.baidu.mapframework.webview.core.websdk;

//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;

import com.baidu.mapframework.webview.ConsoleLogTester;
import com.baidu.mapframework.webview.core.ICoreWebViewClient;
//import com.baidu.platform.comapi.util.MLog;

/**
 * WebSDK运行环境，维护注入和消息分发
 * <p/>
 * User: liuda
 * Date: 3/17/15
 * Time: 3:19 PM
 */
public class WebSDKRuntime {

    private static final String TAG = WebSDKRuntime.class.getName();

    /**
     * JS通信注入类
     */
    private final WebSDKInjector injector;

    /**
     * 通信管道，在JS注入后才能使用
     */
    private final WebSDKChannel channel;

    /**
     * 通讯消息过滤
     */
    private IWebSDKMessageFilter messageFilter;

    public WebSDKRuntime(/*@NotNull*/ final ICoreWebViewClient mapWebViewClient) {
        injector = new WebSDKInjector();
        channel = new WebSDKChannel(mapWebViewClient);

        //new ConsoleLogTester(mapWebViewClient.getContext()).testConsoleLog(new ConsoleLogTester.TestCallback() {
        //    @Override
        //    public void onSupport() {
                channel.setChannelType(WebSDKChannelConstant.ChannelType.CONSOLE_LOG);
        //    }
        //});
    }

   /* @NotNull*/
    public WebSDKInjector getWebSDKInjector() {
        return injector;
    }

    /*@NotNull*/
    public WebSDKChannel getWebSDKChannel() {
        return channel;
    }

   /* @NotNull*/
    public IWebSDKController getWebSDKController() {
        return webSDKController;
    }

    private final IWebSDKController webSDKController = new IWebSDKController() {

        @Override
        public void sendWebSDKMessage(/*@Nullable*/ final WebSDKMessage message,
                                      /*@Nullable*/ final WebSDKMessage.MessageCallback callback) {
            if (message == null || (messageFilter != null && !messageFilter.nativeToJavascriptFilter(message))) {
                // MLog.d(TAG, "webSDKController", "sendWebSDKMessage", "message illegal " + message);
            } else {
                // MLog.d(TAG, "webSDKController", "sendWebSDKMessage", message.toString());
                injector.ensureInjectFinish(new WebSDKInjector.IEnsureInjectCallback() {
                    @Override
                    public void onEnsureFinish() {
                        channel.sendJSMessage(message, callback);
                    }
                });
            }
        }

        @Override
        public void setWebSDKMessageHandler(/*@Nullable*/ final IWebSDKMessageHandler handler) {
            IWebSDKMessageHandler filterHandler = new IWebSDKMessageHandler() {
                @Override
                public void handleMessage(WebSDKMessage message, WebSDKMessage.MessageCallback callback) {
                   /* if (injector.interceptInjectSignal(message)) {
                        return;
                    }
*/
                    if (message == null || (messageFilter != null && !messageFilter
                                                                              .javascriptNativeToFilter(message))) {
                        // MLog.d(TAG, "webSDKController", "receiveWebSDKMessage", "message illegal " + message);
                    } else {
                        // MLog.d(TAG, "webSDKController", "receiveWebSDKMessage", message.toString());
                        if (handler != null) {
                            handler.handleMessage(message, callback);
                        } else {
                            // MLog.d(TAG, "webSDKController", "receiveWebSDKMessage no handler", message.toString());
                        }
                    }
                }
            };
            channel.setJSMessageHandler(filterHandler);
        }

        @Override
        public void setWebSDKMessageFilter(/*@Nullable*/ IWebSDKMessageFilter filter) {
            messageFilter = filter;
        }
    };
}