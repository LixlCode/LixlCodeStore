/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.scenefw;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Base Card, 卡片基类定义
 * <p>
 * onCreate -> onShow -> onHide -> onDestroy
 */
public class Card extends FrameLayout {

    public Card(Context context) {
        super(context);
    }

    public Card(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Card(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onCreate() {
        SFLog.d(this.getClass().getSimpleName() + ":onCreate");
    }

    protected final void setContentView(int layout) {
        try {
            final View view = inflate(getContext(), layout, null);
            final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView(view, params);
        } catch (Exception e) {
            // exception
        }

    }

    protected final void setContentView(View view) {
        try {
            final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView(view, params);
        } catch (Exception e) {
            // exception
        }
    }
    //    声明自己的依赖
    //    Var<String> inputXXX();

    //    声明自己的输出
    //    Var<String> outputXXX();

}
