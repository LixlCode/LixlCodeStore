package com.baidu.mapframework.scenefw.binding.schedulers;

import android.os.Handler;
import android.os.Looper;

/**
 * Author: wangyongyu
 * Date: 2017/3/8
 */

public final class UIScheduler extends Scheduler {
    private final Handler handler;

    public UIScheduler() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void scheduleDirect(Runnable run) {
        handler.post(run);
    }
}
