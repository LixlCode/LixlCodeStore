package com.wecome.demoandroid.demoutils;

import android.util.Log;

/**
 * Created by Administrator on 2017/2/26.
 * 封装日志类：为的是控制日志的输出
 */

public class LogUtil {

    //Log的开关
    private static boolean showLog = false;
    //为了打出的日志看着清晰，设置两个分隔符
    private static final String INN = "-->>";
    private static final String INIO = "::";

    /**
     * 获取异常信息
     * @return
     */
    private static String getInfo(){
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return 	ste.getClassName() + INIO + ste.getLineNumber() + INIO + ste.getMethodName() + INN;
    }

    /**
     * 获取日志的标识
     * @param objTag
     * @return
     */
    private static String getTag(Object objTag) {
        String tag;
        if (objTag == null) {
            tag = "null";
        } else if (objTag instanceof String) {
            tag = (String) objTag;
        } else if (objTag instanceof Class) {
            // 如果objTag不是String，则取它的类名
            tag = ((Class<?>) objTag).getSimpleName();
        } else {
            tag = objTag.getClass().getSimpleName();
        }
        return tag;
    }

    /**
     * 获取日志输出时的提示语
     * @param objMsg
     * @return
     */
    private static String getMsg(Object objMsg) {
        String msg;
        if (objMsg == null) {
            msg = "null";
        } else {
            msg = objMsg.toString();
        }
        return msg;
    }

    /**
     * 日志输出正常的数据
     * @param objTag
     * @param objMsg
     */
    public static void i(Object objTag, Object objMsg) {
        if (!showLog) {
            Log.i(getTag(objTag), getInfo() + getMsg(objMsg));
        }
    }

    /**
     * 日志输出错误的信息
     * @param objTag
     * @param objMsg
     */
    public static void e(Object objTag, Object objMsg) {
        if (!showLog) {
            Log.i(getTag(objTag), getInfo() + getMsg(objMsg));
        }
    }

    /**
     * 日志输出一般的信息
     * @param objTag
     * @param objMsg
     */
    public static void v(Object objTag, Object objMsg) {
        if (!showLog) {
            Log.i(getTag(objTag), getInfo() + getMsg(objMsg));
        }
    }
}
