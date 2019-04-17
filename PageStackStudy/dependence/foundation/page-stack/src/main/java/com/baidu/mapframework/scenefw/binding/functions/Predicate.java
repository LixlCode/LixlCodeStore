package com.baidu.mapframework.scenefw.binding.functions;

import android.support.annotation.NonNull;

/**
 * A functional interface (callback) that returns true or false for the given input value
 *
 * @param <V> input
 *
 * Author: wangyongyu
 * Date: 2017/3/8
 */

public interface Predicate<V> {
    boolean test(@NonNull V v);
}
