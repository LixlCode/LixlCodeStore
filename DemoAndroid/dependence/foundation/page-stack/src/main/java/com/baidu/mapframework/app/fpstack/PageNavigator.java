/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapframework.app.fpstack;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;

import com.baidu.mapframework.app.map.GPSSwitcher;
import com.baidu.mapframework.app.map.LayerInterface;
import com.baidu.mapframework.app.pagestack.R;
//import com.baidu.platform.comapi.DebugConfig;
//import com.baidu.platform.comapi.JNIInitializer;
//import com.baidu.platform.comapi.util.MLog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.SandboxActivity;
import android.text.TextUtils;
import android.view.ViewGroup;

/**
 * PageNavigator
 * <p>
 * 页面栈导航重构,逻辑从原来的 BaseTask 里抽出来。
 * </p>
 *
 * @author liguoqing
 * @version 1.0 3/16/16
 */
public final class PageNavigator {

    private static final String TAG = "BMUISTACK:PageNavigator";
    private static final boolean DEBUG = /*DebugConfig.DEBUG*/false;

    private SoftReference<FragmentActivity> containerActivityRef;
    private ViewGroup pageContainerViewGroup;
    private FragmentManager mFragmentManager;
    final ReorderStack<Page> pageStack = new ReorderStack<>();
    private static final List<String> PERSISTED_PAGE_TYPE = new ArrayList<>();

    private boolean isAnimationEnabled = true/*JNIInitializer.getCachedContext().getResources().getBoolean(R.bool
            .animation_enabled)*/;

    private int persistContainerId = -1;
    private int replaceContainerId = -1;

    private LayerInterface.LayerTransition layerTransition;
    private GPSSwitcher gpsSwitcher;
    private TaskManager.IComClassLoader comClassLoader;

    public PageNavigator() {
        FragmentManager.enableDebugLogging(DEBUG);
    }

    public void setContainerIds(int persistContainerId, int replaceContainerId) {
        this.persistContainerId = persistContainerId;
        this.replaceContainerId = replaceContainerId;
    }

    /**
     * Constructor
     *  @param containerActivity
     * @param pageContainer
     * @param
     */
    public PageNavigator(/*@NotNull*/ FragmentActivity containerActivity, /*@NotNull*/ ViewGroup pageContainer) {
        if (containerActivity == null || pageContainer == null) {
            throw new IllegalArgumentException("ContainerActivity or PageContainer must be not null!");
        }
        containerActivityRef = new SoftReference<>(containerActivity);
        pageContainerViewGroup = pageContainer;
        mFragmentManager = containerActivity.getSupportFragmentManager();
        FragmentManager.enableDebugLogging(DEBUG);
    }

    public void setContainerActivity(FragmentActivity containerActivity) {
        containerActivityRef = new SoftReference<>(containerActivity);
        mFragmentManager = containerActivity.getSupportFragmentManager();
    }

    public void setPageContainer(ViewGroup viewGroup) {
        pageContainerViewGroup = viewGroup;
    }

    public void setLayerTransition(LayerInterface.LayerTransition layerTransition) {
        this.layerTransition = layerTransition;
    }

    public void setComClassLoader(TaskManager.IComClassLoader comClassLoader) {
        this.comClassLoader = comClassLoader;
    }

    public void setPageGPSSwitcher(GPSSwitcher gpsSwitcher) {
        this.gpsSwitcher = gpsSwitcher;
    }

    @SafeVarargs
    public static void registerPersistPageTypes(final Class<? extends BasePage>... pageClasses) {
        for (Class c : pageClasses) {
            PERSISTED_PAGE_TYPE.add(c.getName());
        }
    }

