package com.wecome.demo.fragment;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wecome.demo.R;
import com.wecome.demo.activity.DialogActivity;
import com.wecome.demo.activity.SearchActivity;
import com.wecome.demo.activity.TestActivity;
import com.wecome.demo.activity.TestToolBarActivity;
import com.wecome.demo.adapter.GraphicPagerAdpater;
import com.wecome.demo.adapter.HomeExpandableListAdapter;
import com.wecome.demo.base.BaseFragment;
import com.wecome.demo.constants.SettingCenterConstants;
import com.wecome.demo.controller.HomePageController;
import com.wecome.demo.model.HomeExpandableListModel;
import com.wecome.demo.model.InternationalCardModel;
import com.wecome.demo.model.MonthReportModel;
import com.wecome.demo.model.NaviCardModel;
import com.wecome.demo.model.SignCardModel;
import com.wecome.demo.utils.SimpleToolbar;
import com.wecome.demo.view.IndexInvalidViewPager;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment implements View.OnClickListener
                                            , ExpandableListView.OnChildClickListener{

    private SimpleToolbar simple_toolbar;
    private PopupWindow mPopupWindow;
    private boolean isPanelClose = false;
    private ImageView mComToolsArrow;
    private View mToolsHeaderView;
    private ExpandableListView mListView;
    private HomeExpandableListAdapter adapter;

    @Override
    protected View initView() {
        View mRootView = View.inflate(mActivity, R.layout.fragment_home, null);
        // initToolBar(mRootView);
        initExpandableListView(mRootView);
        return mRootView;
    }

    private void initExpandableListView(View view) {
        // 加载布局
        mListView = view.findViewById(R.id.el_list);
        // 创建适配器
        adapter = new HomeExpandableListAdapter(mActivity);
        // 注册子条目点击事件
        mListView.setOnChildClickListener(this);
        // 处理组条目的点击事件
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        // 处理滑动监听
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        // 显示List数据
        setupViews();

        // 添加第一个头部
        setFirstHead();
        // 添加第二个头部
        setSecondHead();
    }

    private void setSecondHead() {
        HomePageController.getInstance().initData(mActivity); // 数据
        mToolsHeaderView = View.inflate(mActivity, R.layout.home_common_expand_header, null);
        LinearLayout toolsContainer = mToolsHeaderView.findViewById(R.id.container_root);
        HomePageController.getInstance().setContainerView(toolsContainer);
        mComToolsArrow = (ImageView) mToolsHeaderView.findViewById(R.id.title_right_arrow);
        mComToolsArrow.setOnClickListener(this);
        mToolsHeaderView.findViewById(R.id.tools_title_right_text).setOnClickListener(this);
        setThreePanelDisplay();

        mListView.addHeaderView(mToolsHeaderView);
    }

    // 常用功能:第三行 点击title小按钮，收起（1）或展开（0）
    private void setThreePanelDisplay() {
        if (!isPanelClose) {
            HomePageController.getInstance().displayAllItems(false);
            mComToolsArrow.setImageResource(R.mipmap.home_common_unexpand_icon);
            ((TextView) mToolsHeaderView.findViewById(R.id.tools_title_right_text)).setText("更多");
            isPanelClose = true;
        } else {
            HomePageController.getInstance().displayAllItems(true);
            mComToolsArrow.setImageResource(R.mipmap.home_common_expand_icon);
            ((TextView) mToolsHeaderView.findViewById(R.id.tools_title_right_text)).setText("收起");
            isPanelClose = false;
        }
    }

    private void setFirstHead() {
        // 第一块
        View mFirstHead = mActivity.getLayoutInflater().inflate(R.layout.home_list_frist_head, null);
        ImageView firstHead = mFirstHead.findViewById(R.id.iv_first_head);

        // 第二块
        IndexInvalidViewPager mViewPager = mFirstHead.findViewById(R.id.vp_view_pager_1);
        GraphicPagerAdpater viewPagerAdpater = new GraphicPagerAdpater(mActivity);
        mViewPager.setAdapter(viewPagerAdpater);
        mViewPager.setPageMargin(30);
        // 准备数据
        SignCardModel signCardModel = new SignCardModel();
        NaviCardModel naviCardModel = new NaviCardModel();
        InternationalCardModel internationalCardModel = new InternationalCardModel();
        MonthReportModel monthReportModel = new MonthReportModel();
        viewPagerAdpater.setData(signCardModel, naviCardModel, internationalCardModel, monthReportModel);

        // 添加到List
        mListView.addHeaderView(mFirstHead);
    }

    private void setupViews() {
        setListData();

        mListView.setAdapter(adapter);
        int groupCount = adapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            mListView.expandGroup(i);
        }
    }

    private void setListData() {
        HomeExpandableListModel listItemModel;
        ArrayList<HomeExpandableListModel> list = new ArrayList<>();

        // 活动专区
        listItemModel = new HomeExpandableListModel(R.string.tools_activity, R.mipmap
                .icon_usercenter_operation, SettingCenterConstants.USER_ACTIVITY);
        listItemModel.setShowRightText(true);
        listItemModel.setRightText("热门活动");
        list.add(listItemModel);

        if (true) {
            listItemModel = new HomeExpandableListModel(R.string.my_contribution, R.mipmap
                    .icon_usercenter_contribution, SettingCenterConstants.USER_CONTRIBUTION);
            listItemModel.setShowRightText(true);
            listItemModel.setRightText("报错、新增、点评");
            list.add(listItemModel);
        }

        // 我是商家
        listItemModel = new HomeExpandableListModel(R.string.my_business, R.mipmap
                .icon_usercenter_business, SettingCenterConstants.USER_BUSINESS);
        listItemModel.setShowRightText(true);
        listItemModel.setRightText("【免费】认领、管理店铺");
        list.add(listItemModel);

        if (true) {
            listItemModel = new HomeExpandableListModel(R.string.my_navprotect, R.mipmap
                    .icon_usercenter_navprotect, SettingCenterConstants.USER_NAVPROTECT);
            listItemModel.setShowRightText(true);
            listItemModel.setRightText("导错、违章赔付");
            list.add(listItemModel);
        }
        // 交通号
        listItemModel = new HomeExpandableListModel(R.string.my_traffic_num,
                SettingCenterConstants.USER_TRAFFIC_NUM);
        listItemModel.setShowRightText(true);
        listItemModel.setRightText("权威机构入驻");
        list.add(listItemModel);
        // 意见反馈
        list.add(new HomeExpandableListModel(R.string.my_help, R.mipmap
                .icon_usercenter_help, SettingCenterConstants.USER_FEEDBACK));
        // 设置
        list.add(new HomeExpandableListModel(R.string.my_setting, R.mipmap
                .icon_usercenter_setting, SettingCenterConstants.USER_SETTING));
        HomeExpandableListModel model = new HomeExpandableListModel(R.string
                .tools_exitap, R.mipmap
                .icon_exitap, SettingCenterConstants.USER_EXITAP);

        list.add(model);
        adapter.clearData();
        adapter.setData(list);
    }

    private void initToolBar(View view) {
        simple_toolbar = view.findViewById(R.id.simple_toolbar);
        simple_toolbar.setMainTitle("首页");
        simple_toolbar.setRightTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出自定义overflow
                popUpMyOverflow();
            }
        });
    }

    /**
     * 弹出自定义的popWindow
     */
    public void popUpMyOverflow() {
        //除了PopupWindow意外的地方变暗，形成这招的效果
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.alpha=0.6f;   //这句就是设置窗口里崆件的透明度的．0.0全透明．1.0不透明．
        window.setAttributes(wl);


        //获取状态栏高度
        Rect frame = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        //状态栏高度+toolbar的高度
        int yOffset = frame.top + simple_toolbar.getHeight();
        if (null == mPopupWindow) {
            //初始化PopupWindow的布局
            View popView = getLayoutInflater()
                    .inflate(R.layout.main_popwindow, null);
            //popView即popupWindow的布局，ture设置focusAble.
            mPopupWindow = new PopupWindow(popView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            //必须设置BackgroundDrawable后setOutsideTouchable(true)才会有效
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            //点击外部关闭。
            mPopupWindow.setOutsideTouchable(true);
            //设置一个动画。
            mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            //设置Gravity，让它显示在右上角。
            mPopupWindow.showAtLocation(simple_toolbar,
                    Gravity.END | Gravity.TOP, 0, yOffset);
            //设置item的点击监听
            popView.findViewById(R.id.ll_item1).setOnClickListener(this);
            popView.findViewById(R.id.ll_item2).setOnClickListener(this);
            popView.findViewById(R.id.ll_item3).setOnClickListener(this);
        } else {
            mPopupWindow.showAtLocation(simple_toolbar,
                    Gravity.END | Gravity.TOP, 0, yOffset);
        }
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Window window = mActivity.getWindow();
                WindowManager.LayoutParams wl = window.getAttributes();
                wl.alpha=1.0f;   //这句就是设置窗口里崆件的透明度的．0.0全透明．1.0不透明．
                window.setAttributes(wl);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_item1:
                Toast.makeText(mActivity,"拍照", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mActivity, TestToolBarActivity.class));
                break;
            case R.id.ll_item2:
                Toast.makeText(mActivity,"分享", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mActivity, TestActivity.class));
                break;
            case R.id.ll_item3:
                Toast.makeText(mActivity,"付款==" + getChannel(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mActivity, DialogActivity.class));
                break;

            // ------------------------------------------------------------
            case R.id.title_right_arrow:
            case R.id.tools_title_right_text:
                setThreePanelDisplay();
                break;

        }
        //点击PopWindow的item后,关闭此PopWindow
        if (null != mPopupWindow && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    // 获取渠道号
    private String getChannel() {
        try {
            PackageManager pm = mActivity.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(mActivity
                    .getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getString("CHANNEL");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        HomeExpandableListModel item = adapter.getChild(groupPosition, childPosition);
        switch (item.getId()) {
            case SettingCenterConstants.USER_ACTIVITY:
                Toast.makeText(mActivity, "活动专区", Toast.LENGTH_SHORT).show();
                break;
            case SettingCenterConstants.USER_CONTRIBUTION:
                Toast.makeText(mActivity, "我的贡献", Toast.LENGTH_SHORT).show();
                break;
            case SettingCenterConstants.USER_BUSINESS:
                Toast.makeText(mActivity, "我是商家", Toast.LENGTH_SHORT).show();
                break;
            case SettingCenterConstants.USER_NAVPROTECT:
                Toast.makeText(mActivity, "导航保障", Toast.LENGTH_SHORT).show();
                break;
            case SettingCenterConstants.USER_TRAFFIC_NUM:
                Toast.makeText(mActivity, "交通号", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mActivity, SearchActivity.class)); // 测试
                break;
            case SettingCenterConstants.USER_FEEDBACK:
                Toast.makeText(mActivity, "意见反馈", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mActivity, DialogActivity.class)); //测试
                break;
            case SettingCenterConstants.USER_SETTING:
                Toast.makeText(mActivity, "设置", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mActivity, TestActivity.class)); // 测试
                break;
            case SettingCenterConstants.USER_EXITAP:
                Toast.makeText(mActivity, "关闭地图", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mActivity, TestToolBarActivity.class)); // 测试
                break;
            default:
                break;
        }

        return true;
    }

}
