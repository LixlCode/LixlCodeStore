package com.baidu.mapframework.scenefw.binding.functions;

import android.support.annotation.NonNull;

/**
 * A functional interface (callback) that computes a value based on multiple input values
 *
 * @param <V1> first input value
 * @param <V2> second input value
 * @param <R>  output value
 *
 * Author: wangyongyu
 * Date: 2017/3/8
 */

public interface Func2<V1, V2, R> {
    R apply(@NonNull V1 v1, @NonNull V2 v2);
}
