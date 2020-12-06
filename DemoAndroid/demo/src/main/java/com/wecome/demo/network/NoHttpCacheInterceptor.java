package com.wecome.demo.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.wecome.demo.utils.NetStatusUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class NoHttpCacheInterceptor  implements Interceptor{

    private Context mContext;

    NoHttpCacheInterceptor(Context context) {
        this.mContext = context;
    }

    /**
     * 在没有网络的情况下，取读缓存数据
     */
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        boolean connected = NetStatusUtil.getInstance(mContext).isNetworkConnected(mContext);
        //如果没有网络，则启用 FORCE_CACHE
        if (!connected) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Log.e("lxl", "无网络设置_common");

            Response response = chain.proceed(request);
            return response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=60")
                    .removeHeader("Pragma")
                    .build();
        }
        //有网络的时候，这个拦截器不做处理，直接返回
        return chain.proceed(request);
    }

}
