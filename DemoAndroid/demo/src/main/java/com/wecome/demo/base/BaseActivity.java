package com.wecome.demo.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wecome.demo.R;

import java.lang.reflect.Method;

public class BaseActivity extends AppCompatActivity {
    /**
     * 通用的ToolBar标题
     */
    private TextView commonTitleTv;
    /**
     * 通用的ToolBar
     */
    private Toolbar commonTitleTb;
    /**
     * 内容区域
     */
    private RelativeLayout content;
    /**
     * 控制menu的显示,默认不显示
     */
    private boolean isShowMeun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initView();
        setSupportActionBar(commonTitleTb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initView() {
        commonTitleTv = (TextView) findViewById(R.id.common_title_tv);
        commonTitleTb = (Toolbar) findViewById(R.id.common_title_tb);
        content = (RelativeLayout) findViewById(R.id.content);
    }


    /**
     * 子类调用，重新设置Toolbar
     *
     * @param layout
     */
    public void setToolBar(int layout) {
        hidetoolBar();
        commonTitleTb = (Toolbar) content.findViewById(layout);
        setSupportActionBar(commonTitleTb);
        //设置actionBar的标题是否显示，对应ActionBar.DISPLAY_SHOW_TITLE。
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * 隐藏ToolBar，通过setToolBar重新定制ToolBar
     */
    public void hidetoolBar() {
        commonTitleTb.setVisibility(View.GONE);
    }

    /**
     * 设置menu
     *
     * @param
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isShowMeun) {
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }
        return false;
    }

    /**
     * 设置menu中的图标和文字同时显示
     */
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //使菜单上图标可见
        if (isShowMeun && menu != null && menu instanceof MenuBuilder) {
            //编sdk版本24的情况 可以直接使用 setOptionalIconsVisible
            if (Build.VERSION.SDK_INT > 23) {
                MenuBuilder builder = (MenuBuilder) menu;
                builder.setOptionalIconsVisible(true);
            } else {
                //sdk版本24的以下，需要通过反射去执行该方法
                try {
                    MenuBuilder builder = (MenuBuilder) menu;
                    Method m = builder.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * 控制是否显示menu菜单
     */
    public void setMenuShow(Boolean isShowMeun){
        this.isShowMeun = isShowMeun;
    }

    /**
     * menu的点击事件
     *
     * @param onclick
     */
    public void setToolBarMenuOnclick(Toolbar.OnMenuItemClickListener onclick) {
        commonTitleTb.setOnMenuItemClickListener(onclick);
    }


    /**
     * 设置左上角back按钮
     */
    public void setBackArrow() {
        final Drawable upArrow = getResources().getDrawable(R.mipmap.icon_back);
        //给ToolBar设置左侧的图标
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
        // 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回按钮的点击事件
        commonTitleTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 设置toolbar下面内容区域的内容
     *
     * @param layoutId
     */
    public void setContentLayout(int layoutId) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View contentView = inflater.inflate(layoutId, null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            content.addView(contentView, params);
        }
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            commonTitleTv.setText(title);
        }
    }

    /**
     * 设置标题
     *
     * @param resId
     */
    public void setTitle(int resId) {
        commonTitleTv.setText(resId);
    }

    /**
     * 设置标题字体的样式
     */
    public void setTitleStyle (int colorId, int textSize){
        commonTitleTv.setTextColor(getResources().getColor(colorId));
        commonTitleTv.setTextSize(textSize);
    }

}
