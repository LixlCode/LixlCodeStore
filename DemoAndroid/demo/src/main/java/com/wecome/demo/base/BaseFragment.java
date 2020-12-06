package com.wecome.demo.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/8/3.
 * App五个页面的base类
 */

public abstract class BaseFragment extends Fragment{

    protected Activity mActivity;
    protected Context mContext;

    /**
     * 此方法要求子类返回控件，以便添加到activity上
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){

        // 获取到Fragment附着的Activity
        mActivity = getActivity();
        // 单例模式需要传Application的Context
        if (getContext() != null) {
            mContext = getContext().getApplicationContext();
        }

        /**
         * 不在这更新控件，因为这个方法的作用是返回控件，添加添加到Activity身上，
         * 需要马上把控件返回出去，让Activity快速展示出来.
         * 如果在这处理业务，更新界面，会导致Activity展示控件 比较慢.
         */
        // 要求子类必须返回自己的控件，写抽象方法让子类必须覆盖
        return initView();
    }

    /**
     * 返回控件或布局方法的实现，抽象方法，要求子类必须重写
     */
    protected abstract View initView();

    /**
     * 当Activity创建完成时调用，在这方法中更新控件
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //创建更新控件的方法
        initData();
    }

    /**
     * 让子类实现，更新自己的控件或布局，不是必须实现的
     * 		如果布局是写死的就不必更新，所以这个方法子类不一定必须实现
     */
    protected void initData() {
        // TODO Auto-generated method stub
    }

}
