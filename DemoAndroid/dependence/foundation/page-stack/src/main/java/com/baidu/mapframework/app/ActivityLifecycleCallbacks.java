package com.baidu.mapframework.app;

import android.app.Activity;
import android.os.Bundle;

/**
 * Acitivty生命周期回调
 *
 * @author liguoqing
 * @version 1.0
 * @date 13-7-2 上午9:32
 * @see {@link android.app.Application.ActivityLifecycleCallbacks} (Api level = 14)
 */
public interface ActivityLifecycleCallbacks {

    enum TYPE {

        CREATED,
        STARTED,
        RESUMED,
        PAUSED,
        STOPPED,
        SAVEINSTANCESTATE,
        DESTROYED,

    }

    void onActivityCreated(Activity activity, Bundle savedInstanceState);

    void onActivityStarted(Activity activity);

    void onActivityResumed(Activity activity);

    void onActivityPaused(Activity activity);

    void onActivityStopped(Activity activity);

    void onActivitySaveInstanceState(Activity activity, Bundle outState);

    void onActivityDestroyed(Activity activity);
}
