package com.wecome.demoandroid.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.wecome.demoandroid.R;
import com.wecome.demoandroid.adapter.SecondViewPagerAdapter;
import com.wecome.demoandroid.base.MainBaseFragment;
import com.wecome.demoandroid.fragment.secondnews.SecondPagerFragmentFive;
import com.wecome.demoandroid.fragment.secondnews.SecondPagerFragmentFour;
import com.wecome.demoandroid.fragment.secondnews.SecondPagerFragmentOne;
import com.wecome.demoandroid.fragment.secondnews.SecondPagerFragmentThree;
import com.wecome.demoandroid.fragment.secondnews.SecondPagerFragmentTwo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 * 资讯主界面
 */

public class TwoPagerFragment extends MainBaseFragment {

    private TabLayout mTabLayout;
    private ViewPager mTwoViewPager;

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.main_fragment_two, null);
        mTabLayout = view.findViewById(R.id.tl_tabLayout);
        mTwoViewPager = view.findViewById(R.id.vp_two_viewPager);
        return view;
    }

    /** 更新该界面上Fragment上面的5个界面,
     *  准备5个页面作为ViewPager的数据
     */
    @Override
    protected void initData() {
        super.initData();
        //首先创建集合，将这个五个页面存储到集合中
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new SecondPagerFragmentOne());
        fragments.add(new SecondPagerFragmentTwo());
        fragments.add(new SecondPagerFragmentThree());
        fragments.add(new SecondPagerFragmentFour());
        fragments.add(new SecondPagerFragmentFive());

        //给ViewPager设置适配器
        mTwoViewPager.setAdapter(new SecondViewPagerAdapter(
                getFragmentManager(),fragments
        ));

        //设置TabLayout的显示模式
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        //关联ViewPager和TabLayout
        mTabLayout.setupWithViewPager(mTwoViewPager,true);
    }

}
