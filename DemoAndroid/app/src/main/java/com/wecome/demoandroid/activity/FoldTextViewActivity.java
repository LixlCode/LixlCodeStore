package com.wecome.demoandroid.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wecome.demoandroid.R;

public class FoldTextViewActivity extends
        AppCompatActivity implements View.OnClickListener{

    private LinearLayout ll_app_desc;
    private ImageView iv_app_desc_arrow;
    private TextView tv_desc;
    private int startHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fold_text);
        ll_app_desc = (LinearLayout) findViewById(R.id.ll_app_desc);
        iv_app_desc_arrow = (ImageView) findViewById(R.id.iv_app_desc_arrow);
        tv_desc = (TextView) findViewById(R.id.tv_app_desc);
        ll_app_desc.setOnClickListener(this);

        //为了得到应用详细信息显示时的高度，这里为tv_desc设置监听器
        tv_desc.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {

            //监听到View的布局发生变化是调用这个方法
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                startHeight = tv_desc.getHeight(); //获取到显示7行的高
                if (startHeight > 0) {
                    //为了节省内存，获取到想要的高之后一定要一处这个监听器
                    tv_desc.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_app_desc:
                //应用简介显示的开关
                descDisplayToggle();
                break;
        }
    }

    /**
     * 应用简介显示的开关,要么开，要么关
     */
    //应用简介是否完全展示，true是完全展示
    private boolean discIsOpen;

    private void descDisplayToggle() {
        //设置最大显示行
        tv_desc.setMaxLines(1000);

        //获取简介内容所有行都显示时的高度
        int allLineHeight = getAllLineHeight();
        //利用第三方的框架，创建渐变动画对象
        ValueAnimator valueAnimator;
        if (discIsOpen) {
            //如果原来简介信息是全部显示，就隐藏一部分，只显示7行
            valueAnimator = ValueAnimator.ofInt(allLineHeight, startHeight);
            //显示向下的箭头
            iv_app_desc_arrow.setImageResource(R.mipmap.arrow_down);
        }else{
            //否则说明原来的简介信息是只显示了7行，就让他全部显示
            valueAnimator = ValueAnimator.ofInt(startHeight, allLineHeight);
            //显示向下的箭头
            iv_app_desc_arrow.setImageResource(R.mipmap.arrow_up);
        }

        //添加监听器，获取ValueAnimator模拟出来的数据
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //获取模拟出来的高
                int height = (Integer) valueAnimator.getAnimatedValue();
                //把高设置给简介内容的TextView
                tv_desc.getLayoutParams().height = height;
                //赋完值之后不会立即生效，所以要通知view刷新布局参数
                tv_desc.requestLayout();

                /*if (discIsOpen) {
                    //表示在原来的基础上滚动---让Txtview出来多少就向上滚动多少
                    scroll_view.scrollBy(0, height);
                }*/
            }
        });
        discIsOpen = !discIsOpen;
        //动画的时间--从隐藏到显示或者从显示到隐藏的动画
        valueAnimator.setDuration(200);
        valueAnimator.start();
    }

    /**
     * 获取简介内容所有行都显示时的高度
     * @return
     */
    private int getAllLineHeight() {
        TextView textView = new TextView(this);
        textView.setTextSize(14);//这个字体大小要和布局中的保持一致
        CharSequence text = tv_desc.getText();//取出简介信息的所有文本
        textView.setText(text);//将所有的文本显示到模拟的TextView上

        //指定测量规格---宽度和原来的textView保持一致，模式为精确
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(tv_desc.getWidth(), View.MeasureSpec.EXACTLY);
        //高度时未指定，可以是任意高度
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        //将以上的宽和高设置给模拟的TextView---手动调用模拟出来的TextView
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        //返回每个应用最终显示所有简介信息时TextView的高度
        return textView.getMeasuredHeight();
    }

}
