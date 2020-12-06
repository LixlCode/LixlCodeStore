package com.wecome.demo.utils;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.wecome.demo.MainApplication;


public class ScreenUtils {
    private static Display defaultDisplay;

    private static float mDensity = 0;
    private static float mScaledDensity = 0;
    private static int mScreenHeight = 0;

    public static float getDensity(Context context) {
        if (mDensity == 0) {
            mDensity = context.getResources().getDisplayMetrics().density;
        }
        return mDensity;
    }

    public static float getScaledDensity(Context context) {
        if (mScaledDensity == 0) {
            mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        }
        return mScaledDensity;
    }

    public static int dip2px(float dip, Context context) {
        return (int) (0.5F + getDensity(context) * dip);
    }

    public static int dip2px(int dip) {
        return (int) (0.5F + getDensity(MainApplication.getContext()) * dip);
    }

    public static int dip2px(float dip) {
        return (int) (0.5F + getDensity(MainApplication.getContext()) * dip);
    }

    public static int px2dip(int px, Context context) {
        return (int) (0.5F + px / getDensity(context));
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param （DisplayMetrics类中属性scaledDensity）
     *
     * @return
     */
    public static int px2sp(float pxValue, Context context) {
        return (int) (pxValue / getScaledDensity(context) + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param （DisplayMetrics类中属性scaledDensity）
     *
     * @return
     */
    public static int sp2px(float spValue, Context context) {
        return (int) (spValue * getScaledDensity(context) + 0.5f);
    }

    // get screen width value int pixels
//    public static int getScreenWidth(Activity activity) {
//        return getScreenWidth(TaskManagerFactory.getTaskManager().getContainerActivity());
//    }

    public static int getScreenWidth(Context context) {
        if (context != null) {
            return context.getResources().getDisplayMetrics().widthPixels;
        }
        return 0;
    }

    // get screen height value int pixels
    public static int getScreenHeight(Context context) {
        if (context != null) {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
        return 0;
    }

    public static void setViewScreenHeight(int height) {
        if (mScreenHeight >= 0 && (mScreenHeight - height) < mScreenHeight / 4) {
            mScreenHeight = height;
        }
    }

    /**
     * 通过activity的根view的高度获取屏幕告诉
     * 解决某些手机上虚拟按键的问题
     */
//    public static int getViewScreenHeightFull() {
//        Context context = TaskManagerFactory.getTaskManager().getContainerActivity();
//        if (mScreenHeight > 0) {
//            if (Build.VERSION.SDK_INT >= 21) {
//                return mScreenHeight - getStatusBarHeight(context);
//            }
//            return mScreenHeight;
//        }
//        return getScreenHeight(context) - getStatusBarHeight(context);
//    }

//    public static int getViewScreenHeight(Context context) {
//        if (context == null) {
//            return 0;
//        }
//        if (mScreenHeight > 0) {
//            return mScreenHeight;
//        }
//        return getScreenHeight(context) - getStatusBarHeight(context);
//    }

    public static Display getDefaultDisplay(Context context) {
        if (defaultDisplay == null) {
            defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        }
        return defaultDisplay;
    }

    public static int getHeight(Context context) {
        return getDefaultDisplay(context).getHeight();
    }

    public static int getWidth(Context context) {
        return getDefaultDisplay(context).getWidth();
    }

    public static int percentHeight(float percent, Context context) {
        return (int) (percent * getHeight(context));
    }

    public static int percentWidth(float percent, Context context) {
        return (int) (percent * getWidth(context));
    }

//    public static int getStatusBarHeight(Context context) {
//        if (context instanceof Activity) {
//            Rect rectangle = new Rect();
//            Window window = ((Activity) context).getWindow();
//            if (window != null && window.getDecorView() != null) {
//                window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
//            }
//            return rectangle.top;
//        } else {
//            DisplayMetrics metrics = JNIInitializer.getCachedContext().getResources().getDisplayMetrics();
//            int STATUS_BAR_HEIGHT = (int) Math.ceil(25 * metrics.density);
//            return STATUS_BAR_HEIGHT;
//        }
//
//    }

//    public static int getStatusBarHeightFullScreen(Context context) {
//        if (Build.VERSION.SDK_INT < 21 || context == null) {
//            return 0;
//        }
//        int result = 0;
//        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = context.getResources().getDimensionPixelSize(resourceId);
//        }
//        return result > 0 ? result : dip2px(25);
//    }

//    public static boolean zoomPlaceHolderRight(Context context) {
//        int sH = ScreenUtils.getViewScreenHeight(context);
//        // 改为按照详情页标准
//        if (sH <= 0) {
//            context = JNIInitializer.getCachedContext().getApplicationContext();
//            sH = ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusBarHeight(context);
//        }
//
//        int sHDP = ScreenUtils.px2dip(sH, context);
//        // 分别为 - 顶部搜索框 - 右边按钮bar - 底部
//        int minH = sHDP - 65 - 202 - 264;
//        int zoomDpHeight = 80;
//
//        return minH > zoomDpHeight;
//
//    }
}
