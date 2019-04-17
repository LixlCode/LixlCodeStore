/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.scenefw;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.baidu.mapframework.scenefw.binding.Binder;

/**
 * Scene Register.
 * <p>
 * All scenes must be created by the SceneRegister.
 * </p>
 */
public class SceneRegister {

    private static HashMap<String, Class<? extends Scene>> sceneRegistry = new HashMap<>();

    /**
     * 注册场景类型
     *
     * @param sceneId    场景 ID
     * @param sceneClass 场景类型
     */
    public static void register(String sceneId, Class<? extends Scene> sceneClass) {
        sceneRegistry.put(sceneId, sceneClass);
    }

    private HashMap<String, Scene> sceneMap = new HashMap<>();

    /**
     * packaged
     * create scene instance
     */
    Scene createSceneInstance(String sceneId, String sceneTag) {
        String sceneKey = genSceneKey(sceneId, sceneTag);
        Scene s = sceneMap.get(sceneKey);
        if (s == null) {
            Class<?> clz = sceneRegistry.get(sceneId);
            if (clz == null) {
                throw new IllegalStateException(sceneId + " is not registered!");
            }
            try {
                // Create Scene instance
                Scene obj = (Scene) clz.newInstance();
                if (obj != null) {
                    obj.tag = sceneTag;
                    obj.id = sceneId;
                    // Set binder
                    Binder binder = new Binder();
                    obj.setBinder(binder);
                    obj.onCreate(binder);
                    sceneMap.put(sceneKey, obj);
                }
                return obj;
            } catch (InstantiationException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        } else {
            return s;
        }
    }

    void destroySceneInstance(Scene scene) {
        if (scene != null) {
            String sceneKey = genSceneKey(scene.id, scene.tag);
            sceneMap.remove(sceneKey);
        }
    }

    void clearSceneInstance(TransitionManager transitionManager) {
        List<Scene> scenes = new LinkedList<>(sceneMap.values());
        for (Scene scene : scenes) {
            scene.onDestroy();
            transitionManager.destroyTemplate(scene);
        }
    }

    private String genSceneKey(String sceneId, String sceneTag) {
        return "S:" + sceneId + "@" + sceneTag;
    }
}