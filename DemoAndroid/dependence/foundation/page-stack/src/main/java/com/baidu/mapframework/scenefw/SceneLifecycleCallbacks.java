/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.scenefw;

import com.baidu.mapframework.scenefw.binding.Binder;

import android.os.Bundle;

/**
 * Scene Lifecycle callbacks.
 * <p>
 * 生命周期调用顺序：
 * <p>
 * onCreate -> onLoadData -> onReady -> onShow -> onHide -> onDestroy <br/>
 * ^         + <br/>
 * +---------+ <br/>
 * <br/>
 * <u>
 * <li> onCreate: 场景实例创建后的回调</li>
 * <li> onLoadData: 场景创建后，被压栈之前用于预加载数据的回调</li>
 * <li> onReload: 当前场景正在显示时，被调用replace到自己时回调</li>
 * <li> onReady: 绑定场景模板</li>
 * <li> onShow: 场景渲染</li>
 * <li> onHide: 场景界面隐藏</li>
 * <li> onDestroy: 销毁场景，回退时可被销毁</li>
 * </u>
 * </p>
 */
public interface SceneLifecycleCallbacks<T extends SceneTemplate> {

    void onCreate(Binder binder);

    void onLoadData(Bundle data);

    void onReload(Bundle data);

    void onReady();

    void onShow();

    void onShowComplete();

    void onResume();

    void onPause();

    void onHide();

    void onHideComplete();

    void onDestroy();
}
