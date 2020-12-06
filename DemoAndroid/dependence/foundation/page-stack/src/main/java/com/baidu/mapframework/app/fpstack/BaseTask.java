package com.baidu.mapframework.app.fpstack;

import java.util.ArrayList;
import java.util.Stack;

import com.baidu.mapframework.app.ActivityLifecycleCallbacks;
import com.baidu.mapframework.app.map.GPSSwitcher;
import com.baidu.mapframework.app.map.LayerInterface;
//import com.baidu.mapframework.common.config.GlobalConfig;
//import com.baidu.mapframework.common.customize.config.CstmConfig;
import com.baidu.mapframework.scenefw.ScenePage;
//import com.baidu.mobstat.StatService;
//import com.baidu.platform.comapi.DebugConfig;
//import com.baidu.platform.comapi.util.BMEventBus;
//import com.baidu.platform.comapi.util.MLog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.FrameLayout;

/**
 * Task的基类实现，以FragmentActivity作为载体，子页面用Fragment实现
 * </p>
 *
 * @author liguoqing
 * @version 1.0
 * @date 13-5-26 下午3:53
 * @hide
 */
public abstract class BaseTask extends FragmentActivity implements Task, DebugUI {
    private static final boolean DEBUG = /*DebugConfig.DEBUG*/false;
    private static final String TAG = "BMUISTACK:BaseTask";
    private String taskTag;
    protected static ActivityLifecycleCallbacks activityLifecycleCallbacks;
    protected PageNavigator mNavigator = new PageNavigator();

    private static final String TASKMGR_STATE_KEY = "maps.taskmgr.state";
    private static final String TASK_SIG_KEY = "maps.task.sig";

    static final String FRAGMENTS_TAG = "android:support:fragments";

    // 是否调用了onDestroy API17 新增了 isDestroyed()
    boolean mDestroyed = false;

    private volatile Handler debugHandler;
    private static final int MESSAGE_ID_ADDCONTENT = 1;
    private static final int MESSAGE_ID_UPDATE_PAGE_NAME = 2;
    private static final int MESSAGE_ID_UPDATE_PAGE_VELOCITY = 3;
    private static final int MESSAGE_DELAY_MS = 500;

    private DebugUISetting settingEntity;
    private DebugView debugView;

    private GPSSwitcher gpsSwitcher;

    @Override
    public TaskManager getTaskManager() {
        return TaskManagerFactory.getTaskManager();
    }

    @Override
    public void setTaskTag(String taskTag) {
        this.taskTag = taskTag;
    }

    @Override
    public final String getTaskTag() {
        return this.taskTag;
    }

    @Override
    public Stack<Page> getPageStack() {
        return mNavigator.pageStack;
    }

    public void setLayerTransition(LayerInterface.LayerTransition layerTransition) {
        mNavigator.setLayerTransition(layerTransition);
    }

    @Override
    public void navigateTo(final String componentId, final String pageClsName, final String pageTagString,
                           final Bundle pageArgs) {
        if (DEBUG) {
            //MLog.d(TAG, "=== navigateTo === " + pageClsName);
        }
        if (mDestroyed) {
            return;
        }
        if (null == mNavigator) {
            throw new IllegalStateException("PageNavigator is null!");
        }
        mNavigator.navigateTo(componentId, pageClsName, pageTagString, pageArgs);
    }

    @Override
    public void navigateTo(String pageClsName, String pageTagString, Bundle pageArgs) {
        if (DEBUG) {
            //MLog.d(TAG, "=== navigateTo === " + pageClsName);
        }
        navigateTo("map.android.baidu.mainmap" /*ComConstant.COM_ID_MAIN */, pageClsName, pageTagString, pageArgs);
    }

    @Override
    public void onShowDefaultContent(Intent intent) {
        if (DEBUG) {
            //MLog.d(TAG, "onShowDefaultContent");
        }
    }

    /**
     * 返回
     *
     * @param args 附加参数
     * @return 需要外层TaskManager处理返回 false，不需要返回 true
     */
    @Override
    public boolean goBack(Bundle args) {
        if (DEBUG) {
            //MLog.d(TAG, "goBack:" + args);
        }
        if (mDestroyed) {
            return false;
        }
        if (null == mNavigator) {
            throw new IllegalStateException("PageNavigator is null!");
        }
        return mNavigator.goBack(args);
    }

