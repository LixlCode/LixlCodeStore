/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.scenefw.binding;

import java.util.LinkedList;

/**
 * 网络及异步任务，使用该类同步状态
 * <p>
 * 如：Scene 触发网络请求
 * TaskVar<T> carTask = RouteTaskFactory.createCarRouteTask()
 * <p>
 * card.setCarTask(binder.newConnect(carTask)) 传给卡片使用
 * binder 使用 binder可以使页面在 onShow时候，同步网络结构，onHide时，不同步网络结果
 */
public class TaskVar<T> extends Var<T> {

    private Task<T> task;
    private LinkedList<TaskStageCallback<T>> callbacks = new LinkedList<>();

    public TaskVar() {
        this.task = new Binder.StubTask<>();
        this.task.getTracker().setStageCallback(trackerCallback);
    }

    public void setTask(Task<T> task) {
        synchronized (this) {
            if (this.task != null) {
                this.task.getTracker().setStageCallback(null);
            }

            if (task != null) {
                task.getTracker().setStageCallback(trackerCallback);
            }

            this.task = task;
        }

        notifyCallback(this.task.getTracker().getStage());
    }

    public synchronized Task<T> getTask() {
        return task;
    }

    public synchronized Task.Stage getStage() {
        if (task != null) {
            return task.getTracker().getStage();
        } else {
            return Task.Stage.NOT_START;
        }
    }

    public synchronized void subscribeTask(TaskStageCallback<T> callback) {
        callbacks.add(callback);
    }

    public synchronized void unSubscribeTask(TaskStageCallback<T> callback) {
        callbacks.remove(callback);
    }

    public interface TaskStageCallback<T> {
        void onNotStart();

        void onLoading();

        void onSuccess(T t);

        void onFailed(Exception e);
    }

    private Tracker.Callback trackerCallback = new Tracker.Callback() {
        @Override
        public void onState(Task.Stage stage) {
            notifyCallback(stage);
        }
    };

    private void notifyCallback(Task.Stage stage) {
        LinkedList<TaskStageCallback<T>> currentCallbacks;
        synchronized (this) {
            currentCallbacks = new LinkedList<>(callbacks);
        }

        if (stage == Task.Stage.SUCCESS) {
            set(task.getTracker().getResult());
            for (TaskStageCallback<T> callback : currentCallbacks) {
                callback.onSuccess(task.getTracker().getResult());
            }
        }

        if (stage == Task.Stage.FAILED) {
            for (TaskStageCallback<T> callback : currentCallbacks) {
                callback.onFailed(task.getTracker().getException());
            }
        }

        if (stage == Task.Stage.LOADING) {
            for (TaskStageCallback<T> callback : currentCallbacks) {
                callback.onLoading();
            }
        }

        if (stage == Task.Stage.NOT_START) {
            for (TaskStageCallback<T> callback : currentCallbacks) {
                callback.onNotStart();
            }
        }
    }

    @Override
    public String toString() {
        return "TaskVar{" + "task=" + task + '}';
    }
}
