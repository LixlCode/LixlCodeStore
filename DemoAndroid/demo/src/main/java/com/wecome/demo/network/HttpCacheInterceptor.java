package com.wecome.demo.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.wecome.demo.utils.NetStatusUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 如果服务器支持缓存，请求返回的Response会带有这样的Header:Cache-Control, max-age=xxx
 * 这种情况下我们只需要手动给okhttp设置缓存就可以让okhttp自动帮你缓存了。
 * 这里的max-age的值代表了缓存在你本地存放的时间。参考代码如下：
 * OkHttpClient okHttpClient = new OkHttpClient();
 * OkHttpClient newClient = okHttpClient.newBuilder()
 * .cache(new Cache(mContext.getCacheDir(), 10240*1024))
 * .connectTimeout(20, TimeUnit.SECONDS)
 * .readTimeout(20, TimeUnit.SECONDS)
 * .build();
 * <p>
 * 如果服务器不支持缓存就可能没有指定这个头部，这种情况下我们就需要使用Interceptor
 * 来重写Respose的头部信息，从而让okhttp支持缓存。
 */
public class HttpCacheInterceptor implements Interceptor {

    private Context mContext;

    HttpCacheInterceptor(Context context) {
        this.mContext = context;
    }

    /**
     * 拦截器（服务器不支持缓存的情况下我们需要用拦截器来自己实现okHttp的缓存）
     *
     * @param chain
     * @return
     * @throws IOException
     */
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
//        //拦截Request对象
//        Request request = chain.request();
//        //判断有无网络连接
//        boolean connected = NetStatusUtil.getInstance(mContext).isNetworkConnected(mContext);
//        if (!connected) {
//            //如果没有网络,从缓存获取数据
//            request = request.newBuilder()
//                    .cacheControl(CacheControl.FORCE_CACHE)
//                    .build();
//            Log.e("lxl", "no network");
//        }
//        Response response = chain.proceed(request);
//
//        if (connected) {
//            //有网络，缓存时间短
//            Log.e("lxl", "有网络");
//            String cacheControl = request.cacheControl().toString();
//            return response.newBuilder()
//                    .removeHeader("Pragma")
//                    .header("Cache-Control", "public, max-age=90")
//                    .build();
//        } else {
//            //没有网络
//            Log.e("lxl", "没有网络的缓存设置");
//            int maxTime = 3600;
//            return response.newBuilder()
//                    //这里的设置的是我们的没有网络的缓存时间，想设置多少就是多少。
//                    .header("Cache-Control", "public, max-age=" + maxTime)
//                    .removeHeader("Pragma")
//                    .build();
//        }

        /**
         * 在有网络的情况下，先去读缓存，设置的缓存时间到了，在去网络获取
         */
        Request request = chain.request();
        boolean connected = NetStatusUtil.getInstance(mContext).isNetworkConnected(mContext);
        if(connected){
            //如果有网络，缓存60s
            Log.e("lxl","print");
            Response response = chain.proceed(request);
            int maxTime = 60;
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxTime)
                    .build();
        }
        //如果没有网络，不做处理，直接返回
        return chain.proceed(request);
    }

}
