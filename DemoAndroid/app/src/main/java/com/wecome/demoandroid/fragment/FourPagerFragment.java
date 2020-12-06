package com.wecome.demoandroid.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.wecome.demoandroid.R;
import com.wecome.demoandroid.adapter.FourViewPagerAdapter;
import com.wecome.demoandroid.base.MainBaseFragment;
import com.wecome.demoandroid.fragment.fourapp.FourPagerFragmentFive;
import com.wecome.demoandroid.fragment.fourapp.FourPagerFragmentFour;
import com.wecome.demoandroid.fragment.fourapp.FourPagerFragmentOne;
import com.wecome.demoandroid.fragment.fourapp.FourPagerFragmentSeven;
import com.wecome.demoandroid.fragment.fourapp.FourPagerFragmentSix;
import com.wecome.demoandroid.fragment.fourapp.FourPagerFragmentThree;
import com.wecome.demoandroid.fragment.fourapp.FourPagerFragmentTwo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 * 应用主界面
 */

public class FourPagerFragment extends MainBaseFragment {

    private TabLayout mTabLayout;
    private ViewPager mFourViewPager;

    //返回自己的布局
    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.main_fragment_four, null);
        mTabLayout = view.findViewById(R.id.tl_tabLayout_app);
        mFourViewPager = view.findViewById(R.id.vp_four_viewPager);
        return view;
    }

    //更新布局，这里是为ViewPager上的Fragment准备数据
    @Override
    protected void initData() {
        super.initData();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FourPagerFragmentOne());
        fragments.add(new FourPagerFragmentTwo());
        fragments.add(new FourPagerFragmentThree());
        fragments.add(new FourPagerFragmentFour());
        fragments.add(new FourPagerFragmentFive());
        fragments.add(new FourPagerFragmentSix());
        fragments.add(new FourPagerFragmentSeven());

        //给ViewPager设置适配器
        mFourViewPager.setAdapter(new FourViewPagerAdapter(
                getFragmentManager(),fragments
        ));
        //设置TabLayout的显示模式
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //关联ViewPager和TabLayout
        mTabLayout.setupWithViewPager(mFourViewPager);
    }
}
