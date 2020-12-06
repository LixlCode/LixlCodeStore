package com.baidu.mapframework.webview.core.websdk;

/**
 * WebSDK消息接受处理接口
 * <p/>
 * User: liuda
 * Date: 3/17/15
 * Time: 8:14 PM
 */
public interface IWebSDKMessageHandler {


    void handleMessage(WebSDKMessage message, WebSDKMessage.MessageCallback callback);

}