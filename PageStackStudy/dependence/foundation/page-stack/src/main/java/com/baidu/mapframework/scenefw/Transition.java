package com.baidu.mapframework.scenefw;

/**
 * Transition between scenes
 */
class Transition {

    /**
     * constructor
     *
     * @param incomingScene 进入的场景
     * @param outgoingScene 退场的场景
     */
    Transition(Scene incomingScene, Scene outgoingScene, boolean isReplace) {
        this.incomingScene = incomingScene;
        this.outgoingScene = outgoingScene;
        this.isReplace = isReplace;
    }

    final boolean isReplace;
    final Scene incomingScene;
    final Scene outgoingScene;


}