    /**
     * 页面导航
     *
     * @param componentId   组件 Id
     * @param pageClsName   页面类名
     * @param pageTagString 页面 Tag
     * @param pageArgs      页面参数
     */
    public void navigateTo(/*@NotNull*/ String componentId, /*@NotNull*/ String pageClsName, /*@Nullable*/ String pageTagString,
                           /*@Nullable*/ Bundle pageArgs) {

        ClassLoader classLoader = getComponentClassLoader(componentId);

        BasePage page = PageFactoryImpl.getInstance().getBasePageInstance(classLoader, pageClsName, pageTagString);

        if (page == null) {
            return;
        }

        page.setComId(componentId);
        page.setGpsSwitcher(gpsSwitcher);
        page.setTask((Task) containerActivityRef.get());
        if (!TextUtils.isEmpty(pageTagString)) {
            page.setPageTag(pageTagString);
        } else {
            page.setPageTag(PageFactory.DEFAULT_PAGE_TAG);
        }

        BasePage top = null;
        if (!pageStack.isEmpty()) {
            top = (BasePage) pageStack.peek();
            // 如果顶层页面和要跳转到的页面相同
            if (top.equals(page)) {
                processReNavigateSamePage(componentId, top, pageArgs, false);
                return;
            }
        }

        if (isPersistedPage(page)) {
            if (pageStack.contains(page)) {
                String tagString = ((Object) page).getClass().getName() + page.getPageTag();
                if (page.getTag() != null && !page.getTag().equals(tagString)) {
                    tagString = page.getTag();
                }
                if (mFragmentManager.findFragmentByTag(tagString) != null) {
                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    processPageVisibleState(ft, page);
                    ft.commitAllowingStateLoss();
                    processReNavigateSamePage(componentId, page, pageArgs, false);
                    if (layerTransition != null) {
                        layerTransition.onLayerTransition(top, page);
                    }
                    notifyPageStackChanged(false);
                    return;
                }
            }
        }
        try {
            page.setPageArgumentsInside(pageArgs);
        } catch (Exception e) {
            if (DEBUG) {
                //MLog.e(TAG, "", e);
            }
        }
        // add the page to page-stack
        // if the record has been in stack,bring it to front
        recordPageNavigation(componentId, page);
        if (DEBUG) {
            //MLog.i(TAG, TaskManagerFactory.getTaskManager().dump());
        }

        navigateAction(top, page);
    }

    /**
     * 回退操作
     *
     * @param args
     *
     * @return
     */
    public boolean goBack(Bundle args) {
        if (DEBUG) {
            //MLog.d(TAG, "goBack:" + args);
        }
        HistoryRecord record = getTaskManager().getLatestRecord();
        if (record == null) {
            return false;
        }
        // 当前task
        if (record.taskName.equals(((Object) containerActivityRef.get()).getClass().getName())) {
            if (DEBUG) {
                //MLog.i(TAG, getTaskManager().dump());
            }
            getTaskManager().pop();
            // 当前task中无page时back
            if (record.pageName == null && pageStack.isEmpty()) {

                HistoryRecord nextRecord = getTaskManager().getLatestRecord();

                if (nextRecord != null) {
                    // 纯task跳转
                    if (!TextUtils.isEmpty(nextRecord.taskName) && nextRecord.pageName == null) {
                        Intent backIntent = ((TaskManagerImpl) getTaskManager()).getTaskIntent(nextRecord);
                        if (backIntent != null) {
                            backIntent.putExtra(TaskManager.ACTION_NAVIGATE_BACK, true);
                            backIntent.putExtra(TaskManager.NAVIGATE_PAGE_PARAM, args);
                            getTaskManager().navigateToTask(containerActivityRef.get(), backIntent);
                        }

                    } else if (!TextUtils.isEmpty(nextRecord.pageName) && !nextRecord.taskName.equals(
                            ((Object) containerActivityRef.get()).getClass().getName())) {
                        // back到上个task中的page
                        ((TaskManagerImpl) getTaskManager()).navigateBackFromTask(containerActivityRef.get(),
                                nextRecord, args);
                    }
                }

                // 结束该Task
                containerActivityRef.get().finish();
                return true;
            } else {
                HistoryRecord nextRecord = getTaskManager().getLatestRecord();
                if (pageStack.isEmpty()) {
                    if (nextRecord != null) {
                        navigateTo(nextRecord.componentId, nextRecord.pageName, nextRecord.pageSignature, args);
                        return true;
                    } else {
                        containerActivityRef.get().finish();
                        return false;
                    }
                } else if (nextRecord != null && !nextRecord.taskName.equals(
                        ((Object) containerActivityRef.get()).getClass().getName())) {
                    Page topPage = pageStack.peek();
                    pageStack.pop();
                    if (topPage != null) {
                        topPage.onGoBack();
                        FragmentTransaction ft = mFragmentManager.beginTransaction();
                        ft.remove((BasePage) topPage);
                        ft.commitAllowingStateLoss();
                        PageFactoryImpl.getInstance().removePage((BasePage) topPage);
                        if (layerTransition != null) {
                            layerTransition.onLayerTransition((BasePage)topPage, null);
                        }

                        notifyPageStackChanged(false);
                    }
                    try {
                        Intent backIntent = new Intent(containerActivityRef.get(), Class.forName(nextRecord.taskName));
                        backIntent.putExtra(TaskManager.ACTION_NAVIGATE_BACK, true);
                        backIntent.putExtra(TaskManager.NAVIGATE_PAGE_PARAM, args);
                        getTaskManager().navigateToTask(containerActivityRef.get(), backIntent);
                    } catch (Exception e) {
                        if (DEBUG) {
                            //MLog.e(TAG, "", e);
                        }
                    }
                    return true;
                } else {
                    Page topPage = pageStack.peek();
                    pageStack.pop();
                    if (topPage != null) {
                        topPage.onGoBack();
                    }
                    if (pageStack.isEmpty()) {
                        FragmentTransaction ft = mFragmentManager.beginTransaction();

                        if (pageStack.isEmpty()) {
                            ft.remove((BasePage) topPage);
                            ft.commitAllowingStateLoss();
                            PageFactoryImpl.getInstance().removePage((BasePage) topPage);

                            // 如果还有历史，则到上个历史页面，否则finish该task
                            // HistoryRecord nextRecord = getTaskManager().getLatestRecord();
                            if (nextRecord != null) {
                                navigateTo(nextRecord.componentId, nextRecord.pageName, nextRecord.pageSignature, args);
                                Page page = null;
                                if (!pageStack.isEmpty()) {
                                    page = pageStack.peek();
                                }
                                if (layerTransition != null) {
                                    layerTransition.onLayerTransition((BasePage) topPage, (BasePage) page);
                                }
                                notifyPageStackChanged(false);
                                return true;
                            } else {
                                notifyPageStackChanged(true);
                            }
                            return false;
                        }
                    } else {
                        return navigateBackAction((BasePage) topPage, args);
                    }
                }
            }
        } else {
            // 如果栈状态错误，结束该Task
            containerActivityRef.get().finish();
        }
        return false;
    }

