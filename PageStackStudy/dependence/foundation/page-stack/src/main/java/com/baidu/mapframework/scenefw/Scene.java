/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.scenefw;

import com.baidu.mapframework.app.map.LayerInterface;
import com.baidu.mapframework.app.map.LayerSwitcher;
//import com.baidu.mapframework.common.config.GlobalConfig;
//import com.baidu.mapframework.nirvana.NirvanaFramework; lxl
//import com.baidu.mapframework.nirvana.schedule.LifecycleManager;lxl
//import com.baidu.mapframework.nirvana.schedule.UITaskType;lxl
import com.baidu.mapframework.scenefw.binding.Binder;
//import com.baidu.mapframework.voice.sdk.model.VoiceResult;
//import com.baidu.mobstat.StatService;
//import com.baidu.platform.comapi.JNIInitializer;

import android.content.Intent;
import android.os.Bundle;

// import com.baidu.mapframework.voice.sdk.model.VoiceResult;

/**
 * Base Scene class
 */
public abstract class Scene<T extends SceneTemplate> implements SceneLifecycleCallbacks<T>, LayerInterface.Switcher {
    String id;
    String tag;
    private Binder binder;
    private T template;
    boolean isVisible = false;
    boolean isTouchable = false;
    boolean isPageBack = false;

    public String getId() {
        return id;
    }

    /**
     * 获取场景实例的 Tag
     *
     * @return
     */
    public String getTag() {
        return tag;
    }

    public String getSceneLogTag() {
        return "";
    }

    /* package */
    void setBinder(Binder binder) {
        this.binder = binder;
    }

    /**
     * 获取场景 Binder
     *
     * @return
     */
    public final Binder getBinder() {
        return this.binder;
    }

    public final boolean isBackFromPage() {
        return isPageBack;
    }

    /**
     * 当前 scene 是否是可见状态
     */
    public boolean isVisible() {
        return isVisible;
    }

    public final T getSceneTemplate() {
        return template;
    }

    /**
     * 设置场景绑定的模板类型
     */
    public abstract Class<T> getSceneTemplateClass();

    final void onBindTemplate(T template) {
        SFLog.d(this.getClass().getSimpleName() + ":onBindTemplate");
        this.template = template;
        onReady();
    }

    @Override
    public void onCreate(Binder binder) {
       /* NirvanaFramework.getLifecycleManager().onUIStateChange(
                UITaskType.UIType.SCENE,
                this.getClass().getName(),
                LifecycleManager.UIState.ACTIVE
        );*/

        SFLog.d(this.getClass().getSimpleName() + ":onCreate");
    }

    @Override
    public void onReady() {
        SFLog.d(this.getClass().getSimpleName() + ":onReady");
    }

    @Override
    public void onShow() {
      /*  NirvanaFramework.getLooperBuffer().startAnim();
        SFLog.d(this.getClass().getSimpleName() + ":onShow");
        isVisible = true;
        if (GlobalConfig.getInstance().isInitMtj()) {
            StatService.onPageStart(JNIInitializer.getCachedContext(), getSceneLogTag());
        }*/
    }

    @Override
    public void onShowComplete() {
        //NirvanaFramework.getLooperBuffer().stopAnim();
        SFLog.d(this.getClass().getSimpleName() + ":onShowComplete");
        binder.bind();
    }

    @Override
    public void onResume() {
        SFLog.d(this.getClass().getSimpleName() + ":onResume");
        isTouchable = true;
    }

    @Override
    public void onPause() {
        SFLog.d(this.getClass().getSimpleName() + ":onPause");
        isTouchable = false;
    }

    @Override
    public void onHide() {
        /*NirvanaFramework.getLooperBuffer().startAnim();
        SFLog.d(this.getClass().getSimpleName() + ":onHide");
        isVisible = false;
        binder.unbind();
        if (GlobalConfig.getInstance().isInitMtj()) {
            StatService.onPageEnd(JNIInitializer.getCachedContext(), getSceneLogTag());
        }*/
    }

    @Override
    public void onHideComplete() {
        //NirvanaFramework.getLooperBuffer().stopAnim();
        SFLog.d(this.getClass().getSimpleName() + ":onHideComplete");
    }

    @Override
    public void onReload(Bundle data) {
        SFLog.d(this.getClass().getSimpleName() + ":onReload");
    }

    @Override
    public void onDestroy() {
        SFLog.d(this.getClass().getSimpleName() + ":onDestroy");
        binder.destroy();
       /* NirvanaFramework.getLifecycleManager().onUIStateChange(
                UITaskType.UIType.SCENE,
                this.getClass().getName(),
                LifecycleManager.UIState.DESTROYED
        );*/
    }

    public boolean onBackPressed() {
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public String toString() {
        return String.format("<%s|%s|%s>", getClass().getName(), id, tag);
    }

    @Override
    public LayerSwitcher layerSwitcher() {
        return null;
    }

    public String getPageTag() {
        return "";
    }

    public String infoToUpload() {
        return "";
    }

    //public void handleVoiceResult(VoiceResult voiceResult) {

    //}

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }
}
