/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.wecome.demo.listener;

import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.wecome.demo.constants.SettingConstant;

/**
 * Created by tianya on 2017/7/31.
 */

public class AlphaPressTouchListener implements View.OnTouchListener {

    private float alpha = 1f;

    public AlphaPressTouchListener(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int actionVal = event.getAction();
        if (actionVal == MotionEvent.ACTION_DOWN) {
            v.setAlpha(alpha);
        } else if (actionVal == MotionEvent.ACTION_UP || actionVal == MotionEvent.ACTION_CANCEL) {
            v.setAlpha(1f);
        }
        return false;
    }

    public static AlphaPressTouchListener texButton() {
        return new AlphaPressTouchListener(SettingConstant.PressAlphaTextVal);
    }

    public static AlphaPressTouchListener textIconButton() {
        return new AlphaPressTouchListener(SettingConstant.PressAlphaIconVal);
    }

    public static void enable(View view) {
        if (view == null) {
            return;
        }
        view.setOnTouchListener(new AlphaPressTouchListener(SettingConstant.PressAlphaIconVal));
    }

    public static void enable(View view, float alpha) {
        if (view == null) {
            return;
        }
        view.setOnTouchListener(new AlphaPressTouchListener(alpha));
    }

    // @BindingAdapter("alphaPress")
    public static void alphaPress(LinearLayout view, boolean need) {
        if (need) {
            AlphaPressTouchListener.enable(view);
        }
    }
}
