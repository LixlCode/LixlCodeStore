package com.wecome.demoandroid.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wecome.demoandroid.R;
import com.wecome.demoandroid.WebServer;
import com.wecome.demoandroid.adapter.MainRecyclerViewAdapter;
import com.wecome.demoandroid.base.MainBaseFragment;
import com.wecome.demoandroid.demoutils.UiUtil;
import com.wecome.demoandroid.demoutils.response.MainInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 * 首页主界面
 */

public class OnePagerFragment extends MainBaseFragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context mContext;
    private List<MainInfoBean> data = new ArrayList<>();
    private MainRecyclerViewAdapter adapter;

    private static final int TOTAL_COUNTER = 18;
    private static final int PAGE_SIZE = 10;
    private int delayMillis = 4000;
    private int mCurrentCounter = 0;
    private boolean isError;
    private boolean mLoadMoreEndGone = false;

    /**
     * @return 返回首页的布局，做一些页面初始化的事
     */
    @Override
    protected View initView() {
        mContext = getActivity();
        View view = View.inflate(mActivity, R.layout.main_fragment_one, null);
        mRecyclerView = view.findViewById(R.id.rv_recyclerView);
        mSwipeRefreshLayout = view.findViewById(R.id.srl_swipeRefreshLayout);

        getData();

        //初始化RecyclerView
        initRecyclerView();

        return view;
    }

    private void getData() {
        //从模拟服务器中获取数据
        data = WebServer.getMultiNewsList();
        mCurrentCounter = data.size();
    }

    /**
     * 初始化RecyclerView,它有三种布局管理器，如下：
     * 1、LinearLayoutManager:线性布局管理器，支持水平和垂直效果。
     * 2、GridLayoutManager:网格布局管理器，支持水平和垂直效果。
     * 3、StaggeredGridLayoutManager:分布型管理器，瀑布流效果
     */
    private void initRecyclerView() {
        //创建RecyclerView布局管理者，确定排列方式（线性，网格，瀑布流）
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        //StaggeredGridLayoutManager layoutManager =
        //        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //设置布局管理器要排列的方式（横向或者纵向）默认是纵向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //给当前的RecyclerView设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        //给RecyclerView设置适配器
        adapter = new MainRecyclerViewAdapter(data);
        mRecyclerView.setAdapter(adapter);

        //RecyclerView条目点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UiUtil.showToast("点击" + position);
            }
        });

        //RecyclerView下拉刷新
        //设置进度条的颜色主题，最多能设置四种 加载颜色是循环播放的，只要没有完成刷新就会一直循环
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED);
        // 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeRefreshLayout.setDistanceToTriggerSync(200);
        // 设定下拉圆圈的背景
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        // 设置圆圈的大小
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT/*使用了默认的样式  LARGE,大的样式*/);
        // 设置下拉刷新的监听
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setEnableLoadMore(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //获取新的数据
                        adapter.setNewData(data);
                        isError = false;
                        mCurrentCounter = PAGE_SIZE;
                        mSwipeRefreshLayout.setRefreshing(false);
                        adapter.setEnableLoadMore(true);
                    }
                }, delayMillis);
            }
        });

        //RecyclerView上拉加载更多
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setEnabled(false);
                        if (data.size() < PAGE_SIZE){
                            adapter.loadMoreEnd(true);
                        }else {
                            if (mCurrentCounter >= TOTAL_COUNTER){
                                //数据加载完毕
                                //adapter.loadMoreEnd();
                                //true is gone, false is visible
                                adapter.loadMoreEnd(mLoadMoreEndGone);
                            }else {
                                if (isError){
                                    //成功获取到更多数据
                                    adapter.addData(data);
                                    mCurrentCounter = data.size();
                                    adapter.loadMoreComplete();
                                }else {
                                    //获取更多数据的时候出错了
                                    isError = true;
                                    /*Toast.makeText(mContext,
                                            "加载更多失败,点我重试", Toast.LENGTH_SHORT).show();*/
                                    adapter.loadMoreFail();
                                }
                            }
                            mSwipeRefreshLayout.setEnabled(true);
                        }
                    }
                },delayMillis);
            }
        },mRecyclerView);

        //条目上子控件的点击事件
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                UiUtil.showToast("点击子控件"+position);
            }
        });

    }

}