    void processReNavigateSamePage(String componentId, BasePage page, Bundle pageArgs, boolean isBack) {
        boolean ret = false;
        try {
            ret = mFragmentManager.executePendingTransactions();
        } catch (Exception e) {
            if (DEBUG) {
                //MLog.e(TAG, "", e);
            }
        }
        String tagString = ((Object) page).getClass().getName() + page.getPageTag();
        if (page.getTag() != null && !page.getTag().equals(tagString)) {
            tagString = page.getTag();
        }
        Fragment f = mFragmentManager.findFragmentByTag(tagString);
        if (f != null) {
            page.mIsBack = isBack;
            if (isBack) {
                page.setBackwardArgumentsInside(pageArgs);
                page.onBackFromOtherPage(pageArgs);
            } else {
                page.mBackArgs = null;
            }
            FragmentTransaction ftr = mFragmentManager.beginTransaction();

            if (isPersistedPage(page)) {
                page.setRelaunchedArgs(pageArgs);
                if (f.isHidden()) {
                    ftr.show(page);
                } else if (!f.isAdded() && !f.isInLayout()) {
                    ftr.add(persistContainerId, page, tagString);
                } else {
                    ftr.remove(page);
                    ftr.commitAllowingStateLoss();
                    try {
                        mFragmentManager.executePendingTransactions();
                    } catch (Exception e) {
                        // ignore
                    }
                    ftr = mFragmentManager.beginTransaction();
                    page.setPageArgumentsInside(pageArgs);
                    ftr.replace(replaceContainerId, page, tagString);
                }
            } else {
                ftr.remove(page);
                ftr.commitAllowingStateLoss();
                try {
                    mFragmentManager.executePendingTransactions();
                } catch (Exception e) {
                    // ignore
                }
                ftr = mFragmentManager.beginTransaction();
                page.setPageArgumentsInside(pageArgs);
                ftr.replace(replaceContainerId, page, tagString);
            }
            ftr.commitAllowingStateLoss();
            // add the page to page-stack
            // if the record has been in stack,bring it to front
            recordPageNavigation(componentId, page);

        } else if (!ret && !page.isAdded()) {
            page.mIsBack = isBack;
            if (isBack) {
                page.setBackwardArgumentsInside(pageArgs);
                page.onBackFromOtherPage(pageArgs);
            } else {
                page.mBackArgs = null;
            }
            try {
                mFragmentManager.popBackStackImmediate();
                page.setPageArgumentsInside(pageArgs);
                FragmentTransaction ftr = mFragmentManager.beginTransaction();
                if (isPersistedPage(page)) {
                    ftr.add(persistContainerId, page, tagString);
                    if (page.isHidden()) {
                        ftr.show(page);
                    }
                } else {
                    ftr.replace(replaceContainerId, page, tagString);
                }
                ftr.commitAllowingStateLoss();
                // add the page to page-stack
                // if the record has been in stack,bring it to front
                recordPageNavigation(componentId, page);
            } catch (Exception e) {
                if (DEBUG) {
                    //MLog.e(TAG, "", e);
                }
            }

        }
    }

