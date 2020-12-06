package com.wecome.demo.network;

import com.wecome.demo.model.FeasibleFragmentModel;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RetrofitHttpService {

    // 不支持背压
    @GET("福利/10/1")
    Observable<FeasibleFragmentModel> getAndroidDataForFeasibleFragmentNoBackpressure();

    // 支持背压
    @GET("福利/10/1")
    Flowable<FeasibleFragmentModel> getAndroidDataForFeasibleFragmentBackpressure();

}