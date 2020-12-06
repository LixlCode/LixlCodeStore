package com.baidu.mapframework.scenefw.binding.functions;

import android.support.annotation.NonNull;

/**
 * A functional interface (callback) that accepts a single value.
 *
 * @param <V> input value
 *            <p>
 *            Author: wangyongyu
 *            Date: 2017/3/8
 */

public interface Func0<V> {
    void apply(@NonNull V v);
}
