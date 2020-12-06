/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.app.fpstack;

/**
 * DebugUI
 *
 * @author liguoqing
 * @version 1.0 2018/6/4
 */
public interface DebugUI {
    /**
     * 该函数进行 UI 调试的时候使用
     *
     * @param message
     */
    void updatePageName(CharSequence message);

    /**
     * 页面速度
     * @param velocity
     */
    void updatePageVelocity(CharSequence velocity);
}
