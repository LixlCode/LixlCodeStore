package com.baidu.mapframework.webview.core.websdk;

//import org.jetbrains.annotations.NotNull;

/**
 * WebSDK消息过滤器
 * <p/>
 * User: liuda
 * Date: 3/25/15
 * Time: 11:52 AM
 */
public interface IWebSDKMessageFilter {

    /**
     * 过滤NA发送到JS的协议
     */
    boolean nativeToJavascriptFilter(/*@NotNull*/ WebSDKMessage message);

    /**
     * 过滤JS发送到NA的协议
     */
    boolean javascriptNativeToFilter(/*@NotNull*/ WebSDKMessage message);
}