    private void navigateAction(BasePage topPage, BasePage page) {
        if (DEBUG) {
            //MLog.d(TAG, "navigateAction " + ((Object) page).getClass().getSimpleName());
        }

        if (mFragmentManager.isDestroyed()) {
            return;
        }

        boolean isPersistedPage = isPersistedPage(page);
        if (isPersistedPage) {
            try {
                mFragmentManager.executePendingTransactions();
            } catch (Exception e) {
                // ignore
            }
        }

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        page.mIsBack = false;

        if (isAnimationEnabled) {
            // 添加到栈顶的界面的进入动画
            int[] customAnims = page.getCustomAnimations();
            int inAnim = customAnims[0];
            // 替换掉的界面的退出动画
            int outAnim = customAnims[1];
            if (topPage != null && !topPage.shouldOverrideCustomAnimations()) {
                outAnim = topPage.getCustomAnimations()[3];
            }

            ft.setCustomAnimations(inAnim, outAnim);
        }
        String tagString = ((Object) page).getClass().getName() + page.getPageTag();
        if (page.getTag() != null && !page.getTag().equals(tagString)) {
            tagString = page.getTag();
        }

        Fragment tmp = mFragmentManager.findFragmentByTag(tagString);

        processPageVisibleState(ft, page);

        if (tmp != null) {
            if (isPersistedPage(page)) {
                //MLog.d(TAG, "is persisted page,add");
                if (tmp.isHidden()) {
                    //MLog.d(TAG, "navigate to show page:" + page);
                    ft.show(page);
                } else if (!tmp.isAdded() && !tmp.isInLayout()) {
                    //MLog.d(TAG, "navigate to add page:" + page);
                    ft.add(persistContainerId, page, tagString);
                }
            } else {
                //MLog.d(TAG, "is not persisted page,replace");
                ft.replace(replaceContainerId, page, tagString);
            }
        } else {
            if (isPersistedPage(page)) {
                //MLog.d(TAG, "is persisted page, not in stack");
                //MLog.d(TAG, "navigate to add page:" + page);
                ft.add(persistContainerId, page, tagString);
            } else {
                //MLog.d(TAG, "is not persisted page,replace");
                ft.replace(replaceContainerId, page, tagString);
            }
        }
        ft.commitAllowingStateLoss();
        if (layerTransition != null) {
            layerTransition.onLayerTransition(topPage, page);
        }
        notifyPageStackChanged(false);

    }

    private void processPageVisibleState(FragmentTransaction ft, BasePage targetPage) {
        final int pageCnt = pageStack.size();
        for (int index = pageCnt - 1; index >= 0; index--) {
            final BasePage page = (BasePage) pageStack.get(index);
            if (page != targetPage) {
                if (isPersistedPage(page)) {
                    if (page.isVisible() && page.isAdded()) {
                        ft.hide(page);
                    }
                } else if (isPersistedPage(targetPage)) {
                    ft.remove(page);
                }
            }
        }
    }

    private boolean isPersistedPage(BasePage page) {
        return PERSISTED_PAGE_TYPE.contains(page.getClass().getName());
    }

