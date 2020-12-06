package com.wecome.demo.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wecome.demo.R;

public class SimpleToolbar extends Toolbar {

    /**
     * 左侧Title
     */
    private TextView mTxtLeftTitle;
    /**
     * 中间Title
     */
    private TextView mTxtMiddleTitle;
    /**
     * 右侧Title
     */
    private TextView mTxtRightTitle;
    /**
     * 左侧app name
     */
    private TextView tv_name;

    public SimpleToolbar(Context context) {
        this(context, null);
    }

    public SimpleToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTxtLeftTitle = (TextView) findViewById(R.id.txt_left_title);
        tv_name = (TextView) findViewById(R.id.tv_name);
        mTxtMiddleTitle = (TextView) findViewById(R.id.txt_main_title);
        mTxtRightTitle = (TextView) findViewById(R.id.txt_right_title);
    }

    //设置中间title的内容
    public void setMainTitle(String text) {
        this.setTitle(" ");
        mTxtMiddleTitle.setVisibility(View.VISIBLE);
        mTxtMiddleTitle.setText(text);
    }

    //设置中间title的内容文字的颜色
    public void setMainTitleColor(int color) {
        mTxtMiddleTitle.setTextColor(color);
    }

    //设置title左边文字
    public void setLeftTitleText(String text) {
        mTxtLeftTitle.setVisibility(View.VISIBLE);
        mTxtLeftTitle.setText(text);
    }

    //设置title左边文字颜色
    public void setLeftTitleColor(int color) {
        mTxtLeftTitle.setTextColor(color);
    }

    //设置title左边图标
    public void setLeftTitleDrawable(int res) {
        Drawable dwLeft = ContextCompat.getDrawable(getContext(), res);
        if (dwLeft != null){
        dwLeft.setBounds(0, 0, dwLeft.getMinimumWidth(), dwLeft.getMinimumHeight());
        }
        mTxtLeftTitle.setCompoundDrawables(dwLeft, null, null, null);
    }

    //设置title左边点击事件
    public void setLeftTitleClickListener(OnClickListener onClickListener) {
        mTxtLeftTitle.setOnClickListener(onClickListener);
    }

    // 展示左侧返回按钮
    public void setLeftshow(){
        mTxtLeftTitle.setVisibility(VISIBLE);
    }

    // 隐藏左侧的appName
    public void hideLeftAppName(){
        tv_name.setVisibility(GONE);
    }

    //设置title右边文字
    public void setRightTitleText(String text) {
        mTxtRightTitle.setVisibility(View.VISIBLE);
        mTxtRightTitle.setText(text);
    }

    //设置title右边文字颜色
    public void setRightTitleColor(int color) {
        mTxtRightTitle.setTextColor(color);
    }

    //设置title右边图标
    public void setRightTitleDrawable(int res) {
        Drawable dwRight = ContextCompat.getDrawable(getContext(), res);
        if (dwRight != null) {
        dwRight.setBounds(0, 0, dwRight.getMinimumWidth(), dwRight.getMinimumHeight());
        }
        mTxtRightTitle.setCompoundDrawables(null, null, dwRight, null);
    }

    //设置title右边点击事件
    public void setRightTitleClickListener(OnClickListener onClickListener) {
        mTxtRightTitle.setOnClickListener(onClickListener);
    }

    // 隐藏右侧按钮
    public void setRightHide(){
        mTxtRightTitle.setVisibility(GONE);
    }

}