    @Override
    public boolean goBack() {
        return goBack(null);
    }

    @Override
    public boolean goBackToScene(String sceneId, Bundle args) {
        Bundle params;
        if (args == null) {
            params = new Bundle();
        } else {
            params = args;
        }
        params.putString(ScenePage.PAGE_SCENE_KEY, sceneId);
        return goBack(params);
    }

    private Bundle getArguments() {
        Intent localIntent = getIntent();
        if (localIntent == null) {
            return null;
        } else {
            return localIntent.getBundleExtra(TaskManager.NAVIGATE_PAGE_PARAM);
        }
    }

    @Override
    public boolean handleBack(Bundle args) {
        return false;
    }


    /**
     * @see {@link FragmentActivity#startActivityFromFragment }
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        boolean isCalled = false;
        int index = (requestCode >> 16) - 1;
        if (index >= 0) {
            Bundle bundle = new Bundle();
            String key = "index";
            bundle.putInt(key, index);
            Fragment frag = null;
            try {
                frag = this.getSupportFragmentManager().getFragment(bundle, key);
            } catch (IllegalStateException e) {
                if (DEBUG) {
                    //MLog.e(TAG, "", e);
                }
            } catch (NullPointerException e) {
                if (DEBUG) {
                    //MLog.e(TAG, "", e);
                }
            }
            // 没有找到发起调用的fragment，说明不是在fragment内部调用的startActivityForResult
            // 顶层的fragment没有收到onActivityResult，这里主动再调一次
            isCalled = frag != null;
        } else {
            // index为0，也说明不是在fragment内部调用的startActivityForResult，需要主动再调
            isCalled = false;
        }

        if (!isCalled) {
            if (!mNavigator.pageStack.isEmpty()) {
                Page topPage = mNavigator.pageStack.peek();
                if (topPage != null) {
                    try {
                        // 主动调用
                        ((Fragment) topPage).onActivityResult(requestCode, resultCode, data);
                    } catch (ClassCastException e) {
                        // Page都是从Fragment继承的，理论上不会到这里，如果以后又重构，可能要修改这里
                        if (DEBUG) {
                            //MLog.e(TAG, "", e);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void finish() {
        if (DEBUG) {
            //MLog.d(TAG, "Finish:" + this);
        }
        // 清理page
        Stack<Page> pages = getPageStack();
        if (pages != null && !pages.isEmpty()) {
            pages.clear();
        }
        removeHistoryRecords();
        if (DEBUG) {
           // MLog.d(TAG, "removeHistoryRecords end\n" + getTaskManager().dump());
        }
        super.finish();
    }

    @Override
    public void updatePageName(CharSequence text) {
        makeSettingEntity();
        if (settingEntity.showPageName) {
            makeDebugHandler();
            Message message = debugHandler.obtainMessage(MESSAGE_ID_UPDATE_PAGE_NAME, text);
            debugHandler.removeMessages(MESSAGE_ID_UPDATE_PAGE_NAME);
            debugHandler.sendMessageDelayed(message, MESSAGE_DELAY_MS);
        } else {
            if (debugView != null) {
                debugView.showPageNameView.setText("");
            }
        }
    }

    @Override
    public void updatePageVelocity(CharSequence text) {
        makeSettingEntity();
        if (settingEntity.showPageVelocity) {
            makeDebugHandler();
            Message message = debugHandler.obtainMessage(MESSAGE_ID_UPDATE_PAGE_VELOCITY, text);
            debugHandler.removeMessages(MESSAGE_ID_UPDATE_PAGE_VELOCITY);
            debugHandler.sendMessageDelayed(message, MESSAGE_DELAY_MS);
        } else {
            if (debugView != null) {
                debugView.showPageVelocityView.setText("");
            }
        }
    }

    /**
     * 清理该Task中的记录
     */
    private boolean removeHistoryRecords() {
        if (DEBUG) {
            //MLog.d(TAG, "removeHistoryRecords");
        }
        ReorderStack<HistoryRecord> historyRecords = TaskManagerFactory.getTaskManager().getHistoryRecords();
        ArrayList<HistoryRecord> delItems = new ArrayList<HistoryRecord>();
        if (historyRecords != null) {
            for (HistoryRecord record : historyRecords) {
                if (record == null || record.taskName.equals(
                        ((Object) this).getClass().getName())
                        && record.taskSignature.equals(HistoryRecord.genSignature(this))) {
                    delItems.add(record);
                }
            }
            return historyRecords.removeAll(delItems);
        }
        return false;
    }

