package com.baidu.mapframework.webview.core.websdk;

/**
 * WebSDK 通讯消息格式
 * <p/>
 * User: liuda
 * Date: 3/18/15
 * Time: 8:43 PM
 */
public class WebSDKChannelConstant {

    enum ChannelType {
        CONSOLE_LOG("consoleLog"),
        URL("url");

        private String typeValue;

        ChannelType(String typeValue) {
            this.typeValue = typeValue;
        }

        public String getTypeValue() {
            return typeValue;
        }
    }

    interface ChannelScheme {
        /**
         * 消息协议名
         */
        String MESSAGE_SCHEME = "bdscheme://";

        /**
         * 消息信号量
         */
        String MESSAGE_SEMAPHORE = "_MESSAGE_SEMAPHORE_";

        /**
         * 消息队列头
         */
        String MESSAGE_QUEUE = "_MESSAGE_QUEUE_";

        /**
         * 消息分隔符
         */
        String MESSAGE_SPERATOR = "_MESSAGE_SEPERATOR_";
    }

    interface WebSDKMessageParam {
        /**
         * 消息事件key
         */
        String INVOKE_EVENT_KEY = "invokeEvent";

        /**
         * 回调事件key
         */
        String CALLBACK_EVENT_KEY = "callbackEvent";

        /**
         * 参数key
         */
        String PARAM_KEY = "param";

        /**
         * 返回值key
         */
        String RESPONSE_DATA_KEY = "responseData";

        /**
         * 返回code 0 表示成功
         */
        String ERROR_NO = "errno";

        /**
         * 返回数据内容key
         */
        String RESULT_DATA_KEY = "result";
    }

}