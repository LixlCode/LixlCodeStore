package com.baidu.mapframework.app.fpstack;

import java.net.URI;

import com.baidu.mapframework.app.ActivityLifecycleCallbacks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.SandboxActivity;

/**
 * 页面栈管理接口
 *
 * @author liguoqing
 * @version 1.0
 * @date 13-6-14 下午8:02
 */
public interface TaskManager {
    int STACK_STRATEGY_REPLACE = 1;
    /**
     * 页面栈页面切换的action定义 用于栈内page切换，Task切换不需要该参数
     */
    String ACTION_NAVIGATE_PAGE = "com.baidu.map.act.navigate_page";
    String ACTION_NAVIGATE_BACK = "com.baidu.map.act.back";
    String NAVIGATE_PAGE_NAME = "target_page_name";
    String NAVIGATE_PAGE_PARAM = "target_page_param";
    String NAVIGATE_PAGE_COMID = "target_page_comid";

    String NAVIGATE_REORDER_TASK = "navigate_reorder_task";
    /**
     * 页面tag
     */
    String NAVIGATE_PAGE_TAG = "target_page_tag";
    /**
     * 页面 URI scheme
     */
    String PAGE_SCHEME = "bdmapui";

    /**
     * 注册页面栈
     *
     * @param task 注册Task的类对象
     * @param page Deprecated
     */
    boolean registerPage(Class<?> task, Class<?> page);

    /**
     * 注册根Task
     *
     * @param clsName 目标Tak的类名(全限定名)
     */
    void registerRootTask(String clsName);

    void navigateToScene(Context ctx, String sceneId, Bundle sceneArgs);

    /**
     * 无参数跳转页面
     *
     * @param ctx         Context
     * @param pageClsName 目标页面的类名(全限定名)
     *
     * @see {@link #navigateTo(android.content.Context, String, android.os.Bundle)}
     */
    void navigateTo(Context ctx, String pageClsName);

    /**
     * 带参数跳转页面
     *
     * @param ctx         Context
     * @param pageClsName 目标页面的类名(全限定名)
     * @param pageArgs    跳转参数
     */
    void navigateTo(Context ctx, String pageClsName, Bundle pageArgs);

    /**
     * 指定页面Tag跳转页面
     * </p>
     * 为跳转的页面添加标签，构造多实例页面
     *
     * @param ctx           Context
     * @param pageClsName   目标页面的类名(全限定名)
     * @param pageTagString 目标页面标签
     */
    void navigateTo(Context ctx, String pageClsName, String pageTagString);

    /**
     * 指定页面Tag跳转页面，附加页面参数
     * </p>
     * 为跳转的页面添加标签，构造多实例页面
     *
     * @param ctx           Context
     * @param pageClsName   目标页面的类名(全限定名)
     * @param pageTagString 目标页面标签
     * @param pageArgs      页面参数
     */
    void navigateTo(Context ctx, String pageClsName, String pageTagString, Bundle pageArgs);

    /**
     * 指定页面Tag跳转页面，附加页面参数
     * </p>
     * 为跳转的页面添加标签，构造多实例页面
     *
     * @param ctx           Context
     * @param componentId   组件id
     * @param pageClsName   目标页面的类名(全限定名)
     * @param pageTagString 目标页面标签
     * @param pageArgs      页面参数
     */
    void navigateTo(Context ctx, String componentId, String pageClsName, String pageTagString,
                    Bundle pageArgs);

    /**
     * Task跳转
     * <p>按默认方式（单实例）跳转Task。</p>
     *
     * @param ctx    Context
     * @param intent Intent
     */
    void navigateToTask(Context ctx, Intent intent);

    /**
     * Task跳转
     * <p>按给定 Intent Flag 进行 Task 跳转。</p>
     *
     * @param ctx    Context
     * @param intent Activity Intent
     * @param flags  Intent Flags
     */
    void navigateToTask(Context ctx, Intent intent, int flags);

    /**
     * 根据URI跳转页面
     * <p/>
     * 扩展方法，暂未实现
     *
     * @param ctx      Context
     * @param uri      URI
     * @param pageArgs Bundle
     */
    void navigateTo(Context ctx, URI uri, Bundle pageArgs);

    /**
     * 页面栈带参回退回退
     *
     * @param pageArgs
     */
    void onGoBack(Bundle pageArgs);

    /**
     * 页面栈无参回退回调
     */
    void onGoBack();

    /**
     * 添加历史记录
     */
    void track(HistoryRecord record);

    /**
     * 获取根页面记录
     */
    HistoryRecord getRootRecord();

    /**
     * 获取栈顶页面记录
     *
     * @return HistoryRecord
     */
    HistoryRecord getLatestRecord();

    /**
     * 获取当前页面栈记录集合
     */
    ReorderStack<HistoryRecord> getHistoryRecords();

    /**
     * 以record作为标记重置页面栈状态
     * <p>切断历史记录，将record之前的记录清掉。</p>
     *
     * @return 成功返回 true,否则返回 false
     */
    boolean resetStackStatus(HistoryRecord record);

    /**
     * 重置当前页面栈
     * </p>
     * 清空当前页面栈，并将 record作为新的根页面
     */
    void resetRootRecord(HistoryRecord record);

    /**
     * 恢复页面状态到根页面
     */
    void resetToRootRecord();

    /**
     * 清除record在页面栈中的记录
     *
     * @return 成功返回 true,否则返回 false
     */
    boolean removeStackRecord(HistoryRecord record);

    /**
     * 页面记录弹栈
     *
     * @return 成功返回 true,否则返回 false
     */
    boolean pop();

    /**
     * 调试信息输出
     *
     * @return 页面栈状态调试信息字符串
     */
    String dump();

    /**
     * 页面进栈策略
     *
     * @return {@link com.baidu.mapframework.app.fpstack.TaskManager#STACK_STRATEGY_REPLACE}
     */
    int getStackStrategy();

    /**
     * 设置根页面
     */
    void setRootRecord(HistoryRecord record);

    /**
     * ClearTop操作
     * </p>
     * 清除栈中record之后进栈的页面
     */
    void clearTop(HistoryRecord record);

    /**
     * 清理栈记录
     */
    void clear();

    /**
     * 销毁TaskManager
     */
    void destroy();

    /**
     * 获取当前的Context
     *
     * @return 当前激活页面的 Activity Context
     */
    Context getContext();

    /**
     * 保存页面栈状态
     */
    Parcelable saveState();

    /**
     * 恢复页面栈状态
     */
    void restoreState(Parcelable state);

    void attach(Activity activity);

    void detach();

    Activity getContainerActivity();

    void preLoadPage(String pageClsName, String pageTagString);

    void registerPageStackChangedListener(IPageStackChangedListener pageStackStatus);

    void unregisterPageStackChangedListener(IPageStackChangedListener pageStackStatus);

    void setComClassLoader(IComClassLoader comClassLoader);

    IComClassLoader getComClassLoader();

    ActivityLifecycleCallbacks getActivityLifecycleCallbacks();

    void setActivityLifecycleCallbacks(ActivityLifecycleCallbacks callbacks);

    interface IPageStackChangedListener {
        void onPageStackChanged(boolean isEmpty);
    }

    interface IComClassLoader {
        SandboxActivity getSandboxActivity(String comId);
    }

}
