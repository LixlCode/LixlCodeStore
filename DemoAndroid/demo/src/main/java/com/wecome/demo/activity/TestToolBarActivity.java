package com.wecome.demo.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wecome.demo.R;
import com.wecome.demo.adapter.FeasibleHeaderViewPagerAdapter;
import com.wecome.demo.base.BaseActivity;

import java.util.ArrayList;

public class TestToolBarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置到BaseActivity中的content中
        setContentLayout(R.layout.activity_toolbar);
        // 设置标题
        setTitle("ToolBar测试");
        // 设置返回按钮和点击事件
        setBackArrow();
        // 显示menu菜单
        setMenuShow(true);
        // 设置menu菜单的显示和点击事件
        setToolBarMenuOnclick(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_search:
                        Toast.makeText(TestToolBarActivity.this, "search", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_notification:
                        Toast.makeText(TestToolBarActivity.this, "edit", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_settings:
                        Toast.makeText(TestToolBarActivity.this, "settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_about:
                        Toast.makeText(TestToolBarActivity.this, "about", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        setHeader();
    }


    private void setHeader() {
        // 给RecyclerView添加第二个头部
        ArrayList<Drawable> headData = new ArrayList<>();
        headData.add(getResources().getDrawable(R.mipmap.icon_bg_2));
        headData.add(getResources().getDrawable(R.mipmap.icon_bg_3));
        headData.add(getResources().getDrawable(R.mipmap.icon_bg_4));

        ViewPager mViewPager = findViewById(R.id.vp_view_page_9);

        ArrayList<ImageView> iv = new ArrayList<>();
        for (int i = 0; i < headData.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageDrawable(headData.get(i));
            iv.add(imageView);
        }

        FeasibleHeaderViewPagerAdapter viewPagerAdapter = new FeasibleHeaderViewPagerAdapter(this, iv);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setPageMargin(30); // 设置item之间的间距
        mViewPager.setOffscreenPageLimit(3); // 缓存页面，为了加载流畅
        mViewPager.setPageTransformer(true, new CustPagerTransformer(this)/*RotatePageTransformer()*/);
    }


    public class RotatePageTransformer implements ViewPager.PageTransformer {

        private int degress = 25;

        @Override
        public void transformPage(@NonNull View page, float position) {
            int width = page.getWidth();
            int height = page.getHeight();
            // page ViewPager的条目 当前条目和预加载的条目
            // position 条目移动的位置

            if (position < -1) {// <-1 条目划出预加载长度，被viewpager删除
                page.setRotation(0);
            } else if (position < 0) {// -1~0 当前显示的条目滑到左边，或者左相邻条目滑动到显示

                page.setRotation(position * degress);
                // 设置旋转的中心点为 控件下边中心
                page.setPivotX(width / 2);
                page.setPivotY(height);

            } else if (position <= 1) {// 0~1 当前显示的条目滑到右边，或者右相邻条目滑动到显示

                page.setRotation(position * degress);
                // 设置旋转的中心点为 控件下边中心
                page.setPivotX(width / 2);
                page.setPivotY(height);

            } else {// > 1 条目划出预加载长度，被viewpager删除
                page.setRotation(0);
            }
        }
    }


    public class CustPagerTransformer implements ViewPager.PageTransformer {

        private int maxTranslateOffsetX;
        private ViewPager viewPager;

        CustPagerTransformer(Context context) {
            this.maxTranslateOffsetX = dp2px(context, 180);
        }

        public void transformPage(@NonNull View view, float position) {
            if (viewPager == null) {
                viewPager = (ViewPager) view.getParent();
            }
            int leftInScreen = view.getLeft() - viewPager.getScrollX();
            int centerXInViewPager = leftInScreen + view.getMeasuredWidth() / 2;
            int offsetX = centerXInViewPager - viewPager.getMeasuredWidth() / 2;
            float offsetRate = (float) offsetX * 0.38f / viewPager.getMeasuredWidth();
            float scaleFactor = 1 - Math.abs(offsetRate);
            if (scaleFactor > 0) {
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setTranslationX(-maxTranslateOffsetX * offsetRate);
            }
        }
        /**
         * dp和像素转换
         */
        private int dp2px(Context context, float dipValue) {
            float m = context.getResources().getDisplayMetrics().density;
            return (int) (dipValue * m + 0.5f);
        }
    }

}
