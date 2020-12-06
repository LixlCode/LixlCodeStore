package com.baidu.mapframework.webview.core.websdk;

import static com.baidu.mapframework.webview.core.websdk.WebSDKChannelConstant.ChannelScheme;
import static com.baidu.mapframework.webview.core.websdk.WebSDKChannelConstant.ChannelType;
import static com.baidu.mapframework.webview.core.websdk.WebSDKChannelConstant.WebSDKMessageParam;

import java.net.URLDecoder;
import java.util.HashMap;

//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

//import com.baidu.baidumaps.common.crash.ErrorLog;
//import com.baidu.mapframework.nirvana.looper.LooperManager;
//import com.baidu.mapframework.nirvana.looper.LooperTask;
//import com.baidu.mapframework.nirvana.module.Module;
//import com.baidu.mapframework.nirvana.schedule.ScheduleConfig;
import com.baidu.mapframework.webview.core.ICoreWebViewClient;
//import com.baidu.platform.comapi.util.MLog;
//import com.baidu.platform.comapi.util.UiThreadUtil;

import android.text.TextUtils;

/**
 * WebSDK通讯管道，监听WebView输入处处消息
 * <p/>
 * User: liuda
 * Date: 3/18/15
 * Time: 5:55 PM
 */
public class WebSDKChannel {
    private static final String TAG = WebSDKChannel.class.getName();

    private final ICoreWebViewClient mapWebViewClient;

    private IWebSDKMessageHandler receiveHandler;
    private ChannelType channelType = ChannelType.URL;
    private HashMap<String, WebSDKMessage.MessageCallback> callbackMap =
            new HashMap<String, WebSDKMessage.MessageCallback>();

    public WebSDKChannel(/*@NotNull*/ ICoreWebViewClient mapWebViewClient) {
        this.mapWebViewClient = mapWebViewClient;
    }

    public void setChannelType(/*@NotNull*/ ChannelType type) {
        // MLog.d(TAG, "setChannelType", type.getTypeValue());
        this.channelType = ChannelType.URL;
    }

    public void setJSMessageHandler(/*@Nullable*/ IWebSDKMessageHandler receiveHandler) {
        // MLog.d(TAG, "setJSMessageHandler");
        this.receiveHandler = receiveHandler;
    }

