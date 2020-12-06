package com.wecome.demoandroid.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wecome.demoandroid.R;
import com.wecome.demoandroid.adapter.ThreeRecyclerViewAdapter;
import com.wecome.demoandroid.base.MainBaseFragment;

/**
 * Created by Administrator on 2017/8/3.
 * 生活主页面
 */

public class ThreePagerFragment extends MainBaseFragment {

    private RecyclerView mRecyclerView;
    private Context mContext;

    @Override
    protected View initView() {
        mContext = getActivity();
        View view = View.inflate(mActivity, R.layout.main_fragment_three, null);
        mRecyclerView = view.findViewById(R.id.rv_threeRecyclerView);

        //设置RecyclerView
        initRecyclerView();

        return view;
    }

    /**
     * 设置RecyclerView
     */
    private void initRecyclerView() {
        //创建RecyclerView管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //给当前RecyclerView设置这个线性管理器
        mRecyclerView.setLayoutManager(layoutManager);
        //给当前RecyclerView设置Adapter
        ThreeRecyclerViewAdapter threeRecyclerViewAdapter =
                new ThreeRecyclerViewAdapter(mContext);

        mRecyclerView.setAdapter(threeRecyclerViewAdapter);
    }

}
