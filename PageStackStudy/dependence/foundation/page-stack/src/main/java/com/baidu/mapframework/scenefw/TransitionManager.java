/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.scenefw;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.baidu.mapframework.app.map.LayerInterface;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Bundle;

/**
 * 转场管理器
 */
class TransitionManager {

    private SceneRegister register;

    public TransitionManager(SceneRegister register) {
        this.register = register;
    }

    private Map<SceneTemplate, HashSet<Scene>> templateSceneMap = new HashMap<>();

    public boolean commit(final Transition transition, Bundle params) {
        Scene incomingScene = transition.incomingScene;
        Scene outGoingScene = transition.outgoingScene;
        SceneTemplate inComingTemp = null;
        SceneTemplate outGoingTemp = null;

        if (incomingScene == outGoingScene) {
            return false;
        }

        if (outGoingScene != null && outGoingScene.getSceneTemplate() == null) {
            return false;
        }

        if (incomingScene != null) {
            if (incomingScene.getSceneTemplate() != null) {
                inComingTemp = incomingScene.getSceneTemplate();
            } else {
                inComingTemp = inflateTemplate(transition);
            }
            if (inComingTemp == null) {
                return false;
            }
        } else {
            return false;
        }

        final SceneTemplate tmpInTemplate = inComingTemp;
        if (outGoingScene != null) {
            outGoingTemp = outGoingScene.getSceneTemplate();
            if (outGoingTemp == inComingTemp) {
                outGoingScene.onPause();
                outGoingScene.onHide();
                outGoingScene.onHideComplete();
                if (transition instanceof BackTransition) {
                    outGoingScene.onDestroy();
                    destroyScene(outGoingScene);
                }
                inComingTemp.onBindScene(incomingScene);
                if (incomingScene.getSceneTemplate() == null) {
                    incomingScene.onBindTemplate(inComingTemp);
                }
                LayerInterface.LayerTransition layerTransition = SceneDirector.getDirectorInstance()
                        .getLayerTransition();
                if (layerTransition != null) {
                    layerTransition.onLayerTransition(outGoingScene, incomingScene);
                }
                incomingScene.onShow();
                incomingScene.onResume();
                inComingTemp.onShow();
                inComingTemp.onNewShow();
                AnimatorSet showAnim = inComingTemp.createShowAnim();
                if (showAnim != null) {
                    inComingTemp.animatorSet = showAnim;
                    showAnim.addListener(new AnimatorListenerAdapter() {
                        boolean enterCalled = false;

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (!enterCalled) {
                                enterCalled = true;
                                transition.incomingScene.getSceneTemplate().animatorSet = null;
                                transition.incomingScene.onShowComplete();
                            }
                        }
                    });
                    showAnim.start();
                } else {
                    incomingScene.onShowComplete();
                }
                return true;
            } else {
                if (outGoingTemp.animatorSet != null) {
                    outGoingTemp.animatorSet.end();
                    outGoingTemp.animatorSet = null;
                }
                outGoingScene.onPause();
                outGoingScene.onHide();
                outGoingTemp.onHide();
                AnimatorSet stopAnim = outGoingTemp.createHideAnim();
                if (stopAnim != null) {
                    stopAnim.addListener(new AnimatorListenerAdapter() {
                        boolean enterCalled = false;

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (!enterCalled) {
                                enterCalled = true;
                                sceneEnter(transition, tmpInTemplate);
                            }
                        }
                    });
                    stopAnim.start();
                } else {
                    sceneEnter(transition, tmpInTemplate);
                }
            }
        } else {
            sceneEnter(transition, tmpInTemplate);
        }

        return true;

    }

    void sceneEnter(final Transition transition, SceneTemplate template) {
        SceneTemplate inComingTemp = transition.incomingScene.getSceneTemplate();
        template.onBindScene(transition.incomingScene);
        if (inComingTemp == null) {
            transition.incomingScene.onBindTemplate(template);
            inComingTemp = template;
        }

        if (transition.outgoingScene != null) {
            transition.outgoingScene.onHideComplete();
            transition.outgoingScene.getSceneTemplate().onExit();
            if (transition instanceof BackTransition) {
                transition.outgoingScene.onDestroy();
                if (transition.outgoingScene.getSceneTemplate() != transition.incomingScene.getSceneTemplate()) {
                    destroyTemplate(transition.outgoingScene);
                } else {
                    destroyScene(transition.outgoingScene);
                }
            }
        }

        inComingTemp.onEnter();
        LayerInterface.LayerTransition layerTransition = SceneDirector.getDirectorInstance()
                .getLayerTransition();
        if (layerTransition != null) {
            layerTransition.onLayerTransition(transition.outgoingScene, transition.incomingScene);
        }
        transition.incomingScene.onShow();
        transition.incomingScene.onResume();
        inComingTemp.onShow();
        startAnim(transition);
    }

    void startAnim(final Transition transition) {
        AnimatorSet startAnim = transition.incomingScene.getSceneTemplate().createShowAnim();
        if (startAnim != null) {
            transition.incomingScene.getSceneTemplate().animatorSet = startAnim;
            startAnim.addListener(new AnimatorListenerAdapter() {
                boolean enterCalled = false;

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!enterCalled) {
                        transition.incomingScene.getSceneTemplate().animatorSet = null;
                        transition.incomingScene.onShowComplete();
                    }
                }
            });
            startAnim.start();
        } else {
            transition.incomingScene.onShowComplete();
        }
    }

    SceneTemplate inflateTemplate(Transition transition) {
        try {
            Scene inComing = transition.incomingScene;
            Scene outGoing = transition.outgoingScene;
            SceneTemplate outGoingTemp = null;
            Class<? extends SceneTemplate> templateClass = inComing.getSceneTemplateClass();

            if (outGoing != null) {
                outGoingTemp = outGoing.getSceneTemplate();
            } else {
                return inflate(templateClass, inComing);
            }

            if (inComing.getSceneTemplateClass() == outGoing.getSceneTemplateClass()) {
                if (inComing.getTag().equals(outGoing.getTag())) {
                    templateSceneMap.get(outGoingTemp).add(inComing);
                    return outGoingTemp;
                } else {
                    return inflate(templateClass, inComing);
                }
            } else {
                return inflate(templateClass, inComing);
            }

        } catch (Exception e) {
            return null;
        }
    }

    private SceneTemplate inflate(Class<? extends SceneTemplate> templateClass, Scene scene) {
        try {
            SceneTemplate template = templateClass.newInstance();
            template.inflate();

            HashSet<Scene> scenes = new HashSet<>();
            scenes.add(scene);
            templateSceneMap.put(template, scenes);
            return template;
        } catch (Exception e) {
            return null;
        }
    }

    public void destroyTemplate(Scene scene) {
        // 调整销毁Template和Scene的调用顺序，先Scene后Template
        // 因为销毁Template时，会判断"scenes.size()==0",而scenes.remove操作在销毁Scene做的
        // 所以如果先Template判断的话，那么scenes至少有一个当前元素，而导致Template永远无法 destroy

        if (scene == null) {
            return;
        }
        destroyScene(scene); // destroyTemplate时, 同时销毁相应 Scene, 这个是原来逻辑，此处保留。

        SceneTemplate template = scene.getSceneTemplate();
        if (template != null) {
            HashSet<Scene> scenes = templateSceneMap.get(template);
            if (scenes != null && scenes.size() == 0) {
                template.onDestroy();
                // 当destroyTemplate时，移除key。不移除会造成 template 永久保存而出现内存泄漏。
                templateSceneMap.remove(template);
            }
        }
    }

    public void destroyScene(Scene scene) {
        SceneTemplate template = scene.getSceneTemplate();
        if (template != null) {
            HashSet<Scene> scenes = templateSceneMap.get(template);
            if (scenes != null) {
                scenes.remove(scene);
            }
            template.onUnBindScene(scene);
        }
        if (scene != null && register != null) {
            register.destroySceneInstance(scene);
        }
    }

}
