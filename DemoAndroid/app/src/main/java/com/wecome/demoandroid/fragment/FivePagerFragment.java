package com.wecome.demoandroid.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.wecome.demoandroid.R;
import com.wecome.demoandroid.base.MainBaseFragment;

/**
 * Created by Administrator on 2017/8/3.
 * 我的主界面
 */

public class FivePagerFragment extends MainBaseFragment {

    private SwipeRefreshLayout mRecyclerViewRefresh;

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.main_fragment_five, null);
        mRecyclerViewRefresh = view.findViewById(R.id.srl_refresh);
        initRefresh();
        return view;
    }

    private void initRefresh() {
        //设置下拉刷新
        mRecyclerViewRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新完成之后设置成false进度条就消失了
                mRecyclerViewRefresh.setRefreshing(true);

                mRecyclerViewRefresh.setRefreshing(false);
            }
        });
    }

}
