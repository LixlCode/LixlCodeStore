package com.baidu.mapframework.webview.core.websdk;

//import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

/**
 * WebSDK通讯消息实体类
 * <p/>
 * User: liuda
 * Date: 3/17/15
 * Time: 8:04 PM
 */
public class WebSDKMessage {

    public final String invokeEvent;
    public final String param;
    public final String callbackEvent;

    public static int SUCCESS = 0;
    public static int ERROR = 1;
    public static int CANCEL = -1;

    /**
     * 用户使用构造函数，自动生成 callbackEvent
     */
    public WebSDKMessage(/*@Nullable*/ String invokeEvent, /*@Nullable*/ String param) {
        this.invokeEvent = invokeEvent;
        this.param = param;
        this.callbackEvent = String.valueOf(generateMsgID());
    }

    /**
     * 内部使用，包存js callbackEvent
     */
    WebSDKMessage(/*@Nullable*/ String invokeEvent,/*@Nullable*/ String param, /*@Nullable*/ String callbackEvent) {
        this.invokeEvent = invokeEvent;
        this.param = param;
        this.callbackEvent = callbackEvent;
    }

    @Override
    public String toString() {
        return "WebSDKMessage{" +
                       "invokeEvent='" + invokeEvent + '\'' +
                       ", param='" + param + '\'' +
                       ", callbackEvent='" + callbackEvent + '\'' +
                       '}';
    }

    /**
     * 消息回调接口
     */
    public interface MessageCallback {
        void onReturn(int code, JSONObject value);
    }

    /**
     * NA到JS的递增消息ID
     */
    private static int msgID = 0;

    /**
     * 生成消息ID
     */
    private static synchronized int generateMsgID() {
        msgID++;
        return msgID;
    }

}