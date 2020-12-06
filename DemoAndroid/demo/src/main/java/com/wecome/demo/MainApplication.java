package com.wecome.demo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/26.
 * 自定义Application
 */

public class MainApplication extends Application {

    //静态的系统级的上下文
    public static Context context;
    //当前主线程对应的Id
    private static int mainThreadId;

    private static List<Activity> activityList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        //获取系统级的上下文，供外部使用
        context = getApplicationContext();
        //获取当前主线程对应的Id
        mainThreadId = android.os.Process.myTid();
    }

    /**
     * 对外提供获取上下文的公共的方法
     */
    public static Context getContext(){
        return context;
    }

    /**
     * 对外提供获取当前主线程Id的方法
     */
    public static int getMainThreadId(){
        return mainThreadId;
    }

    /**
     * 添加Activity到activityList，在onCreate()中调用
     */
    public static void addActivity(Activity activity) {
        if (activityList != null && activityList.size() > 0) {
            if (!activityList.contains(activity)) {
                activityList.add(activity);
            }
        } else if (activityList != null){
            activityList.add(activity);
        }
    }

    /**
     * 结束Activity到activityList，在onDestroy()中调用
     */
    public static void finishActivity(Activity activity) {
        if (activity != null && activityList != null && activityList.size() > 0) {
            activityList.remove(activity);
        }
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        if (activityList != null && activityList.size() > 0) {
            for (Activity activity : activityList) {
                if (null != activity) {
                    activity.finish();
                }
            }
        }
        if (activityList != null) {
            activityList.clear();
        }
    }
}
