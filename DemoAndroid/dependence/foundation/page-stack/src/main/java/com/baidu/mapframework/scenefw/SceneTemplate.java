/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.scenefw;

import com.baidu.mapframework.app.fpstack.TaskManagerFactory;

import android.animation.AnimatorSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public abstract class SceneTemplate {

    private ViewGroup viewGroup;
    AnimatorSet animatorSet = null;

    public final ViewGroup getViewGroup() {
        return viewGroup;
    }

    final void inflate() {
        if (viewGroup == null) {
            viewGroup = (ViewGroup) LayoutInflater
                    .from(TaskManagerFactory.getTaskManager().getContainerActivity())
                    .inflate(getLayoutId(), null);
            onCreate();
        }
    }

    public abstract int getLayoutId();

    void onEnter() {
        ViewGroup root = SceneDirector.getDirectorInstance()
                .getSceneContainer();
        if (viewGroup != null && viewGroup.getParent() == null) {
            root.addView(viewGroup);
        }
        onEntered();
    }

    /**
     * 进场动画
     */
    public AnimatorSet createShowAnim() {
        return null;
    }

    public abstract void onCreate();

    public abstract void onBindScene(Scene scene);

    public abstract void onUnBindScene(Scene scene);

    public void onEntered() {

    }

    public abstract void onShow();

    public abstract void onNewShow();

    /**
     * 退场动画
     */
    public AnimatorSet createHideAnim() {
        return null;
    }

    public abstract void onHide();

    void onExit() {
        ViewGroup root = SceneDirector.getDirectorInstance()
                .getSceneContainer();
        if (viewGroup != null && viewGroup.getParent() == root) {
            root.removeView(viewGroup);
        }
        onExited();
    }

    public void onExited() {

    }

    /**
     * 模板销毁
     */
    public abstract void onDestroy();

}
