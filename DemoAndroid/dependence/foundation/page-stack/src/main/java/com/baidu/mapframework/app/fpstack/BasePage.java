package com.baidu.mapframework.app.fpstack;

import java.lang.reflect.Field;
import java.util.Observable;

import com.baidu.mapframework.app.map.GPSSwitcher;
import com.baidu.mapframework.app.map.LayerInterface;
import com.baidu.mapframework.app.map.LayerSwitcher;
//import com.baidu.mapframework.common.config.GlobalConfig;
import com.baidu.mapframework.common.util.DeviceHelper;
import com.baidu.mapframework.common.util.ScreenUtils;
//import com.baidu.mapframework.nirvana.NirvanaFramework; lxl
//import com.baidu.mapframework.nirvana.schedule.LifecycleManager;lxl
//import com.baidu.mapframework.nirvana.schedule.UITaskType;lxl
import com.baidu.mapframework.scenefw.ScenePage;
//import com.baidu.mapframework.voice.sdk.model.VoiceResult;
//import com.baidu.mobstat.StatService;
//import com.baidu.platform.comapi.DebugConfig;
//import com.baidu.platform.comapi.JNIInitializer;
//import com.baidu.platform.comapi.util.MLog;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;

/**
 * 页面基类实现
 * <p>
 * 页面基类使用时注意View的生命周期和整个Fragment的生命周期
 * </p>
 *
 * @author liguoqing
 * @version 1.0
 * @date 13-5-26 下午3:47
 */
public class BasePage extends Fragment implements Page, LayerInterface.Switcher {

    private static final String TAG = "BMUISTACK:BasePage";
    private static final boolean DEBUG = /*DebugConfig.DEBUG*/false;

    private static final String STATE_BACK_KEY = "BasePage.is_back";
    private static final String STATE_BACK_ARGS = "BasePage.back_args";
    private static final String STATE_PAGE_TAG = "BasePage.page_tag";

    /**
     * 页面回退标志
     */
    protected boolean mIsBack = false;
    protected Bundle mBackArgs = null;
    // 同一实例重复跳转
    protected boolean mIsRelaunched = false;

    private Bundle mRelaunchedArgs = null;

    /**
     * 延迟加载的 Handler
     */
    private Handler handler = new Handler(Looper.getMainLooper());

    private GPSSwitcher gpsSwitcher;

    protected interface DelayedTask extends Runnable {
        /**
         * 如果返回值等于或者小于 0 则没有延迟间隔。
         *
         * @return
         */
        long getDelayDelta();
    }

    protected abstract class SafeDelayedTask implements DelayedTask {
        @Override
        public final void run() {
            if (!isDetached() && getActivity() != null) {
                doSomething();
            }
        }

        public abstract void doSomething();
    }

    protected abstract class NoDeltaSafeDelayedTask extends SafeDelayedTask {
        @Override
        public final long getDelayDelta() {
            return 0;
        }
    }

    /**
     * 如果有需要延迟加载的模块代码，请重写此方法。
     *
     * @return
     */
    protected DelayedTask onPostDelayTask() {
        return null;
    }

    /**
     * 优化 View 加载所需代码
     */
    protected View pageContent;

    /**
     * 注意：该方法不是 {@link #onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)}
     * 的替代方法。但是如果该方法不返回 null 则会影响 onCreateView 方法的执行逻辑。
     * 主要是方便子类编写惰性加载逻辑。<br/>
     * 注意：一旦重写本方法，则不需要重写 onCreateView 方法。
     * 另外，如果同时重写了 onCreateView 和 {@link #onDestroyView()} 则必须返回或者调用 super 的同名方法。
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return
     */
    protected View onCreatePageContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    /**
     * 将子 View 的 findViewById 过程集中优化。
     * 当然为了兼容性考虑不做强制要求。
     *
     * @param view
     */
    protected void onFindViews(View view) {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final PageInfo pageInfo = new PageInfo();
        pageInfo.classname = getClass().getCanonicalName();
        pageInfo.tag = getPageLogTag();
        pageInfo.tag = pageInfo.tag == null ? "" :
                (isNavigateBack() ? pageInfo.tag + "-" : pageInfo.tag);
        view.setTag(pageInfo);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (DEBUG) {
            //MLog.e(TAG, ((Object) this).getClass().getSimpleName()
            //        + " onConfigurationChanged " + newConfig.orientation);
        }

        View newContent = buildOrientationContentView(newConfig);

        ViewGroup rootView = (ViewGroup) getView();
        if (newContent != null) {
            // Remove all the existing views from the root view.
            rootView.removeAllViews();
            rootView.addView(newContent);
            updateOrientationUI(newConfig);
        }
    }

