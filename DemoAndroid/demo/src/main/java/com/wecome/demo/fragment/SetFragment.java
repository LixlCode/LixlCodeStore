package com.wecome.demo.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wecome.demo.R;
import com.wecome.demo.activity.SettingCenterActivity;
import com.wecome.demo.adapter.SettingAdapter;
import com.wecome.demo.adapter.SettingCenterAdapter;
import com.wecome.demo.base.BaseFragment;
import com.wecome.demo.constants.SettingCenterConstants;
import com.wecome.demo.model.SettingCenterModel;
import com.wecome.demo.model.SettingPageModel;
import com.wecome.demo.utils.SimpleToolbar;

import java.util.ArrayList;
import java.util.List;

public class SetFragment extends BaseFragment implements ExpandableListView.OnChildClickListener{

    private SimpleToolbar simple_toolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private SettingAdapter adapter;
    private ExpandableListView mExpandableListView;

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_set, null);
        initSetPageHeadUi(view);
        initExpandableListView(view);
        // initSetPageList(view);
        return view;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initExpandableListView(View view) {
        mExpandableListView = view.findViewById(R.id.rv_settingList);
        adapter = new SettingAdapter(mActivity);
        // 准备数据
        setListData();
        // 申明字条目的点击事件
        mExpandableListView.setOnChildClickListener(this);
        // 主条目的点击事件
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        // 添加头部
        mExpandableListView.addHeaderView(mActivity.getLayoutInflater().inflate(R.layout.head_view, null));

        // 修改ExpandableListView不能继续网上滑动的bug
        mExpandableListView.setNestedScrollingEnabled(true);

        // 在groups list视图中展开组
        int groupCount = adapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            // 如果组被展开，返回True，否则返回false(如果组被展开，返回false，已经展开，返回false)
            mExpandableListView.expandGroup(i);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setListData() {
        SettingPageModel settingPageModel = null;
        ArrayList<SettingPageModel> list = new ArrayList<>();

        settingPageModel = new SettingPageModel(R.string.setting_keep_awake, SettingCenterConstants.SETTING_KEEP_AWAKE);
        // settingPageModel.setStyle(SettingCenterConstants.EXPANDLIST_STYLE_CHECKADBE);
        settingPageModel.setLeftImgResId(R.mipmap.tab_list_1);
        list.add(settingPageModel);

        settingPageModel = new SettingPageModel(R.string.setting_recieve_msg, SettingCenterConstants.SETTING_SHOW_IMG);
        // settingPageModel.setStyle(SettingCenterConstants.EXPANDLIST_STYLE_CHECKADBE);
        settingPageModel.setLeftImgResId(R.mipmap.tab_list_2);
        list.add(settingPageModel);
        adapter.setData(list);


        list = new ArrayList<SettingPageModel>();
        settingPageModel = new SettingPageModel(R.string.setting_sao, SettingCenterConstants.SETTING_RECIEVE_MSG);
        settingPageModel.setLeftImgResId(R.mipmap.tab_list_1);
        list.add(settingPageModel);

        settingPageModel = new SettingPageModel(R.string.setting_look, SettingCenterConstants.SETTING_REVOLVE_MAP);
        settingPageModel.setLeftImgResId(R.mipmap.tab_list_2);
        list.add(settingPageModel);

        settingPageModel = new SettingPageModel(R.string.setting_search, SettingCenterConstants.SETTING_CHANGE_VIEWPOINT);
        settingPageModel.setLeftImgResId(R.mipmap.tab_list_1);
        list.add(settingPageModel);
        adapter.setData(list);


        list = new ArrayList<SettingPageModel>();
        settingPageModel = new SettingPageModel(R.string.setting_update, SettingCenterConstants.SETTING_AUTO_UPDATE);
        settingPageModel.setLeftImgResId(R.mipmap.tab_list_3);
        list.add(settingPageModel);
        adapter.setData(list);


        list = new ArrayList<SettingPageModel>();
        settingPageModel = new SettingPageModel(R.string.setting_security, SettingCenterConstants.SETTING_SHAKE_VOICE);
        settingPageModel.setLeftImgResId(R.mipmap.tab_list_1);
        list.add(settingPageModel);

        settingPageModel = new SettingPageModel(R.string.setting_sug, SettingCenterConstants.SETTING_SHOW_MAP_BUBBLE);
        settingPageModel.setLeftImgResId(R.mipmap.tab_list_4);
        list.add(settingPageModel);

        settingPageModel = new SettingPageModel(R.string.setting_we, SettingCenterConstants.SETTING_CHOOSE_SD);
        settingPageModel.setLeftImgResId(R.mipmap.tab_list_2);
        list.add(settingPageModel);
        adapter.setData(list);


        mExpandableListView.setAdapter(adapter);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initSetPageList(View view) {
        List<SettingPageModel> data = new ArrayList<>();

//        SettingPageModel settingPageModel = new SettingPageModel();
//        settingPageModel.setStr("会员级别");
//        settingPageModel.setImg(getResources().getDrawable(R.mipmap.tab_list_1, null));
//        data.add(settingPageModel);
//
//        SettingPageModel settingPageModel1 = new SettingPageModel();
//        settingPageModel1.setStr("分享位置");
//        settingPageModel1.setImg(getResources().getDrawable(R.mipmap.tab_list_2, null));
//        data.add(settingPageModel1);
//
//        SettingPageModel settingPageModel2 = new SettingPageModel();
//        settingPageModel2.setStr("出行策略");
//        settingPageModel2.setImg(getResources().getDrawable(R.mipmap.tab_list_3, null));
//        data.add(settingPageModel2);
//
//        SettingPageModel settingPageModel3 = new SettingPageModel();
//        settingPageModel3.setStr("安全中心");
//        settingPageModel3.setImg(getResources().getDrawable(R.mipmap.tab_list_4, null));
//        data.add(settingPageModel3);
//
//        SettingPageModel settingPageModel4 = new SettingPageModel();
//        settingPageModel4.setStr("帮助");
//        settingPageModel4.setImg(getResources().getDrawable(R.mipmap.tab_list_2, null));
//        data.add(settingPageModel4);
//
//        SettingPageModel settingPageModel5 = new SettingPageModel();
//        settingPageModel5.setStr("扫一扫");
//        settingPageModel5.setImg(getResources().getDrawable(R.mipmap.tab_list_4, null));
//        data.add(settingPageModel5);
//
//        SettingPageModel settingPageModel6 = new SettingPageModel();
//        settingPageModel6.setStr("看一看");
//        settingPageModel6.setImg(getResources().getDrawable(R.mipmap.tab_list_3, null));
//        data.add(settingPageModel6);

//        SettingPageModel settingPageModel7 = new SettingPageModel();
//        settingPageModel7.setStr("手一抖");
//        settingPageModel7.setImg(getResources().getDrawable(R.mipmap.tab_list_4, null));
//        data.add(settingPageModel7);
//
//        SettingPageModel settingPageModel8 = new SettingPageModel();
//        settingPageModel8.setStr("浏览记录");
//        settingPageModel8.setImg(getResources().getDrawable(R.mipmap.tab_list_1, null));
//        data.add(settingPageModel8);
//
//        SettingPageModel settingPageModel9 = new SettingPageModel();
//        settingPageModel9.setStr("吐槽反馈");
//        settingPageModel9.setImg(getResources().getDrawable(R.mipmap.tab_list_4, null));
//        data.add(settingPageModel9);
//
//        SettingPageModel settingPageModel11 = new SettingPageModel();
//        settingPageModel11.setStr("关于");
//        settingPageModel11.setImg(getResources().getDrawable(R.mipmap.tab_list_3, null));
//        data.add(settingPageModel11);

//        RecyclerView mRecyclerView = view.findViewById(R.id.rv_settingList);
//        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
//        mRecyclerView.setLayoutManager(linearLayoutManager);
//        SettingAdapter adapter = new SettingAdapter(mActivity, R.layout.seting_page_list, data);
//        mRecyclerView.setAdapter(adapter);

        // 解决滑动不流畅的问题
//         mRecyclerView.setNestedScrollingEnabled(false);

//        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
//        adapter.addHeaderView(getLayoutInflater().inflate(R.layout.head_view, null));
        // adapter.addFooterView(getLayoutInflater().inflate(R.layout.foot_view, null));

    }

    private void initSetPageHeadUi(View view) {
        simple_toolbar = view.findViewById(R.id.toolbar);
        AppBarLayout mAppBarLayout = view.findViewById(R.id.app_bar_layout);
        mCollapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.seting_bg);
        mCollapsingToolbarLayout.setContentScrim(new BitmapDrawable(bitmap));

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -simple_toolbar.getHeight() / 2) {
                    mCollapsingToolbarLayout.setTitle("我的");
                    mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
                    mCollapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
                } else {
                    mCollapsingToolbarLayout.setTitle("");
                    simple_toolbar.setRightTitleDrawable(R.mipmap.ta_title_setting);
                    simple_toolbar.setLeftTitleText(getResources().getString(R.string.app_name));
                    simple_toolbar.setLeftTitleColor(getResources().getColor(R.color.black));
                }
            }
        });

        simple_toolbar.setRightTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, SettingCenterActivity.class));
            }
        });
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        SettingPageModel item = adapter.getChild(groupPosition, childPosition);
        switch (item.getId()) {
            case  SettingCenterConstants.SETTING_KEEP_AWAKE:
                Toast.makeText(mActivity,"屏幕亮度", Toast.LENGTH_LONG).show();
                break;
            case  SettingCenterConstants.SETTING_SHOW_IMG:
                Toast.makeText(mActivity,"消息通知", Toast.LENGTH_LONG).show();
                break;
            case  SettingCenterConstants.SETTING_RECIEVE_MSG:
                Toast.makeText(mActivity,"扫一扫", Toast.LENGTH_LONG).show();
                break;
            case  SettingCenterConstants.SETTING_REVOLVE_MAP:
                Toast.makeText(mActivity,"看一看", Toast.LENGTH_LONG).show();
                break;
            case  SettingCenterConstants.SETTING_CHANGE_VIEWPOINT:
                Toast.makeText(mActivity,"搜一搜", Toast.LENGTH_LONG).show();
                break;
            case  SettingCenterConstants.SETTING_AUTO_UPDATE:
                Toast.makeText(mActivity,"软件更新设置", Toast.LENGTH_LONG).show();
                break;
            case  SettingCenterConstants.SETTING_SHAKE_VOICE:
                Toast.makeText(mActivity,"安全中心", Toast.LENGTH_LONG).show();
                break;
            case  SettingCenterConstants.SETTING_SHOW_MAP_BUBBLE:
                Toast.makeText(mActivity,"吐槽反馈", Toast.LENGTH_LONG).show();
                break;
            case  SettingCenterConstants.SETTING_CHOOSE_SD:
                Toast.makeText(mActivity,"我们", Toast.LENGTH_LONG).show();
                break;
        }

        return false;
    }

}