    private boolean navigateBackAction(BasePage page, Bundle args) {
        if (DEBUG) {
            //MLog.d(TAG, "navigateBackAction " + ((Object) page).getClass().getSimpleName());
        }

        FragmentTransaction ft = mFragmentManager.beginTransaction();

        if (pageStack.isEmpty()) {
            ft.remove(page);
            ft.commitAllowingStateLoss();
            PageFactoryImpl.getInstance().removePage(page);
            if (layerTransition != null) {
                layerTransition.onLayerTransition(page, null);
            }
            containerActivityRef.get().finish();
            if (DEBUG) {
                //MLog.d(TAG, "The Task's page-stack is empty.Finish!");
            }
            return false;
        }

        HistoryRecord record = getTaskManager().getLatestRecord();
        if (record == null) {
            return false;
        }
        String pageName = record.pageName;
        String pageTag = record.pageSignature;
        String comId = record.componentId;
        ClassLoader classLoader = getComponentClassLoader(comId);
        BasePage targetPage = PageFactoryImpl.getInstance().getBasePageInstance(classLoader, pageName, pageTag);

        targetPage.setComId(comId);

        Bundle oldArgs = PageFactoryImpl.getInstance().getPageSavedArguments(pageName, pageTag);
        if (targetPage == null) {
            return false;
        }
        targetPage.mIsBack = true;
        targetPage.mBackArgs = args;
        targetPage.setBackwardArgumentsInside(args);
        targetPage.onBackFromOtherPage(args);
        if (targetPage.getTask() == null) {
            targetPage.setTask((Task) containerActivityRef.get());
        }
        if (oldArgs != null) {
            targetPage.mIsBack = false;
            targetPage.setPageArgumentsInside(oldArgs);
        }
        String tagString = ((Object) targetPage).getClass().getName() + targetPage.getPageTag();
        if (targetPage.getTag() != null && !targetPage.getTag().equals(tagString)) {
            tagString = targetPage.getTag();
        }
        if (isAnimationEnabled) {
            // 当前界面的退出动画
            int[] customAnims = page.getCustomAnimations();
            int outAnim = customAnims[3];
            // 回退到的界面的重新进入动画
            int inAnim =
                    targetPage.shouldOverrideCustomAnimations() ? customAnims[2] : targetPage.getCustomAnimations()[0];

            ft.setCustomAnimations(inAnim, outAnim);
        }

        processPageVisibleState(ft, targetPage);
        ft.remove(page);
        Fragment tmp = mFragmentManager.findFragmentByTag(tagString);
        if (isPersistedPage(targetPage)) {
            //MLog.d(TAG, "is persisted page,add");
            if (null != tmp && tmp.isHidden()) {
                //MLog.d(TAG, "navigate back show page:" + targetPage);
                ft.show(targetPage);
            } else if (null == tmp) {
                try {
                    mFragmentManager.executePendingTransactions();
                } catch (Exception e) {
                    // ignore
                }
                tmp = mFragmentManager.findFragmentByTag(tagString);
                if (null == tmp) {
                    //MLog.d(TAG, "navigate back add page:" + targetPage);
                    ft.add(persistContainerId, targetPage, tagString);
                }
            }
        } else {
            //MLog.d(TAG, "is not persisted page,replace");
            ft.replace(replaceContainerId, targetPage, tagString);
        }
        PageFactoryImpl.getInstance().removePage(page);
        try {
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            // empty
        }
        if (layerTransition != null) {
            layerTransition.onLayerTransition(page, targetPage);
        }
        notifyPageStackChanged(false);
        return true;
    }

    private void recordPageNavigation(String comId, Page page) {

        HistoryRecord record = new HistoryRecord(comId, ((Object) containerActivityRef.get()).getClass().getName(),
                page.getClass().getName());
        record.taskSignature = HistoryRecord.genSignature(containerActivityRef.get());
        record.pageSignature = page.getPageTag() != null ? page.getPageTag() : PageFactory.DEFAULT_PAGE_TAG;
        pageStack.push(page);
        TaskManagerFactory.getTaskManager().track(record);
    }

    private ClassLoader getComponentClassLoader(String componentId) {
        if (comClassLoader == null) {
            comClassLoader = TaskManagerFactory.getTaskManager().getComClassLoader();
        }
        if (comClassLoader != null) {
            SandboxActivity sandboxActivity = comClassLoader.getSandboxActivity(componentId);
            if (sandboxActivity != null) {
                return sandboxActivity.getClassLoader();
            }
        }
        return getClass().getClassLoader();
    }

    private TaskManager getTaskManager() {
        return TaskManagerFactory.getTaskManager();
    }

    private void notifyPageStackChanged(boolean isEmpty) {
        List<TaskManager.IPageStackChangedListener> pageCallbacks = ((TaskManagerImpl) getTaskManager())
                .pageStackStatusCallbacks;
        List<TaskManager.IPageStackChangedListener> callbacks = null;
        synchronized(pageCallbacks) {
            callbacks = new LinkedList<>(pageCallbacks);
        }
        if (callbacks != null) {
            for (TaskManager.IPageStackChangedListener callback : callbacks) {
                callback.onPageStackChanged(isEmpty);
            }
        }
    }

}
