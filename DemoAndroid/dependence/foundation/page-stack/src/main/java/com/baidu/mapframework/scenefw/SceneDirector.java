/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.scenefw;

import java.util.Stack;

import com.baidu.mapframework.app.map.LayerInterface;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

// import com.baidu.mapframework.app.map.LayerControl;

/**
 * 场景调度管理器
 * <p>
 * 负责整个场景系统的核心调度逻辑，管理场景（Scene）、布局（SceneTemplate）和卡片（Card）<br/>
 * SceneDirector 在整个框架中是一个单例, 在 UI 线程中调用，通过 {@link #getDirectorInstance()} 获取 SceneDirector 的实例对象。
 * 场景的切换提供以下方式：
 * <u>
 * <li>{@link #pushScene(String, String, Bundle)}: 把具有 SceneId 和 Tag 属性的场景压入当前栈</li>
 * <li>{@link #replaceScene(String, String, Bundle)}: 用具有 SceneId 和 Tag 属性的场景替换当前场景 </li>
 * <li>{@link #popScene()}: 当前场景出栈</li>
 * <li>{@link #popToRootScene()}: 回退到根场景</li>
 * </u>
 * 使用该 API 之前，必须通过 {@link SceneRegister#register(String, Class)} 方法，注册可以使用的场景类型。<br/>
 * 场景的生命周期定义见：{@link SceneLifecycleCallbacks}
 */
public final class SceneDirector {

    private SceneRegister sceneRegister = new SceneRegister();
    private SceneContainer container;
    private Scene currentScene;
    private Stack<Scene> scenesStack = new Stack<>();
    private TransitionManager transitionManager = new TransitionManager(sceneRegister);
    private LayerInterface.LayerTransition layerTransition;

    private SceneDirector() {
        super();
    }

    public static SceneDirector getDirectorInstance() {
        return HOLDER.INSTANCE;
    }

    /**
     * 获取场景容器 ViewGroup
     */
    ViewGroup getSceneContainer() {
        return this.container.getContainerView();
    }

    /**
     * 设置场景容器 ViewGroup
     */
    void setSceneContainer(SceneContainer container) {
        this.container = container;
    }

    /**
     * 图层控制
     * @param layerTransition
     */
    public void setLayerTransition(LayerInterface.LayerTransition layerTransition) {
        this.layerTransition = layerTransition;
    }

    public LayerInterface.LayerTransition getLayerTransition() {
        return layerTransition;
    }

    /**
     * Id 为 sceneId 的场景入栈
     *
     * @param sceneId  场景的 Id， 标识场景类型
     * @param sceneTag 场景的 Tag，区分场景实例
     * @param params   设置的场景参数
     */
    public void pushScene(String sceneId, String sceneTag, Bundle params) {
        SFLog.d("Director::pushScene(" + sceneId + "," + sceneTag + ")");
        Scene s = sceneRegister.createSceneInstance(sceneId, sceneTag);
        doPushScene(s, params);
    }

    /**
     * Id 为 sceneId 的场景入栈, 默认 tag 是 ""
     *
     * @param sceneId 场景的 Id， 标识场景类型
     * @param params
     */
    public void pushScene(String sceneId, Bundle params) {
        pushScene(sceneId, "", params);
    }

    /**
     * Id 为 sceneId 的场景替换当前场景, 默认 tag 是 ""
     *
     * @param sceneId 场景的 Id， 标识场景类型
     * @param params
     */
    public void replaceScene(String sceneId, Bundle params) {
        replaceScene(sceneId, "", params);
    }

    /**
     * 替换当前场景, 被替换的场景会执行 {@link Scene#onDestroy()}
     *
     * @param sceneId  场景的 Id， 标识场景类型
     * @param sceneTag 场景的 Tag，区分场景实例
     * @param params   设置的场景参数
     */
    public void replaceScene(String sceneId, String sceneTag, Bundle params) {
        SFLog.d("SceneDirector::replaceScene(" + sceneId + "," + sceneTag + ")");

        if (currentScene == null) {
            return;
        }
        Scene inComing = sceneRegister.createSceneInstance(sceneId, sceneTag);
        doReplaceScene(inComing, params);
    }

    /**
     * 当前场景页面，被其他页面覆盖，处理 currentScene 生命周期
     */
    void storeScene() {
        SFLog.d("SceneDirector::storeScene(currentScene: " + currentScene + ")");
        if (currentScene != null) {
            if (currentScene.isTouchable) {
                currentScene.onPause();
            }
            currentScene.onHide();
            currentScene.onHideComplete();
            if (layerTransition != null) {
                layerTransition.onLayerTransition(currentScene, null);
            }
        } else {
            // 页面back退出，不做处理
        }
    }

