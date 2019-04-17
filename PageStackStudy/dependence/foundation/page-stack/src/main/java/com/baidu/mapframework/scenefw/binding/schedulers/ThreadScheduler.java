package com.baidu.mapframework.scenefw.binding.schedulers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import android.support.annotation.NonNull;

/**
 * Author: wangyongyu
 * Date: 2017/3/8
 */

public final class ThreadScheduler extends Scheduler {
    private final ScheduledExecutorService executor;

    private static final String THREAD_NAME_PREFIX = "ThreadScheduler";

    public ThreadScheduler() {
        this.executor = create(new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                Thread t = new Thread(runnable);
                t.setDaemon(true);
                t.setPriority(Thread.NORM_PRIORITY);
                return t;
            }
        });
    }

    /**
     * Creates a ScheduledExecutorService with the given factory.
     * @param factory the thread factory
     * @return the ScheduledExecutorService
     */
    public ScheduledExecutorService create(ThreadFactory factory) {
        return Executors.newScheduledThreadPool(1, factory);
    }

    @Override
    public void scheduleDirect(Runnable run) {
        executor.submit(run);
    }
}
