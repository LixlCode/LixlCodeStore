package com.wecome.demo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class FeasibleHeaderViewPagerAdapter extends PagerAdapter{

    private Context mContext;
    private ArrayList<ImageView> headData;

    public FeasibleHeaderViewPagerAdapter(Activity mActivity, ArrayList<ImageView> headData) {
        this.headData = headData;
        this.mContext = mActivity;
    }

    //返回有效的View的个数
    @Override
    public int getCount() {
        return headData == null ? 0 : headData.size();
    }

    //判断是否page view与 instantiateItem(ViewGroup, int)返回的object的key 是否相同，以提供给其他的函数使用
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    //instantiateItem该方法的功能是创建指定位置的页面视图。finishUpdate(ViewGroup)返回前，页面应该保证被构造好
    //返回值：返回一个对应该页面的object，这个不一定必须是View，但是应该是对应页面的一些其他容器
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView ivItem = headData.get(position);
        container.addView(ivItem);
        return ivItem;
    }


    //该方法的功能是移除一个给定位置的页面。
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }
}
