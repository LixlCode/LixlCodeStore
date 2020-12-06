package com.wecome.demo.utils;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class FullScreenUtil {

    private static volatile FullScreenUtil mFullScreenUtil;

    private FullScreenUtil() {}

    public static FullScreenUtil getInstance() {
        if (mFullScreenUtil == null) {
            synchronized (FullScreenUtil.class) {
                if (mFullScreenUtil == null) {
                    mFullScreenUtil = new FullScreenUtil();
                }
            }
        }
        return mFullScreenUtil;
    }

    /**
     * 提供设置页面全屏显示的方法
     * @param window
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void setFullScreenUtil(Window window) {
        // 设置状态栏为透明状态
        setStatusBarTransparent(window);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 适配全面屏
            setFullScreenWindowLayout(window);
        }
    }

    /**
     * 提供设置页面状态栏透明的方法
     * @param window
     */
    public void setBarTransparent(Window window) {
        setStatusBarTransparent(window);
    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.P)
    private static void setFullScreenWindowLayout(Window window) {
        window.getDecorView().setSystemUiVisibility(View
                .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        //设置页面全屏显示
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.layoutInDisplayCutoutMode = WindowManager
                .LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        //设置页面延伸到刘海区显示
        window.setAttributes(lp);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setStatusBarTransparent(Window window){
        window.getDecorView().setSystemUiVisibility(View
                .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

}
