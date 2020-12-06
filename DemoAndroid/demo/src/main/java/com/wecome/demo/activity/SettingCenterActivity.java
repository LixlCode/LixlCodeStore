package com.wecome.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.wecome.demo.R;
import com.wecome.demo.adapter.SettingCenterAdapter;
import com.wecome.demo.constants.SettingCenterConstants;
import com.wecome.demo.listener.AlphaPressTouchListener;
import com.wecome.demo.model.SettingCenterModel;
import com.wecome.demo.utils.SimpleToolbar;
import com.wecome.demo.view.SettingCenterListItemView;

import java.util.ArrayList;

public class SettingCenterActivity  extends AppCompatActivity
        implements ExpandableListView.OnChildClickListener{

    private SettingCenterAdapter mAdapter;
    private ExpandableListView exp_list;
    private Button mLogoutBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_list);

        initToolBar();

        exp_list = (ExpandableListView)findViewById(R.id.exp_list);
        // 初始化适配器
        mAdapter = new SettingCenterAdapter(this);
        // 设置条目数据
        setListData();

        // 申明子条目的点击事件
        exp_list.setOnChildClickListener(this);
        // 主条目的点击事件
        exp_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        // 加载底部布局
        View footView = LayoutInflater.from(this).inflate(R.layout.set_list_foot, null);
        exp_list.addFooterView(footView);
        initLogoutView();

        // 在groups list视图中展开组
        int groupCount = mAdapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            // 如果组被展开，返回True，否则返回false(如果组被展开，返回false，已经展开，返回false)
            exp_list.expandGroup(i);
        }

    }

    private void initToolBar() {
        SimpleToolbar simple_toolbar = findViewById(R.id.simple_toolbar);
        simple_toolbar.setMainTitle("设置详情");
        simple_toolbar.setRightHide();
        simple_toolbar.hideLeftAppName();
        simple_toolbar.setLeftshow();
        simple_toolbar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initLogoutView() {
        mLogoutBtn = (Button) findViewById(R.id.btn_logout);
        if (/*是否登录*/true) {
            mLogoutBtn.setVisibility(View.VISIBLE);
            AlphaPressTouchListener.enable(mLogoutBtn);
            mLogoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } else {
            mLogoutBtn.setVisibility(View.GONE);
        }
    }

    private void setListData() {
        SettingCenterModel listItem = null;
        // 地图常用设置
        ArrayList<SettingCenterModel> list = new ArrayList<SettingCenterModel>();

        // 屏幕常亮
        listItem = new SettingCenterModel(R.string.setting_keep_awake, SettingCenterConstants.SETTING_KEEP_AWAKE);
        listItem.setStyle(SettingCenterConstants.EXPANDLIST_STYLE_CHECKADBE);
        listItem.setChecked(true);
        list.add(listItem);
        // 消息通知
        listItem = new SettingCenterModel(R.string.setting_recieve_msg, SettingCenterConstants.SETTING_RECIEVE_MSG);
        listItem.setStyle(SettingCenterConstants.EXPANDLIST_STYLE_CHECKADBE);
        listItem.setChecked(true);
        listItem.setShowSecondaryLeftText(true);
        listItem.setSecondaryLeftText(true ? R.string.setting_recieve_msg_1 : R.string.setting_recieve_msg_2);
        list.add(listItem);
        // 旋转手势
        listItem = new SettingCenterModel(R.string.setting_revolve_map, SettingCenterConstants.SETTING_REVOLVE_MAP);
        listItem.setStyle(SettingCenterConstants.EXPANDLIST_STYLE_CHECKADBE);
        listItem.setChecked(true);
        listItem.setShowSecondaryLeftText(true);
        listItem.setSecondaryLeftText(R.string.setting_revolve_map_1);
        list.add(listItem);
        // 切换视角
        listItem = new SettingCenterModel(R.string.setting_change_viewpoint, SettingCenterConstants.SETTING_CHANGE_VIEWPOINT);
        listItem.setStyle(SettingCenterConstants.EXPANDLIST_STYLE_CHECKADBE);
        listItem.setChecked(true);
        listItem.setShowSecondaryLeftText(true);
        listItem.setSecondaryLeftText(R.string.setting_change_viewpoint_1);
        list.add(listItem);
        // WIFI自动更新
        listItem = new SettingCenterModel(R.string.setting_auto_update, SettingCenterConstants.SETTING_AUTO_UPDATE);
        listItem.setStyle(SettingCenterConstants.EXPANDLIST_STYLE_CHECKADBE);
        listItem.setChecked(false);
        listItem.setShowSecondaryLeftText(true);
        listItem.setSecondaryLeftText(R.string.setting_auto_update_1);
        list.add(listItem);
        // 首页气泡开关
        listItem = new SettingCenterModel(R.string.setting_bubble_show, SettingCenterConstants.SETTING_SHOW_MAP_BUBBLE);
        listItem.setStyle(SettingCenterConstants.EXPANDLIST_STYLE_CHECKADBE);
        listItem.setChecked(false);
        listItem.setShowSecondaryLeftText(true);
        listItem.setSecondaryLeftText(R.string.setting_bubble_des);
        list.add(listItem);

        mAdapter.setData(list);

        // -----------------------------------------------------------------------------

        list = new ArrayList<SettingCenterModel>();
        listItem = new SettingCenterModel(R.string.setting_home_config, SettingCenterConstants.SETTING_CHOOSE_SD);
        list.add(listItem);
        mAdapter.setData(list);


        // 导航设置
        list = new ArrayList<SettingCenterModel>();
        listItem = new SettingCenterModel(R.string.setting_navi_config, SettingCenterConstants.SETTING_NAVI_CONFIG);
        list.add(listItem);
        mAdapter.setData(list);

        // 关于和新功能介绍
        list = new ArrayList<SettingCenterModel>();
        listItem = new SettingCenterModel(R.string.setting_about, SettingCenterConstants.SETTING_ABOUT);
        list.add(listItem);
        mAdapter.setData(list);


        exp_list.setAdapter(mAdapter);
    }

    /**
     * 各个条目的点击事件
     * @param parent            listView
     * @param v                 listItem
     * @param groupPosition     组
     * @param childPosition     位置
     * @param id                view的id
     * @return                  true
     */
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        /**
         * 判断check是否被选中，直接拿check来判断，做对应的事就ok，如：
         * if（check）{
         *     // TODO
         * } else {
         *     // TODO
         * }
         * 也就是说，CheckBox选中之后的事件是在这个点击事件中处理，
         * 如下：『屏幕常亮』的设置，其他的一样，但是处理事件之前要做持久化，记住用的设置
         */
        SettingCenterModel item = mAdapter.getChild(groupPosition, childPosition);
        boolean check = false;
        if (item.getStyle() == SettingCenterConstants.EXPANDLIST_STYLE_CHECKADBE) {
            SettingCenterListItemView view = (SettingCenterListItemView) v;
            check = !item.isChecked();
            item.setChecked(check);
            view.setChecked(check);
        }

        switch (item.getId()) {
            case  SettingCenterConstants.SETTING_KEEP_AWAKE:
                Toast.makeText(SettingCenterActivity.this,"屏幕亮度设置", Toast.LENGTH_LONG)
                        .show();
                // 1. 做持久化，因为这个设置用户只设置一次，不能每次启动都让用户设置，所以必须做持久化，记住用户的设置
                // TODO 遗留
                if (check) {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
                break;
            case SettingCenterConstants.SETTING_RECIEVE_MSG:
                Toast.makeText(SettingCenterActivity.this,"消息通知设置", Toast.LENGTH_LONG)
                        .show();
                break;
            case SettingCenterConstants.SETTING_REVOLVE_MAP:
                Toast.makeText(SettingCenterActivity.this,"旋转手势开关", Toast.LENGTH_LONG)
                        .show();
                break;
            case SettingCenterConstants.SETTING_CHANGE_VIEWPOINT:
                Toast.makeText(SettingCenterActivity.this,"切换视角开关", Toast.LENGTH_LONG)
                        .show();
                break;
            case SettingCenterConstants.SETTING_AUTO_UPDATE:
                Toast.makeText(SettingCenterActivity.this,"Wifi下自动下载开关", Toast.LENGTH_LONG)
                        .show();
                break;
            case SettingCenterConstants.SETTING_SHOW_MAP_BUBBLE:
                Toast.makeText(SettingCenterActivity.this,"首页出行提示框", Toast.LENGTH_LONG)
                        .show();
                break;

            case SettingCenterConstants.SETTING_CHOOSE_SD:
                Toast.makeText(SettingCenterActivity.this,"设置家和公司", Toast.LENGTH_LONG)
                        .show();
                break;
            case SettingCenterConstants.SETTING_NAVI_CONFIG:
                Toast.makeText(SettingCenterActivity.this,"导航设置", Toast.LENGTH_LONG)
                        .show();
                break;
            case SettingCenterConstants.SETTING_ABOUT:
                Toast.makeText(SettingCenterActivity.this,"关于", Toast.LENGTH_LONG)
                        .show();
                break;
        }
        mAdapter.notifyDataSetChanged();
        return true;
    }
}
