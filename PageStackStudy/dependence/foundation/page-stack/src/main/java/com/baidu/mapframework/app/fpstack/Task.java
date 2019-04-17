package com.baidu.mapframework.app.fpstack;

import android.content.Intent;
import android.os.Bundle;

/**
 * 页面栈的抽象接口
 * </p>
 *
 * @author liguoqing
 * @version 1.0
 * @date 13-5-26 午1:49
 */
public interface Task {

    TaskManager getTaskManager();

    /**
     * 设置当前栈tag
     *
     * @param taskTag 栈tag
     */
    void setTaskTag(String taskTag);

    /**
     * 获取栈Tag
     *
     * @return 页面Tag
     */
    String getTaskTag();

    /**
     * 获取当前栈的页面
     *
     * @return
     */
    java.util.Stack<Page> getPageStack();

    void navigateTo(String pageClsName, String pageTagString, Bundle pageArgs);

    void navigateTo(String componentId, String pageClsName, String pageTagString, Bundle pageArgs);

    /**
     * 显示默认内容，无子页面
     */
    void onShowDefaultContent(Intent intent);

    boolean goBack(Bundle args);

    boolean goBack();

    boolean handleBack(Bundle args);

    boolean goBackToScene(String sceneId, Bundle args);

    /**
     * 销毁该页面，并从栈记录中移除。
     * <p>当需要跳转到其他页面并把自己销毁时使用，谨慎使用，这个接口适合单个Task，无page的移植页面。</p>
     */
    void finish();
}
