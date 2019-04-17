package com.baidu.mapframework.scenefw.binding.schedulers;

/**
 * Author: wangyongyu
 * Date: 2017/3/8
 */

public abstract class Scheduler {
    public abstract void scheduleDirect(Runnable run);
}