    private String pageTag = PageFactory.DEFAULT_PAGE_TAG;
    private Task mTask;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState(outState);
    }

    private void saveState(Bundle outState) {
        outState.putBoolean(STATE_BACK_KEY, mIsBack);
        outState.putString(STATE_PAGE_TAG, pageTag);
        if (mBackArgs != null) {
            outState.putBundle(STATE_BACK_ARGS, mBackArgs);
        }
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        mIsBack = savedInstanceState.getBoolean(STATE_BACK_KEY);
        pageTag = savedInstanceState.getString(STATE_PAGE_TAG);
        mBackArgs = savedInstanceState.getBundle(STATE_BACK_ARGS);

        FragmentActivity activity = getActivity();
        if (activity instanceof BaseTask) {
            mTask = (BaseTask) activity;
        }

    }

    public void setTask(Task task) {
        this.mTask = task;
    }

    @Override
    public void setPageTag(String pageTag) {
        this.pageTag = pageTag;

    }

    @Override
    final public String getPageTag() {
        return this.pageTag;
    }

    @Override
    public Task getTask() {
        return mTask;
    }

    @Override
    public void onGoBack() {
    }

    @Override
    public void onRemoveHistoryStack() {
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public Bundle getPageArguments() {
        return getArguments();
    }

    final void setPageArgumentsInside(Bundle args) {
        changeFullScreenStatus();
        setArguments(args);
        setPageArguments(args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPageArguments(Bundle args) {

    }

    private void changeFullScreenStatus() {
        if (Build.VERSION.SDK_INT >= 21) {
            Activity activity = TaskManagerFactory.getTaskManager().getContainerActivity();
            Window window = activity.getWindow();
            window.setStatusBarColor(statusBarColor());
            if (supportFullScreen()) {
                if (Build.VERSION.SDK_INT >= 23) {
                    window.getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    DeviceHelper.setStatusBarLightMode(window, pageStyle() != PageStyle.BLACK);
                } else {
                    activity.getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                DeviceHelper.setStatusBarLightMode(window, false);
            }
        }
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

    }

    final void setBackwardArgumentsInside(Bundle args) {
        changeFullScreenStatus();
        setBackwardArguments(args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBackwardArguments(Bundle args) {
        mBackArgs = args;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bundle getBackwardArguments() {
        return mBackArgs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNavigateBack() {
        return mIsBack;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackFromOtherPage(Bundle args) {
    }

    /**
     * 触发页面回退操作
     */
    public void goBack() {
        goBack(null);
    }

    /**
     * 触发页面回退操作
     */
    public void goBack(Bundle args) {
        Task task = getTask();
        if (task != null) {
            task.goBack(args);
        }
    }

    public void goBackToScene(String sceneId, Bundle args) {
        Bundle params;
        if (args == null) {
            params = new Bundle();
        } else {
            params = args;
        }
        params.putString(ScenePage.PAGE_SCENE_KEY, sceneId);
        goBack(params);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (DEBUG) {
            //MLog.e(TAG, ((Object) this).getClass().getSimpleName() + " onAttach");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*NirvanaFramework.getLifecycleManager().onUIStateChange(
                UITaskType.UIType.PAGE,
                this.getClass().getName(),
                LifecycleManager.UIState.ACTIVE
        );*/
        if (createInterface != null) {
            createInterface.onCreate();
            createInterface = null;
        }
        /**屏幕常亮的控制逻辑*/
        /*if (GlobalConfig.getInstance().isAllBright()) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            try {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } catch (Exception e) {
                // 解决偶现crash问题
                // android.view.ViewGroup$LayoutParams.height' on a null object reference
                // at com.android.internal.policy.PhoneWindow$DecorView.updateColorViewInt(PhoneWindow.java:3063)
            }
        }*/

        if (DEBUG) {
            //MLog.e(TAG, ((Object) this).getClass().getSimpleName() + " onCreate");
        }

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (DEBUG) {
            //MLog.e(TAG, ((Object) this).getClass().getSimpleName() + " onCreateView");
        }

        // 优化 View 的 inflate 过程，保证只执行一次。
        if (pageContent == null) {
            View view = onCreatePageContent(inflater, container, savedInstanceState);
            if (view != null) {
                pageContent = view;
                onFindViews(pageContent);
                return pageContent;
            }
        } else {
            return pageContent;
        }

        // 如果 pageContent == null 并且 onCreatePageContent 方法也返回 null
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
       /*if (GlobalConfig.getInstance().isInitMtj()) {
            StatService.onPageEnd(JNIInitializer.getCachedContext(), getPageLogTag());
        }*/
        if (DEBUG) {
            //MLog.e(TAG, ((Object) this).getClass().getSimpleName() + " onPause");
        }

        if (mTask != null && mTask instanceof BaseTask) {
            if (((BaseTask) mTask).getDebugUI() != null) {
                ((BaseTask) mTask).getDebugUI().updatePageName("");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (GlobalConfig.getInstance().isInitMtj()) {
            StatService.onPageStart(JNIInitializer.getCachedContext(), getPageLogTag());
        }*/
        if (DEBUG) {
            //MLog.e(TAG, ((Object) this).getClass().getSimpleName() + " onResume");
        }

        boolean shouldOverrideOrientation = this.shouldOverrideRequestedOrientation();
        if (getActivity() != null && !getActivity().isFinishing()) {
            if (shouldOverrideOrientation) {
                int orientation = getDefaultRequestedOrientation();
                getActivity().setRequestedOrientation(orientation);
            } else {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }

        //        String pageLogTag = getPageLogTag();
        //        if (!TextUtils.isEmpty(pageLogTag)) {
        //            PerformanceMonitor.getInstance().addEndTime(pageLogTag, System.currentTimeMillis());
        //        }

        boolean useGPS = changeGPSRequest();

        if (gpsSwitcher != null) {
            gpsSwitcher.enableGPS(useGPS);
        }

        if (mTask != null && mTask instanceof BaseTask) {
            if (((BaseTask) mTask).getDebugUI() != null) {
                ((BaseTask) mTask).getDebugUI().updatePageName(((Object) this).getClass()
                        .getCanonicalName());
            }

        }

        // 优化延迟加载的代码
        DelayedTask delayedTask = onPostDelayTask();
        if (!isDetached() && delayedTask != null) {
            long delayDelta = delayedTask.getDelayDelta();
            if (delayDelta <= 0) {
                handler.post(delayedTask);
            } else {
                handler.postDelayed(delayedTask, delayDelta);
            }
        }

    }

    /**
     * 变更gps注册状态, onResume时调用.
     * @return  useGPS
     */
    protected boolean changeGPSRequest() {
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (DEBUG) {
            //MLog.e(TAG, ((Object) this).getClass().getSimpleName() + " onStart");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (DEBUG) {
            //MLog.e(TAG, ((Object) this).getClass().getSimpleName() + " onStop");
        }
    }

    /**
     * Modified by HanSiwen
     * 增加将mChildFragmentManager置为null，子类在执行super.onDetach后不要再执行getChildFragmentManager
     * 因为使用了反射，所以今后替换support包时请验证此方法
     */
    @Override
    public void onDetach() {
        super.onDetach();
        if (DEBUG) {
            //MLog.e(TAG, ((Object) this).getClass().getSimpleName() + " onDetach");
        }
        mBackArgs = null;
        mIsBack = false;

        try {
            Field childFMField = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFMField.setAccessible(true);
            childFMField.set(this, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (DEBUG) {
            //MLog.e(TAG, ((Object) this).getClass().getSimpleName() + " onDestroy");
        }
        if (destroyInterface != null) {
            destroyInterface.onDestroy();
            destroyInterface = null;
        }
        mRelaunchedArgs = null;
        /*NirvanaFramework.getLifecycleManager().onUIStateChange(
                UITaskType.UIType.PAGE,
                this.getClass().getName(),
                LifecycleManager.UIState.DESTROYED
        );*/
    }

    @Override
    public void onDestroyView() {
        // 添加 View inflate 优化代码
        if (pageContent != null) {
            final ViewParent parent = pageContent.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(pageContent); // 防止 pageContent 被重复加载到不同的 ViewGroup 中
            }
        }
        super.onDestroyView();
        if (DEBUG) {
            //MLog.e(TAG, ((Object) this).getClass().getSimpleName() + " onDestroyView");
        }
    }

    /**
     * 根据横竖屏状态构建View，横竖屏切换不同布局时需要实现
     *
     * @return 所需的竖屏布局
     *
     * @see {@link BasePage#onConfigurationChanged(android.content.res.Configuration)}
     */
    public View buildOrientationContentView(Configuration newConfig) {
        return null;
    }

    /**
     * 更新UI界面，有横竖屏切换布局时需要实现
     * 在横竖屏切换后，更新相关的布局 @see {@link BasePage#onConfigurationChanged(android.content.res.Configuration)}
     */
    public void updateOrientationUI(Configuration newConfig) {
    }

    @Override
    public void update(Observable observable, Object data) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getCustomAnimations() {
        return new int[] {0, 0, 0, 0};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldOverrideCustomAnimations() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPageLogTag() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public int getDefaultRequestedOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public boolean shouldOverrideRequestedOrientation() {
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //MLog.d(TAG, "onHiddenChanged-" + hidden);
    }

    /**
     * @return 同一页面重复跳转的参数
     */
    public Bundle getRelaunchedArgs() {
        return mRelaunchedArgs;
    }

    public void setRelaunchedArgs(Bundle args) {
        mRelaunchedArgs = args;
        changeFullScreenStatus();
    }

    @Override
    public LayerSwitcher layerSwitcher() {
        return null;
    }

    private LayerInterface.Create createInterface;

    public void setCreateInterface(LayerInterface.Create createInterface) {
        this.createInterface = createInterface;
    }

    private LayerInterface.Destroy destroyInterface;

    public void setDestroyInterface(LayerInterface.Destroy destroyInterface) {
        this.destroyInterface = destroyInterface;
    }

    @Override
    public boolean supportFullScreen() {
        return false;
    }

    @Override
    public int statusBarColor() {
        if (supportFullScreen()) {
            if (Build.VERSION.SDK_INT >= 23) {
                return Color.TRANSPARENT;
            } else {
                return 0x2d000000;
            }
        } else {
            return Color.BLACK;
        }
    }

    @Override
    public PageStyle pageStyle() {
        return PageStyle.WHITE;
    }

    @Override
    public int voiceTopMargin() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ScreenUtils.dip2px(60) + ScreenUtils.getStatusBarHeightFullScreen(getContext());
        } else {
            return ScreenUtils.dip2px(60);
        }
    }

    @Override
    public String infoToUpload() {
        return "";
    }


    public void setComId(String comId) {

    }
   /* @Override
    public void handleVoiceResult(VoiceResult voiceResult) {

    }*/

    public void setGpsSwitcher(GPSSwitcher gpsSwitcher) {
        this.gpsSwitcher = gpsSwitcher;
    }

}

