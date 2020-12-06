package com.baidu.mapframework.webview.core.websdk;

/**
 * UI操作WebSDK的控制器
 * <p/>
 * User: liuda
 * Date: 3/25/15
 * Time: 12:08 PM
 */
public interface IWebSDKController {

    /**
     * 发送WebSDK消息
     * @param message 消息
     * @param callback 返回回调
     */
    void sendWebSDKMessage(WebSDKMessage message, WebSDKMessage.MessageCallback callback);

    /**
     * 设置消息接收
     */
    void setWebSDKMessageHandler(IWebSDKMessageHandler handler);

    /**
     * 协议过滤器，必须注册协议，才能掉用
     */
    void setWebSDKMessageFilter(IWebSDKMessageFilter filter);
}