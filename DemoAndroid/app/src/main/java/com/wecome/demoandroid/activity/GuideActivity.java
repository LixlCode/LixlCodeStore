package com.wecome.demoandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wecome.demoandroid.R;
import com.wecome.demoandroid.demoutils.SpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/4.
 * 首次进入应用时的引导页面
 */

public class GuideActivity extends
        AppCompatActivity implements View.OnClickListener{

    private List<ImageView> list;
    private LinearLayout mPointGray;
    private ImageView mPointRed;
    private Button mBtnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 去掉标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //加载布局文件
        setContentView(R.layout.first_guide_activity);
        //初始化控件
        initView();
    }

    /**
     * 初始化控件的方法的实现
     */
    private void initView() {
        //找到控件 ViewPager
        ViewPager mViewPagerGuide = (ViewPager) findViewById(R.id.vp_guide_bg);
        //找到控件---灰点
        mPointGray = (LinearLayout) findViewById(R.id.ll_point_gray);
        //找到控件---红点
        mPointRed = (ImageView) findViewById(R.id.iv_point_red);
        //找布局中的控件---立即体验的按钮
        mBtnNext = (Button) findViewById(R.id.btn_guide_next);
        mBtnNext.setOnClickListener(this);

        //初始化图片数据
        initData();
        //设置Adapter
        mViewPagerGuide.setAdapter(new MyViewPagerAdapter());

        //红点移动--根据手指移动的距离计算红点应该移动的距离
        //监听ViewPager,获取ViewPager移动的距离
        mViewPagerGuide.addOnPageChangeListener(new MyViewPageChangeListener());
    }

    /**
     * 红点的屏幕适配
     * 		因为控件中的单位dp和代码中值的单位px（像素）不是一致的，所以就出现了屏幕的适配
     * 	          这里我们创建一个dp装换成px的方法
     */
    public int dp2px(int dp){
        //px = dp * 密度比
        //获取手机的密度比
        float density = getResources().getDisplayMetrics().density;
        //返回手动四舍五入的值
        return (int) (density*dp+0.5);
    }

    /**
     * 创建ViewPager的监听器类
     */
    private class MyViewPageChangeListener
                    implements ViewPager.OnPageChangeListener {

        //当ViewPager滑动的时候调用
        @Override
        public void onPageScrolled(int position, float positionOffset,
                                             int positionOffsetPixels) {
            // 计算红点应该移动的距离
			/*
			 * 手指移动的距离/屏幕宽度 = 红点移动的距离/灰点的间距
			 * 红点移动的距离 = 手指移动的距离/屏幕宽度 * 灰点的间距
			 * 红点移动的距离 = positionOffset * 20=灰点自身的宽度+俩灰点的边距
			*/
            int redPointX = (int) ((positionOffset + position) * dp2px(20));

            //通过动态不停的设置控件的左边距实现红点控件的移动--- 不停的设置红点的左边距，达到红点移动的效果
            // 获取控件的宽高属性对象
            android.widget.RelativeLayout.LayoutParams params =
                    (android.widget.RelativeLayout.LayoutParams) mPointRed.getLayoutParams();
            // 修改左边距
            params.leftMargin = redPointX;
            //将红点的宽高属性设置给红点控件
            mPointRed.setLayoutParams(params);

        }

        //当ViewPager滑动到某一页的时候调用
        @Override
        public void onPageSelected(int position) {
            // 做判断，如果ViewPager滑动到最后一页了，就显示“立即体验”的按钮
            if (position == list.size()-1) {
                mBtnNext.setVisibility(View.VISIBLE);
            }else{
                mBtnNext.setVisibility(View.GONE);
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
            // TODO Auto-generated method stub
        }
    }

    /**
     * 创建适配ViewPager的内部类
     */
    private class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            // TODO Auto-generated method stub
            return view == object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 创建当前要展示的条目
            ImageView imageView = list.get(position);
            //把要展示的控件添加到ViewPager上
            container.addView(imageView);
            return imageView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView((View) object);
        }
    }

    /**
     * 初始化图片数据的方法的实现
     * 		----给Adatpter准备数据，把拷贝进来的3张图片，转换成ImageView
     */
    private void initData() {
        // 创建一个数组，将引导页面的三张图片的id存放到数组中，为后面将图片装换成ImageView对象做准备
        int[] imgIds = new int[]{R.mipmap.guide_1,R.mipmap.guide_2,R.mipmap.guide_3};
        //将图片转换成ImageView后需要保存起来，这里创建一个集合来保存
        list = new ArrayList<>();
        //利用for循环，将每一张图片转换成ImageView对象
        for (int i = 0; i < imgIds.length; i++) {
            //创建ImageView对象
            ImageView imageView = new ImageView(this);
            // 把图片设置给ImageView
            // 手动给ImageView设置缩放类型
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(imgIds[i]);
            //将转换成的ImageView添加到集合中
            list.add(imageView);

            //根据图片的数量，动态的生成灰点
            //创建灰点
            ImageView point = new ImageView(this);
            //加载灰点资源
            point.setBackgroundResource(R.drawable.guide_point_gray);

            //屏幕适配，将dp值转换成px
            int dp2px = dp2px(10);

            // 设置宽高---导包时需要看自己属于哪个父容器，要那个父容器的包
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px,dp2px);
            point.setLayoutParams(params);
            //设置灰点的边距
            if (i != 0) {
                params.leftMargin = dp2px;
            }
            //将创建好的灰点添加到ViewPager上
            mPointGray.addView(point);
        }
    }

    /**
     * "立即体验"按钮的点击事件的实现
     */
    @Override
    public void onClick(View view) {
        //当点击这个按钮的时候首保存“已进入过应用”的这种状态，然后跳转到主界面
        //保存状态
        SpUtil.putBoolean(getApplicationContext(),SpUtil.FILENAME,SpUtil.IS_APP_FIRST_OPEN,false);
        //进入主界面
        startActivity(new Intent(GuideActivity.this, MainActivity.class));

        //销毁此页面
        finish();
    }
}
