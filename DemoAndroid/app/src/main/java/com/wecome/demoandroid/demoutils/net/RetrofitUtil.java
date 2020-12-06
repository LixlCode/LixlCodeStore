package com.wecome.demoandroid.demoutils.net;

import android.content.Context;

/**
 * Created by Administrator on 2017/10/29.
 * Retrofit网络请求的封装
 */

public class RetrofitUtil {

    private static volatile RetrofitUtil mInstance;

    private RetrofitUtil() {}

    /**
     * 获取单例引用
     *
     * @return
     */
    public static RetrofitUtil getInstance(Context context) {
        RetrofitUtil instance = mInstance;
        if (instance == null) {
            synchronized (RetrofitUtil.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new RetrofitUtil();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }
}