    protected void create(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelable(TASKMGR_STATE_KEY) != null) {
                getTaskManager().restoreState(savedInstanceState.getParcelable(TASKMGR_STATE_KEY));

                String newSig = HistoryRecord.genSignature(this);
                String oldSig = savedInstanceState.getString(TASK_SIG_KEY);
                if (!TextUtils.isEmpty(oldSig)) {
                    ((TaskManagerImpl) getTaskManager()).updateHistoryRecord(((Object) this).getClass().getName(),
                            oldSig, newSig);
                }
                // 其实恢复历史记录没有用处
                ((TaskManagerImpl) getTaskManager()).clearHistoryRecords();
            }
            notifyTaskChange();
            reportActivityLifecycleCallbacks(ActivityLifecycleCallbacks.TYPE.CREATED, this, savedInstanceState);
            return;
        }
        notifyTaskChange();
        reportActivityLifecycleCallbacks(ActivityLifecycleCallbacks.TYPE.CREATED, this, savedInstanceState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mNavigator.setContainerActivity(this);
        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelable(TASKMGR_STATE_KEY) != null) {
                getTaskManager().restoreState(savedInstanceState.getParcelable(TASKMGR_STATE_KEY));

                String newSig = HistoryRecord.genSignature(this);
                String oldSig = savedInstanceState.getString(TASK_SIG_KEY);
                if (!TextUtils.isEmpty(oldSig)) {
                    ((TaskManagerImpl) getTaskManager()).updateHistoryRecord(((Object) this).getClass().getName(),
                            oldSig, newSig);
                }
                // 其实恢复历史记录没有用处,还要清空
                ((TaskManagerImpl) getTaskManager()).clearHistoryRecords();
                if (DEBUG) {
                    //MLog.e(TAG, "Activity restore:" + savedInstanceState.toString());
                }
            }
            notifyTaskChange();
            reportActivityLifecycleCallbacks(ActivityLifecycleCallbacks.TYPE.CREATED, this, savedInstanceState);

            if (getTaskManager().getRootRecord() != null && !((Object) this).getClass().getName().equals(
                    getTaskManager().getRootRecord().taskName)) {
                finish();
            }
            return;
        }

        notifyTaskChange();

        // 处理需要跳转到子页面
        Intent localIntent = getIntent();
        if (localIntent != null) {
            // args
            Bundle pageArgs = getArguments();
            // navigate page
            // if(TaskManager.ACTION_NAVIGATE_PAGE.equals(localIntent.getAction()))
            {
                String comId = localIntent.getStringExtra(TaskManager.NAVIGATE_PAGE_COMID);
                if (TextUtils.isEmpty(comId)) {
                    comId = "map.android.baidu.mainmap"; // ComConstant.COM_ID_MAIN;
                }
                String pageName = localIntent.getStringExtra(TaskManager.NAVIGATE_PAGE_NAME);
                String pageTag = localIntent.getStringExtra(TaskManager.NAVIGATE_PAGE_TAG);
                //no page,show default content ,the param is intent
                if (TextUtils.isEmpty(pageName)) {
                    onShowDefaultContent(localIntent);
                    boolean isReorderTask = localIntent.getBooleanExtra(TaskManager.NAVIGATE_REORDER_TASK, false);
                    if (!isReorderTask && !isTaskNaviBack(localIntent)) {
                        recordTaskNavigation(localIntent);
                    }
                    // activityLifecycleCallbacks.onActivityCreated(this,savedInstanceState);
                } else {
                    // navigate to page
                    this.navigateTo(comId, pageName, pageTag, pageArgs);
                }
            }
        }
        reportActivityLifecycleCallbacks(ActivityLifecycleCallbacks.TYPE.CREATED, this, savedInstanceState);
    }

    public void activityOnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean isTaskNaviBack(Intent intent) {
        return intent != null && intent.getBooleanExtra(TaskManager.ACTION_NAVIGATE_BACK, false);
    }

    private void notifyTaskChange() {
        TaskChangeEvent event = new TaskChangeEvent();
        event.type = TaskChangeEvent.CHANGE_CUR_TASK;
        event.curTask = this;
        //BMEventBus.getInstance().postImmediate(event);
    }

    protected void newIntent(Intent intent) {
        if (DEBUG) {
            //MLog.e(TAG, "onNewIntent");
        }
        super.onNewIntent(intent);
        this.setIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (DEBUG) {
            //MLog.e(TAG, this + "onNewIntent");
        }
        super.onNewIntent(intent);
        this.setIntent(intent);

        // 处理需要跳转到子页面
        if (intent != null) {
            String pageName = intent.getStringExtra(TaskManager.NAVIGATE_PAGE_NAME);
            String pageTag = intent.getStringExtra(TaskManager.NAVIGATE_PAGE_TAG);
            String comId = intent.getStringExtra(TaskManager.NAVIGATE_PAGE_COMID);
            if (TextUtils.isEmpty(comId)) {
                comId = "map.android.baidu.mainmap"; // ComConstant.COM_ID_MAIN;
            }
            // 回退处理
            if (intent.getBooleanExtra(TaskManager.ACTION_NAVIGATE_BACK, false)) {
                handleBack(getArguments());
                if (!TextUtils.isEmpty(pageName)) { // 跳页面
                    Bundle pageArgs = getArguments();
                    //todo
                    this.navigateBack(comId, pageName, pageTag, pageArgs);
                } else {
                    this.onShowDefaultContent(intent);
                }
            } else {
                if (!TextUtils.isEmpty(pageName)) { // 跳页面
                    Bundle pageArgs = getArguments();
                    this.navigateTo(comId, pageName, pageTag, pageArgs);
                } else {
                    this.onShowDefaultContent(intent);
                    boolean isReorderTask = intent.getBooleanExtra(TaskManager.NAVIGATE_REORDER_TASK, false);
                    if (!isReorderTask) {
                        recordTaskNavigation(intent);
                    }
                }
            }
        }
    }

    // 回退到该页面page
    protected void navigateBack(String comId, String pageName, String pageTag, Bundle pageArgs) {
        // 如果pageStack为空，返回
        if (TextUtils.isEmpty(pageName) || mNavigator.pageStack.isEmpty()) {
            return;
        }

        BasePage topPage = (BasePage) mNavigator.pageStack.peek();
        if (topPage != null) {
            // 该Task栈顶页面和目标不匹配，返回
            if (!((Object) topPage).getClass().getName().equals(pageName)) {
                return;
            }
        }
        mNavigator.processReNavigateSamePage(comId, topPage, pageArgs, true);
    }

    private void recordTaskNavigation(Intent intent) {
        HistoryRecord record = new HistoryRecord(((Object) this).getClass().getName(), null);
        record.taskSignature = HistoryRecord.genSignature(this);
        getTaskManager().track(record);
        ((TaskManagerImpl) getTaskManager()).track(record, intent);
        if (DEBUG) {
            //MLog.d(TAG, "recordTaskNavigation");
            //MLog.d(TAG, getTaskManager().dump());
        }
    }

    @Override
    protected void onDestroy() {
        if (DEBUG) {
            //MLog.d(TAG, "onDestroy");
        }
        super.onDestroy();
        mDestroyed = true;
        reportActivityLifecycleCallbacks(ActivityLifecycleCallbacks.TYPE.DESTROYED, this, null);
        TaskChangeEvent event = new TaskChangeEvent();
        event.type = TaskChangeEvent.REMOVE_TASK;
        event.curTask = this;
        //BMEventBus.getInstance().post(event);
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            /*if (GlobalConfig.getInstance().isInitMtj()) {
                StatService.onResume(this);
            }*/
        } catch (Exception e) {
            if (DEBUG) {
                //MLog.e(TAG, "", e);
            }
        }
        reportActivityLifecycleCallbacks(ActivityLifecycleCallbacks.TYPE.RESUMED, this, null);
        notifyTaskChange();
        boolean useGps = changeGPSRequest();
        if (gpsSwitcher != null) {
            gpsSwitcher.enableGPS(useGps);
        }

    }

    /**
     * 变更gps注册状态, onResume时调用.
     */
     boolean changeGPSRequest() {
        return true;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        checkPageState();
    }

    private void checkPageState() {
        HistoryRecord lastRecord = getTaskManager().getLatestRecord();
        if (lastRecord != null && lastRecord.pageName != null && this.equals(
                TaskManagerFactory.getTaskManager().getContainerActivity())) {
            final Stack<Page> pages = getPageStack();
            if (pages.isEmpty()) {
                navigateTo(lastRecord.componentId, lastRecord.pageName, lastRecord.pageSignature, null);
            } else {
                BasePage top = (BasePage) pages.peek();
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                if (top != null && !fragmentManager.getFragments().contains(top) && !top.isVisible()) {
                    navigateTo(lastRecord.componentId, lastRecord.pageName, lastRecord.pageSignature, null);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
       /*if (GlobalConfig.getInstance().isInitMtj()) {
            StatService.onPause(this);
        }*/
        reportActivityLifecycleCallbacks(ActivityLifecycleCallbacks.TYPE.PAUSED, this, null);

        if (DEBUG) {
            //MLog.d(TAG, "onPause");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        reportActivityLifecycleCallbacks(ActivityLifecycleCallbacks.TYPE.STARTED, this, null);
        if (DEBUG) {
            //MLog.d(TAG, "onStart");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        reportActivityLifecycleCallbacks(ActivityLifecycleCallbacks.TYPE.STOPPED, this, null);
        if (DEBUG) {
            //MLog.d(TAG, "onStop");
        }
    }

    /**
     * 系统默认会恢复Fragment状态，在此忽略系统的恢复
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(TASKMGR_STATE_KEY, getTaskManager().saveState());
        outState.putString(TASK_SIG_KEY, HistoryRecord.genSignature(this));
        reportActivityLifecycleCallbacks(ActivityLifecycleCallbacks.TYPE.SAVEINSTANCESTATE, this, outState);
        if (DEBUG) {
            //MLog.e(TAG, "onSaveInstanceState:" + outState.toString());
        }
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (DEBUG) {
            //MLog.d(TAG, "onAttachedToWindow");
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (DEBUG) {
            //MLog.d(TAG, "onDetachedFromWindow");
        }
    }

    @Override
    public void onBackPressed() {
        if (DEBUG) {
           //MLog.d(TAG, "onBackPressed");
        }

        backAction();
    }

    @SuppressLint("NewApi")
    private void backAction() {
        HistoryRecord record = getTaskManager().getLatestRecord();
        if (record == null) {
            finish();
            return;
        }
        Bundle backArgs = null;
        Page topPage = null;
        if (record.taskName.equals(((Object) this).getClass().getName())) {
            if (!mNavigator.pageStack.isEmpty()) {
                topPage = mNavigator.pageStack.peek();
                if (topPage.onBackPressed()) {
                    return;
                }
            }
        }
        // for QA monkey test
        /*if (CstmConfig.isMonkeyTest(this) && Build.VERSION.SDK_INT >= 8) {
            final boolean isMonkeyTest = ActivityManager.isUserAMonkey();
            if (isMonkeyTest) {
                final Stack<HistoryRecord> records = getTaskManager().getHistoryRecords();
                HistoryRecord rootRecord = getTaskManager().getRootRecord();
                if (rootRecord != null && records != null && records.size() == 1 && records.peek().equals(rootRecord)) {
                    return;
                }
            }
        }*/
        // monkey test end

        if (topPage != null) {
            backArgs = topPage.getBackwardArguments();
        }
        if (!goBack(backArgs)) {
            getTaskManager().onGoBack();
        }
    }

    public void setActivityLifecycleCallbacks(ActivityLifecycleCallbacks callbacks) {
        this.activityLifecycleCallbacks = callbacks;
    }

    private void addDebugView() {
        makeSettingEntity();
        makeDebugHandler();
        debugHandler.removeMessages(MESSAGE_ID_ADDCONTENT);
        debugHandler.sendEmptyMessage(MESSAGE_ID_ADDCONTENT);
    }

    private void makeSettingEntity() {
        if (settingEntity == null) {
            settingEntity = DebugUISetting.getInstance(this);
        }
    }

    private void makeDebugHandler() {
        if (debugHandler == null) {
            debugHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case MESSAGE_ID_ADDCONTENT: {

                            if (getWindow().peekDecorView() == null) {
                                debugHandler.removeMessages(MESSAGE_ID_ADDCONTENT);
                                debugHandler.sendEmptyMessageDelayed(MESSAGE_ID_ADDCONTENT, MESSAGE_DELAY_MS);
                            } else {
                                if (debugView == null) {
                                    debugView = new DebugView(getApplicationContext());
                                    addContentView(debugView,
                                            new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                                    FrameLayout.LayoutParams.WRAP_CONTENT));
                                }
                            }

                            break;
                        }

                        case MESSAGE_ID_UPDATE_PAGE_NAME: {
                            if (debugView == null) {
                                addDebugView();
                                Message message = debugHandler.obtainMessage(MESSAGE_ID_UPDATE_PAGE_NAME, msg.obj);
                                debugHandler.removeMessages(MESSAGE_ID_UPDATE_PAGE_NAME);
                                debugHandler.sendMessageDelayed(message, MESSAGE_DELAY_MS);
                            } else {
                                debugView.showPageNameView.setText(msg.obj.toString());
                            }

                            break;
                        }

                        case MESSAGE_ID_UPDATE_PAGE_VELOCITY: {
                            if (debugView == null) {
                                addDebugView();
                                Message message = debugHandler.obtainMessage(MESSAGE_ID_UPDATE_PAGE_VELOCITY, msg.obj);
                                debugHandler.removeMessages(MESSAGE_ID_UPDATE_PAGE_VELOCITY);
                                debugHandler.sendMessageDelayed(message, MESSAGE_DELAY_MS);
                            } else {
                                debugView.showPageVelocityView.setText(msg.obj.toString());
                            }
                            break;
                        }

                        default:
                            super.handleMessage(msg);
                    }
                }
            };
        }
    }

    public DebugUI getDebugUI() {
        if (DEBUG){
            return this;
        } else {
            return null;
        }
    }

    public void setGpsSwitcher(GPSSwitcher gpsSwitcher) {
        this.gpsSwitcher = gpsSwitcher;
        mNavigator.setPageGPSSwitcher(gpsSwitcher);
    }

    private void reportActivityLifecycleCallbacks(ActivityLifecycleCallbacks.TYPE type, Activity activity, Bundle
            bundle) {
        if (activityLifecycleCallbacks == null) {
            activityLifecycleCallbacks = TaskManagerFactory.getTaskManager().getActivityLifecycleCallbacks();
        }
        if (activityLifecycleCallbacks != null) {
            switch (type) {
                case CREATED:
                    activityLifecycleCallbacks.onActivityCreated(activity, bundle);
                    break;
                case STARTED:
                    activityLifecycleCallbacks.onActivityStarted(activity);
                    break;
                case RESUMED:
                    activityLifecycleCallbacks.onActivityResumed(activity);
                    break;
                case PAUSED:
                    activityLifecycleCallbacks.onActivityPaused(activity);
                    break;
                case STOPPED:
                    activityLifecycleCallbacks.onActivityStopped(activity);
                    break;
                case SAVEINSTANCESTATE:
                    activityLifecycleCallbacks.onActivitySaveInstanceState(activity, bundle);
                    break;
                case DESTROYED:
                    activityLifecycleCallbacks.onActivityDestroyed(activity);
                    break;
                default:
                    break;
            }
        }
    }

}
