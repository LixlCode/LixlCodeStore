/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.scenefw;

/**
 * 回退的 Transition 类型
 */
class BackTransition extends Transition {
    /**
     * constructor
     *
     * @param incomingScene 进入的场景
     * @param outgoingScene 退场的场景
     */
    BackTransition(Scene incomingScene, Scene outgoingScene) {
        super(incomingScene, outgoingScene, false);
    }
}
