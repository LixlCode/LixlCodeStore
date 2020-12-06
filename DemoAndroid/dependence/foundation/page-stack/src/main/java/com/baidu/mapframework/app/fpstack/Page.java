package com.baidu.mapframework.app.fpstack;

import com.baidu.mapframework.app.mvc.View;
//import com.baidu.mapframework.voice.sdk.model.VoiceResult;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

/**
 * 页面抽象接口
 * </p>
 *
 * @author liguoqing
 * @version 1.0
 * @date 13-5-26 下午1:44
 */
public interface Page extends View {

    enum PageStyle {
        WHITE, BLACK
    }

    /**
     * 设置页面tag
     *
     * @param pageTag 页面tag
     */
    void setPageTag(String pageTag);

    /**
     * 获取页面Tag
     *
     * @return 页面Tag
     */
    String getPageTag();

    Task getTask();

    /**
     * 回退操作处理，该方法由框架调用。在 {@link android.app.Activity#onBackPressed()}中调用
     * <p/>
     * 如果需要手动操作页面回退，调用 {@link Task#goBack(android.os.Bundle)} 或 {@link Task#goBack()}
     *
     * @return
     */
    boolean onBackPressed();

    void onGoBack();

    /**
     * <p>在{@link com.baidu.mapframework.app.fpstack.TaskManagerImpl#removeStackRecord(HistoryRecord)}中调用<p/>
     * <p>有很多情况下会手动清空页面栈，被清理的页面缺失了正常的生命周期，这时需要一个方法来进行资源清理和内存释放，类似{@link Fragment#onDestroy()}</p>
     * <p>在正常的生命周期时(如back)，{@link Fragment#onDestroy()}与此方法都会被调用</p>
     * <p>由于{@link FragmentTransaction#commit()}的执行流程，无法确定{@link Fragment#onDestroy()}与此方法的调用顺序，注意逻辑判断</p>
     */
    void onRemoveHistoryStack();

    /**
     * 获取页面参数
     *
     * @return
     */
    Bundle getPageArguments();

    /**
     * 设置页面参数
     */
    void setPageArguments(Bundle args);

    /**
     * 设置 传递到要回退到的目标页面的参数
     *
     * @param args
     */
    void setBackwardArguments(Bundle args);

    /**
     * 获取传递到要回退到的目标页面的参数
     *
     * @return
     */
    Bundle getBackwardArguments();

    /**
     * 是否是栈回退时进入的该页面
     *
     * @return
     */
    boolean isNavigateBack();

    /**
     * 其他页面回退到该页面时的回调
     *
     * @param args
     */
    void onBackFromOtherPage(Bundle args);

    /**
     * 获取自定义进出动画.
     * 无动画指定为0.
     *
     * @return int[0] - int[3]对应:
     * <li>[0]进入时, 本Page的进入动画</li>
     * <li>[1]进入时, 被替换的Page的退出动画</li>
     * <li>[2]退出时, 替换Page的进入动画</li>
     * <li>[3]退出时, 本Page的退出动画</li>
     */
    int[] getCustomAnimations();

    /**
     * 指定其它Page back回本Page时, 本Page的进入动画, 是否由{@link #getCustomAnimations()}[2]覆盖.
     * 如果Page有自己的动画, 不希望其它界面指定动画时, 需要return false, 并重写{@link #getCustomAnimations()}返回动画效果.
     *
     * @return 默认返回true, 可以覆盖.
     */
    boolean shouldOverrideCustomAnimations();

    /**
     * 获取页面性能统计Tag
     *
     * @return 该页面性能统计的Tag
     */
    String getPageLogTag();

    /**
     * 设置该页面下默认的屏幕方向
     *
     * @return
     *
     * @see {@link ActivityInfo#SCREEN_ORIENTATION_PORTRAIT} {@link ActivityInfo#SCREEN_ORIENTATION_LANDSCAPE}
     */
    int getDefaultRequestedOrientation();

    /**
     * 是否指定该页面下默认的屏幕方向
     *
     * @return 默认返回false，需要固定该页面下屏幕方向时，返回true，并实现{@link #getDefaultRequestedOrientation}
     */
    boolean shouldOverrideRequestedOrientation();

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    boolean supportFullScreen();

    int statusBarColor();

    PageStyle pageStyle();

    int voiceTopMargin();

    String infoToUpload();

    //void handleVoiceResult(VoiceResult voiceResult);

}
