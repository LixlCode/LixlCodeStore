package com.wecome.demoandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.wecome.demoandroid.R;
import com.wecome.demoandroid.demoutils.LogUtil;
import com.wecome.demoandroid.demoutils.SpUtil;

/**
 * Created by Administrator on 2017/8/4.
 * 打开应用的时的欢迎页面
 */
public class WelcomeActivity extends Activity {

    private ImageView mFirstWelcome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_welcome_activity);
        mFirstWelcome = findViewById(R.id.iv_first_welcome);

        //给欢迎页面的图片设置动画
        initAnimate();
    }

    /**
     * 给欢迎页面的图片设置动画
     */
    private void initAnimate() {
        //① 设置旋转动画---基于中心点顺时针旋转
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // 给动画设置时间
        rotateAnimation.setDuration(1000);
        // 设置动画结束时的位置---让动画保持结束时的位置
        rotateAnimation.setFillAfter(true);

        //② 设置缩放动画---从无到完全展示
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // 给动画设置时间
        scaleAnimation.setDuration(1000);
        // 设置动画结束时的位置---让动画保持结束时的位置
        scaleAnimation.setFillAfter(true);

        // ③设置渐变动画---从无到有
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        // 给动画设置时间
        alphaAnimation.setDuration(2000);
        // 设置动画结束时的位置---让动画保持结束时的位置
        alphaAnimation.setFillAfter(true);

        //④ 让三个动画同时执行---创建AnimationSet集合----参数表示是否共享一个差值器
        AnimationSet animationSet = new AnimationSet(false);
        //将三个动画添加进来
        //animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        //⑤ 开启动画
        mFirstWelcome.startAnimation(animationSet);

        //监听动画，当动画完成后切换界面
        animationSet.setAnimationListener(new FirstWelcomeAnimationListener());
    }

    /**
     * 监听动画，当动画完成后开始页面的跳转
     */
    private class FirstWelcomeAnimationListener implements Animation.AnimationListener{

        //动画开始的时候调用
        @Override
        public void onAnimationStart(Animation animation) {

        }

        //动画结束的时候调用
        @Override
        public void onAnimationEnd(Animation animation) {
            //获取是否是第一次进入应用的标识
            boolean mIsAppFirstOpen = SpUtil.getBoolean(
                    getApplicationContext(), SpUtil.FILENAME, SpUtil.IS_APP_FIRST_OPEN);
            if (mIsAppFirstOpen){
                //是第一次进入应用----跳转到引导用户界面
                LogUtil.e("lxl","是第一次进入应用----跳转到引导用户界面");
                startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
            }else {
                //不是第一次进入引用---跳转到主界面
                LogUtil.e("lxl","不是第一次进入引用---跳转到主界面");
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            }
            //关闭监听---它的监听任务完成了
            finish();
        }

        //动画重复的时候调用
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
