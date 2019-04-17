/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.app.fpstack;

import com.baidu.mapframework.app.pagestack.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * Date: 14-6-23
 */
class DebugView extends LinearLayout {
    public TextView showPageNameView;
    public TextView showPageVelocityView;

    public DebugView(Context context) {
        super(context);
        initView();
    }

    public DebugView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        final View root = LayoutInflater.from(getContext()).inflate(R.layout.debug_setting, this);
        showPageNameView = root.findViewById(R.id.show_page_name);
        showPageVelocityView = root.findViewById(R.id.show_page_velocity);
    }

}
