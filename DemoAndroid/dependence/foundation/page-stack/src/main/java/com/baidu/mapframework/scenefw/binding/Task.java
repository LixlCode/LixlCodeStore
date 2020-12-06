package com.baidu.mapframework.scenefw.binding;

public interface Task<T> {

    /**
     * 执行任务
     */
    void execute();

    /**
     * 取消任务
     */
    void cancel();

    /**
     * 获取任务状态 tracker
     */
    Tracker<T> getTracker();

    enum Stage {
        NOT_START, LOADING, SUCCESS, FAILED,
    }
}