    public void sendJSMessage(/*@NotNull*/ final WebSDKMessage message,
                             /* @Nullable*/ final WebSDKMessage.MessageCallback callback) {
        // MLog.d(TAG, "sendJSMessage", message.toString());
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WebSDKMessageParam.INVOKE_EVENT_KEY, message.invokeEvent);
            jsonObject.put(WebSDKMessageParam.PARAM_KEY, message.param);
            jsonObject.put(WebSDKMessageParam.CALLBACK_EVENT_KEY, message.callbackEvent);
            callbackMap.put(message.callbackEvent, callback);
            notifyJSRuntime(jsonObject.toString());
        } catch (Exception e) {
            //MLog.e(TAG, "sendJSMessage exception", e);
            //ErrorLog.exceptionLog(e);
        }
    }

    public boolean onUrlOutput(/*@Nullable*/ final String url) {
        // MLog.d(TAG, "onUrlOutput", url);

        if (TextUtils.isEmpty(url) || !url.startsWith(ChannelScheme.MESSAGE_SCHEME)) {
            return false;
        }

        try {
            final String msgBody = URLDecoder.decode(url.replace(ChannelScheme.MESSAGE_SCHEME, ""), "UTF-8");
            if (msgBody.startsWith(WebSDKChannelConstant.ChannelScheme.MESSAGE_SEMAPHORE)) {
                // MLog.d(TAG, "onUrlOutput MESSAGE_SEMAPHORE");
                mapWebViewClient
                        .loadUrl("javascript:BMapComBridge.getMessage(\"" + channelType.getTypeValue() + "\");");
            } else if (msgBody.startsWith(WebSDKChannelConstant.ChannelScheme.MESSAGE_QUEUE)) {
                // MLog.d(TAG, "onUrlOutput MESSAGE_QUEUE");
                String[] msgs = msgBody.replace(WebSDKChannelConstant.ChannelScheme.MESSAGE_QUEUE, "")
                                        .split(WebSDKChannelConstant.ChannelScheme.MESSAGE_SPERATOR);
                for (String m : msgs) {
                    parseWebSDKMessage(m);
                }
            }
            return true;
        } catch (Exception e) {
            // MLog.d(TAG, "onUrlOutput exception", e);
            return false;
        }
    }

    /**
     * 当前协议设计十分不可理，console.log输出的信息，客户端很不方便解析
     */
    public boolean onJSOutput(/*@Nullable*/ final String msg) {
        // MLog.d(TAG, "onJSOutput", msg);

        if (TextUtils.isEmpty(msg) || !msg.contains(WebSDKMessageParam.PARAM_KEY)
                    || !msg.contains(WebSDKMessageParam.CALLBACK_EVENT_KEY)) {
            return false;
        }

        try {
            String[] msgs = msg.split(WebSDKChannelConstant.ChannelScheme.MESSAGE_SPERATOR);
            for (String m : msgs) {
                parseWebSDKMessage(m);
            }
            return true;
        } catch (Exception e) {
            // MLog.d(TAG, "onJSOutput exception", e);
            return false;
        }
    }

    private void notifyNARuntime(/*@NotNull*/ final WebSDKMessage message,
                                 /*@Nullable*/ final WebSDKMessage.MessageCallback callback) {
        // MLog.d(TAG, "notifyNARuntime", message.toString());
        //UiThreadUtil.runOnUiThread(new Runnable() {
        //    @Override
        //    public void run() {
                receiveHandler.handleMessage(message, callback);
        //    }
        //});
    }

    private void notifyJSRuntime(/*@Nullable*/ final String msg) {
        // MLog.d(TAG, "notifyJSRuntime", msg);

        if (TextUtils.isEmpty(msg)) {
            return;
        }

        //LooperManager.executeTask(Module.WEB_SDK_MODULE, new LooperTask() {
        //    @Override
        //    public void run() {
                mapWebViewClient.loadUrl("javascript:BMapComBridge.notify(" + msg + ");");
        //    }
        //}, ScheduleConfig.forData());
    }

    private void parseWebSDKMessage(/*@Nullable*/ String msgBody) {
        // MLog.d(TAG, "parseWebSDKMessage", msgBody);
        if (TextUtils.isEmpty(msgBody)) {
            return;
        }

        try {
            JSONObject msgJson = new JSONObject(msgBody);
            // JS 消息请求，格式参数 invokeEvent param callbackEvent
            if (msgJson.has(WebSDKMessageParam.INVOKE_EVENT_KEY)) {
                final String invokeEvent = msgJson.getString(WebSDKMessageParam.INVOKE_EVENT_KEY);
                final String params = msgJson.getString(WebSDKMessageParam.PARAM_KEY);
                final String callbackEvent = msgJson.getString(WebSDKMessageParam.CALLBACK_EVENT_KEY);

                notifyNARuntime(new WebSDKMessage(invokeEvent, params, callbackEvent),
                                       new WebSDKMessage.MessageCallback() {
                                           @Override
                                           public void onReturn(final int code, final JSONObject value) {
                                               JSONObject returnJson = new JSONObject();
                                               try {
                                                   returnJson.put(WebSDKMessageParam.CALLBACK_EVENT_KEY, callbackEvent);
                                                   JSONObject result = new JSONObject();
                                                   result.put(WebSDKMessageParam.ERROR_NO, code);
                                                   if (value == null) {
                                                       result.put(WebSDKMessageParam.RESULT_DATA_KEY, "");
                                                   } else {
                                                       result.put(WebSDKMessageParam.RESULT_DATA_KEY, value);
                                                   }
                                                   returnJson.put(WebSDKMessageParam.RESPONSE_DATA_KEY, result);
                                                   notifyJSRuntime(returnJson.toString());
                                               } catch (JSONException e) {
                                                   // MLog.e(TAG, "parseWebSDKMessage notifyNARuntime", e);
                                               }
                                           }
                                       });
                // JS 消息回调，参数格式 param callbackEvent
            } else if (msgJson.has(WebSDKMessageParam.PARAM_KEY)
                               && msgJson.has(WebSDKMessageParam.CALLBACK_EVENT_KEY)) {

                final String callbackEvent = msgJson.getString(WebSDKMessageParam.CALLBACK_EVENT_KEY);
                final JSONObject responseData = msgJson.getJSONObject(WebSDKMessageParam.RESPONSE_DATA_KEY);
                final WebSDKMessage.MessageCallback messageCallback = callbackMap.get(callbackEvent);
                if (messageCallback != null) {
                    callbackMap.remove(callbackEvent);
                    //LooperManager.executeTask(Module.WEB_SDK_MODULE, new LooperTask() {
                    //    @Override
                    //    public void run() {
                            String result = responseData.optString(WebSDKMessageParam.RESULT_DATA_KEY);
                            JSONObject jsonResult = null;
                            if (TextUtils.isEmpty(result)) {
                                try {
                                    jsonResult = new JSONObject(result);
                                } catch (JSONException e) {
                                    // MLog.d(TAG, "messageCallback", e);
                                }
                            }
                            messageCallback.onReturn(responseData.optInt(WebSDKMessageParam.ERROR_NO), jsonResult);
                        }
                    //}, ScheduleConfig.forData());
                //}
                // JS 消息
            } else {
                // MLog.e(TAG, "bad format webSDK message content " + msgBody);
            }
        } catch (JSONException e) {
            // MLog.e(TAG, "parseWebSDKMessage parseMessage", e);
        }
    }
}