    /**
     * 当前场景页面，从其他场景 back 回来，处理 currentScene 生命周期
     */
    void restoreScene(Bundle params) {
        SFLog.d("SceneDirector::restoreScene(currentScene: " + currentScene + ")");
        if (currentScene != null) {
            currentScene.isPageBack = true;
            currentScene.onLoadData(params);
            currentScene.onShow();
            currentScene.onShowComplete();
            currentScene.onResume();
        } else {
            SFLog.e("页面被 restore，currentScene 为 null, 场景栈逻辑错误");
        }
    }

    /**
     * 当前场景页面，从其他场景 back 回来，跳转到指定场景, 默认 tag 是 ""
     */
    void restoreToScene(String sceneId, Bundle params) {
        restoreToScene(sceneId, "", params);
    }

    /**
     * 当前场景页面，从其他场景 back 回来，跳转到指定场景
     *
     * @param sceneId  场景的 Id， 标识场景类型
     * @param sceneTag 场景的 Tag，区分场景实例
     * @param params   设置的场景参数
     */
    void restoreToScene(String sceneId, String sceneTag, Bundle params) {
        SFLog.d("SceneDirector::restoreToScene(currentScene: " + currentScene + sceneId + "," + sceneTag + ")");

        Scene top = null;
        if (scenesStack.size() > 0) {
            top = scenesStack.pop();
        }

        Scene s = sceneRegister.createSceneInstance(sceneId, sceneTag);
        scenesStack.push(s);
        s.isPageBack = true;
        s.onLoadData(params);
        Transition transition = new Transition(s, top, true);

        currentScene = s;

        // do Transition
        Scene incomingScene = transition.incomingScene;
        Scene outGoingScene = transition.outgoingScene;
        SceneTemplate inComingTemp = null;

        if (incomingScene.getSceneTemplate() != null) {
            inComingTemp = incomingScene.getSceneTemplate();
        } else {
            inComingTemp = transitionManager.inflateTemplate(transition);
        }
        if (inComingTemp == null) {
            return;
        }
        if (top != null && top.getSceneTemplate() != null) {
            top.getSceneTemplate().onHide();
            if (inComingTemp != outGoingScene.getSceneTemplate()) {
                top.getSceneTemplate().onExit(); //restore的场景，都是被store过的，所以直接退场即可
            }
        }

        inComingTemp.onBindScene(transition.incomingScene);
        if (incomingScene.getSceneTemplate() == null) {
            transition.incomingScene.onBindTemplate(inComingTemp);
        }

        if (outGoingScene == null || inComingTemp != outGoingScene.getSceneTemplate()) {
            inComingTemp.onEnter();
        }

        transition.incomingScene.onShow();
        transition.incomingScene.onResume();
        inComingTemp.onShow();
        transition.incomingScene.onShowComplete();
    }

