package com.baidu.mapframework.scenefw.binding.functions;

import android.support.annotation.NonNull;

/**
 * A functional interface that takes a value and returns another value, possibly with a different type
 *
 * @param <V> input value
 * @param <R> output value
 *
 * Author: wangyongyu
 * Date: 2017/3/8
 */

public interface Func1<V, R> {
    R apply(@NonNull V v);
}
