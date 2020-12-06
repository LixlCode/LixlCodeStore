package com.baidu.mapframework.scenefw.binding.schedulers;

/**
 * Author: wangyongyu
 * Date: 2017/3/8
 */

public final class SchedulerFactory {
    static final Scheduler THREAD;

    static final Scheduler UI;

    static {
        THREAD = new ThreadScheduler();

        UI = new UIScheduler();
    }

    public static Scheduler thread() {
        return THREAD;
    }

    public static Scheduler main() {
        return UI;
    }
}