    /**
     * 顶层场景出栈, 出栈的场景会执行 {@link Scene#onDestroy()}
     */
    public boolean popScene() {
        int size = scenesStack.size();
        if (size == 0) {
            return false;
        }
        final Scene top = scenesStack.pop();
        top.isPageBack = false;
        if (scenesStack.size() == 0) {
            // 当前栈中已没有场景
            currentScene = null;
            final SceneTemplate template = top.getSceneTemplate();
            if (template != null) {
                if (template.animatorSet != null) {
                    template.animatorSet.end();
                    template.animatorSet = null;
                }
                template.onHide();
                if (top.isVisible()) {
                    top.onPause();
                    top.onHide();
                }

                AnimatorSet stopAnim = template.createHideAnim();
                if (stopAnim != null) {
                    stopAnim.addListener(new AnimatorListenerAdapter() {
                        boolean enterCalled = false;

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (!enterCalled) {
                                enterCalled = true;
                                goBackAnimEnd(template, top);
                            }
                        }
                    });
                    stopAnim.start();
                    return true;
                } else {
                    template.onExit();
                    top.onDestroy();
                    transitionManager.destroyTemplate(top);
                    if (layerTransition != null) {
                        layerTransition.onLayerTransition(top, null);
                    }
                    sceneRegister.clearSceneInstance(transitionManager);
                }
            }
            return false;
        } else {
            currentScene = scenesStack.peek();
        }
        BackTransition transition = new BackTransition(currentScene, top);
        transitionManager.commit(transition, null);
        return true;
    }

    private void goBackAnimEnd(SceneTemplate template, Scene top) {
        top.onHideComplete();
        template.onExit();
        top.onDestroy();
        transitionManager.destroyTemplate(top);
        if (layerTransition != null) {
            layerTransition.onLayerTransition(top, null);
        }
        sceneRegister.clearSceneInstance(transitionManager);
        container.goBack();
    }

    public void goBack() {
        if (!popScene()) {
            container.goBack();
        }
    }

    /**
     * 获取当前场景类型
     */
    public Class<? extends Scene> getCurrentSceneType() {
        if (currentScene != null) {
            return currentScene.getClass();
        } else {
            return null;
        }
    }

    /**
     * 当 ScenePage 被 TaskManager removeStackRecord 接口手动移除时 （open api）
     * 当 ScenePage 被 TaskManager getHistoryRecords 后，操作栈顺序移除时 （导航）
     * <p>
     * 需要重置当前场景栈顺序
     */
    public void resetDirector() {
        if (currentScene != null) {
            if (currentScene.isTouchable) {
                currentScene.onPause();
            }

            if (currentScene.isVisible) {
                currentScene.onHide();
                currentScene.onHideComplete();
            }
            if (currentScene.getSceneTemplate() != null) {
                currentScene.getSceneTemplate().onExit();
            }
            currentScene.onDestroy();
            transitionManager.destroyTemplate(currentScene);
        }

        scenesStack.clear();
        sceneRegister.clearSceneInstance(transitionManager);
        currentScene = null;
    }

    private void doPushScene(Scene s, Bundle params) {
        scenesStack.push(s);
        s.isPageBack = false;
        s.onLoadData(params);
        Transition transition = new Transition(s, currentScene, false);
        currentScene = s;
        transitionManager.commit(transition, params);
    }

    private void doReplaceScene(Scene s, Bundle params) {
        if (currentScene == null) {
            return;
        }

        if (currentScene == s) {
            currentScene.onReload(params);
            return;
        }

        Scene top = scenesStack.pop();
        s.isPageBack = false;
        scenesStack.push(s);
        s.onLoadData(params);
        Transition transition = new Transition(s, top, true);
        currentScene = s;
        transitionManager.commit(transition, params);
    }

    boolean onBackPressed() {
        if (currentScene != null && currentScene.isVisible()) {
            return currentScene.onBackPressed();
        } else {
            return false;
        }
    }

    void onResume() {
        if (currentScene != null && !currentScene.isTouchable) {
            currentScene.onResume();
        }
    }

    void onPause() {
        if (currentScene != null && currentScene.isTouchable) {
            currentScene.onPause();
        }
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (currentScene != null && currentScene.isVisible) {
            currentScene.onActivityResult(requestCode, resultCode, data);
        }
    }

    public String dump() {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        if (scenesStack == null) {
            return "";
        }
        for (Scene r : scenesStack) {
            if (r == null) {
                continue;
            }
            sb.append("#").append(index).append(":").append(r.toString());
            index++;
        }
        return sb.toString();
    }

    public void clean() {
        scenesStack.clear();
    }

    private static final class HOLDER {
        private static final SceneDirector INSTANCE = new SceneDirector();
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * 销毁顶层scene
     *
     * @return
     */
    @Deprecated
    public boolean finishTopScene() {
        int size = scenesStack.size();
        if (size == 0) {
            return false;
        }

        Scene topScene = scenesStack.pop();
        if (scenesStack.size() != 0) {
            currentScene = scenesStack.peek();
        } else {
            currentScene = null;
        }
        if (topScene != null) {
            if (currentScene != null) {
                // 此时不可见
                final SceneTemplate templateInComing = currentScene.getSceneTemplate();
                if (templateInComing != null) {
                    templateInComing.onEnter();
                    AnimatorSet startAnim = templateInComing.createShowAnim();
                    if (startAnim != null) {
                        startAnim.addListener(new AnimatorListenerAdapter() {

                            @Override
                            public void onAnimationEnd(Animator animation) {

                            }
                        });
                        startAnim.start();
                    }
                }
            }
            final SceneTemplate topTemplate = topScene.getSceneTemplate();
            if (topTemplate == null) {
                return false;
            }
            topTemplate.onExit();
            topScene.onPause();
            topScene.onHide();
            topScene.onHideComplete();
            topScene.onDestroy();
            transitionManager.destroyTemplate(topScene);
            return true;
        }
        return false;
    }

    /**
     * 导航使用
     *
     * @param bundle
     *
     * @return
     */
    @Deprecated
    public boolean backScene(Bundle bundle) {
        boolean finishTopSceneRet = finishTopScene();
        SFLog.d("SceneDirector::backScene(finishTopSceneRet = " + finishTopSceneRet + ")");
        restoreScene(bundle);
        return true;
    }

}
