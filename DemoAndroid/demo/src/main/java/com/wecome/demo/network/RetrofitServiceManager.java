package com.wecome.demo.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit的工具类
 *
 * 单例模式
 * 封装 Retrofit2.x + RxJava2.x + OkHttp3.x
 */
public class RetrofitServiceManager {

    private static final int DEFAULT_TIME_OUT = 5;// 连接超时时间
    private static final int DEFAULT_READ_TIME_OUT = 10; // 读操作超时时间
    private static final int DEFAULT_WRITE_TIME_OUT = 10; // 写操作超时时间

    private volatile static RetrofitServiceManager singleton;
    private Retrofit mRetrofit;  // Retrofit实例

    private static final String BASE_URL = "http://gank.io/api/data/";

    /**
     * 构造方法
     * 生成了 Retrofit 实例，并配置了OkHttpClient 和一些 公共配置。
     * 提供了一个create（）方法，生成 接口实例，接收 Class泛型。
     */
    private RetrofitServiceManager(Context context) {

        // 创建公共参数拦截器
        HttpCommonInterceptor commonInterceptor = new HttpCommonInterceptor.Builder()
                .addHeaderParams("paltform", "android")
                .addHeaderParams("userToken", "1234343434dfdfd3434")
                .addHeaderParams("userId", "123445")
                .build();

        // 打印请求参数
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY; // 日志显示级别
        // 新建Log拦截器
        HttpLoggingInterceptor loggingInterceptor
                = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {

            @Override
            public void log(@NonNull String message) {
                Log.e("lxl", "Http请求的参数=="+message);
            }
        });
        // 给日志拦截器设置日志级别
        loggingInterceptor.setLevel(level);


        // 创建 OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(commonInterceptor) // 公共拦截器
                .addInterceptor(loggingInterceptor)   // 日志拦截器
                .addInterceptor(new NoHttpCacheInterceptor(context))        // 网络拦截器
                .addNetworkInterceptor(new HttpCacheInterceptor(context))   // 网络拦截器
                .cache(new Cache(new File(context
                        .getCacheDir()+"/netCache/"), 10*1024*1024))
                .cookieJar(new CookieManger(context))   // cookie设置
                .build();

        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

    }

    /**
     * 获取RetrofitServiceManager实例
     * @return
     */
    public static RetrofitServiceManager getInstance(Context context){
        if (singleton == null) {
            synchronized (RetrofitServiceManager.class) {
                if (singleton == null) {
                    singleton = new RetrofitServiceManager(context);
                }
            }
        }
        return singleton;
    }

    /**
     * 获取对应的Service
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }

}
