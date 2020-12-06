/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.scenefw.binding;

import com.baidu.mapframework.scenefw.binding.Task.Stage;

public final class Tracker<T> {

    private Stage state = Stage.NOT_START;
    private Callback callback;
    private T result;
    private Exception exception;

    public synchronized void setLoading() {
        state = Stage.LOADING;
        notifyCallback();
    }

    public synchronized void setSuccess(T t) {
        state = Stage.SUCCESS;
        result = t;
        notifyCallback();
    }

    public synchronized void setFailed(Exception e) {
        state = Stage.FAILED;
        exception = e;
        notifyCallback();
    }

    synchronized void setStageCallback(Callback callback) {
        this.callback = callback;
    }

    synchronized T getResult() {
        return result;
    }

    synchronized Exception getException() {
        return exception;
    }

    synchronized Stage getStage() {
        return state;
    }

    private void notifyCallback() {
        if (callback != null) {
            callback.onState(state);
        }
    }

    public interface Callback {
        void onState(Stage stage);
    }

    @Override
    public String toString() {
        return "Tracker{" + "state=" + state + ", callback=" + callback + ", result=" + result + '}';
    }
}