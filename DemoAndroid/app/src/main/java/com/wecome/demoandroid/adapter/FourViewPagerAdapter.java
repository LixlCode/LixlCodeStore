package com.wecome.demoandroid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wecome.demoandroid.base.TwoBaseFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 * 二级页面Viewpager的适配器
 */

public class FourViewPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments;

    public FourViewPagerAdapter(FragmentManager fragmentManager,
                                List<Fragment> fragments) {
        super(fragmentManager);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        TwoBaseFragment baseFragment = (TwoBaseFragment) fragments.get(position);
        return baseFragment.getTitle();
    }
}
