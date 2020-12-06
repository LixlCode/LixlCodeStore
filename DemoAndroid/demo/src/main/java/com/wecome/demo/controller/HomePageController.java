package com.wecome.demo.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wecome.demo.R;
import com.wecome.demo.model.HomeItemDataModel;
import com.wecome.demo.view.HomeItemView;

import java.util.ArrayList;

public class HomePageController {

    private static HomePageController mInstance = null;
    public ToolsHeaderListener toolsListener;
    private ArrayList<HomeItemDataModel> currentDisplay;
    private LinearLayout toolsContainer = null;
    private ArrayList<HomeItemDataModel> toolsItemDatas = new ArrayList<HomeItemDataModel>();
    private Context mContext;

    public static HomePageController getInstance() {
        if (mInstance == null) {
            synchronized (HomePageController.class) {
                if (mInstance == null) {
                    mInstance = new HomePageController();
                }
            }
        }
        return mInstance;
    }

    public void setContainerView(LinearLayout parent) {
        toolsContainer = parent;
    }

    public void initData(Context mContext) {
        this.mContext = mContext;
        initToolsDatas();
        toolsListener = new ToolsHeaderListener();
        createDisplayDatas(false);
    }

    private ArrayList<HomeItemDataModel> createDisplayDatas(boolean open) {
        currentDisplay = new ArrayList<HomeItemDataModel>();
        if (toolsItemDatas != null && toolsItemDatas.size() >= 8) {
            int size = 8;
            if (open) {
                size = toolsItemDatas.size();
            }
            for (int index = 0; index < size; index++) {
                currentDisplay.add(toolsItemDatas.get(index));
            }
        }
        return currentDisplay;
    }


    public void displayAllItems(boolean isAll) {
        updateToolsView(createDisplayDatas(isAll));
    }


    public void updateToolsView(ArrayList<HomeItemDataModel> data) {
        if (toolsContainer != null) {
            LinearLayout row = null;
            Context context = mContext;
            if (context == null || data == null || data.size() <= 0) {
                toolsContainer.removeAllViews();
                return;
            }
            int itemCount = data.size();
            toolsContainer.removeAllViews();
            for (int j = 0; j < itemCount; j += 4) {
                row = (LinearLayout) LayoutInflater.from(context).inflate(
                        R.layout.home_tools_row, null);
                HomeItemView itemView;

                // 第一个item
                HomeItemDataModel itemData = data.get(j);
                itemView = (HomeItemView) row.findViewById(R.id.tools_item_1);
                itemView.setTag(itemData.mEventType);
                itemView.setVisibility(View.VISIBLE);
                itemView.onUpdate(itemData, toolsListener);
                // 第二个item
                if (j + 1 < itemCount) {
                    itemData = data.get(j + 1);
                    itemView = (HomeItemView) row.findViewById(R.id.tools_item_2);
                    itemView.setTag(itemData.mEventType);
                    itemView.setVisibility(View.VISIBLE);
                    itemView.onUpdate(itemData, toolsListener);
                }

                // 第三个item
                if (j + 2 < itemCount) {
                    itemData = data.get(j + 2);
                    itemView = (HomeItemView) row.findViewById(R.id.tools_item_3);
                    itemView.setTag(itemData.mEventType);
                    itemView.setVisibility(View.VISIBLE);
                    itemView.onUpdate(itemData, toolsListener);
                }

                // 第四个item
                if (j + 3 < itemCount) {
                    itemData = data.get(j + 3);
                    itemView = (HomeItemView) row.findViewById(R.id.tools_item_4);
                    itemView.setTag(itemData.mEventType);
                    itemView.setVisibility(View.VISIBLE);
                    itemView.onUpdate(itemData, toolsListener);
                }
                toolsContainer.addView(row);
            }
        }
    }


    private void initToolsDatas() {
        if (toolsItemDatas != null) {
            toolsItemDatas.clear();
            // 离线地图
            toolsItemDatas.add(new HomeItemDataModel(
                    R.string.user_center_page_offline_maps,
                    R.mipmap.user_center_offline_maps,
                    HomeItemDataModel.OFFLINE_TYPE, false, false
            ));
            // 收藏夹
            toolsItemDatas.add(new HomeItemDataModel(
                    R.string.user_center_fav,
                    R.mipmap.icon_usercenter_favorite,
                    HomeItemDataModel.FAV_TYPE, false, false
            ));
            // 常用地址
            toolsItemDatas.add(new HomeItemDataModel(
                    R.string.user_center_addr,
                    R.mipmap.icon_usercenter_address,
                    HomeItemDataModel.ADDR_TYPE, false, false
            ));
            // 足迹
            toolsItemDatas.add(new HomeItemDataModel(
                    R.string.user_center_track,
                    R.mipmap.icon_usercenter_travel,
                    HomeItemDataModel.TRACK_TYPE, false, false
            ));
            // 打车
            toolsItemDatas.add(new HomeItemDataModel(
                    R.string.user_center_tools_order_car,
                    R.mipmap.user_center_tools_order_car,
                    HomeItemDataModel.ORDER_CAR, false, false
            ));
            // 跑步路线
            toolsItemDatas.add(new HomeItemDataModel(
                    R.string.user_center_running_route,
                    R.mipmap.user_center_running_route,
                    HomeItemDataModel.RUNNING_ROUTE, false, false
            ));
            // 充电桩地图
            toolsItemDatas.add(new HomeItemDataModel(
                    R.string.user_center_charge_pile_map,
                    R.mipmap.user_center_icon_charge_map,
                    HomeItemDataModel.CHARGEPILE_TYPE, false, false
            ));

            // 停车助手
            toolsItemDatas.add(new HomeItemDataModel(
                    R.string.user_center_park_helper,
                    R.mipmap.user_center_stop_car_help,
                    HomeItemDataModel.CAR_SERVICE, false, false
            ));

            // 共享单车
            if (true) {
                toolsItemDatas.add(new HomeItemDataModel(
                        R.string.user_center_share_bike_tools,
                        R.mipmap.icon_usercenter_share_bike_tools,
                        HomeItemDataModel.SHARE_BIKE_TYPE, false, false
                ));
            }

            // 主题
            toolsItemDatas.add(new HomeItemDataModel(
                    R.string.user_center_change_theme,
                    R.mipmap.user_center_change_theme,
                    HomeItemDataModel.THEME_TYPE, false, false
            ));

            // 测距
            toolsItemDatas.add(new HomeItemDataModel(
                    R.string.user_center_measure_distance,
                    R.mipmap.icon_usercenter_measure,
                    HomeItemDataModel.MEASSURE_TYPE, false, false
            ));

            if (true) {
                // 直播间
                toolsItemDatas.add(new HomeItemDataModel(
                        R.string.user_center_live_room,
                        R.mipmap.icon_usercenter_live_room,
                        HomeItemDataModel.VIDEO_TYPE, false, false
                ));
            }
            // boolean showNewsPaper = SwitchCloudController.getInstance().isSwitchOn(
            //         SwitchCloudControllerConstant.NEWS_PAPER, true);
            if (true) {
                // 出行早晚报
                toolsItemDatas.add(new HomeItemDataModel(
                        R.string.user_center_news_paper,
                        R.mipmap.icon_usercenter_news_paper,
                        HomeItemDataModel.NEWS_TYPE, false, false
                ));
            }

            // boolean showAutoTrans = WifiTransferCloudControl.getInstance().isWifiTransEnable();
            if (true) {
                toolsItemDatas.add(new HomeItemDataModel(
                        R.string.user_center_auto_connect,
                        R.mipmap.icon_usercenter_wifi_transfer,
                        HomeItemDataModel.AUTO_WIFI, false, false
                ));
            }
            // 行程助手
            toolsItemDatas.add(new HomeItemDataModel(
                    R.string.user_center_schedule_tools,
                    R.mipmap.icon_usercenter_schedule_tools,
                    HomeItemDataModel.SCHE_TYPE, false, false
            ));
            // 小度玩法
            toolsItemDatas.add(new HomeItemDataModel(
                    R.string.user_center_tools_voice,
                    R.mipmap.user_center_tools_voice,
                    HomeItemDataModel.VOICE_SKILL, false, false
            ));
        }
    }


    public class ToolsHeaderListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int eventType = (int) v.getTag();
            switch (eventType) {
                case HomeItemDataModel.TRACK_TYPE:
                    // 我的足迹
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.user_center_track), Toast.LENGTH_SHORT).show();
                    break;
                case HomeItemDataModel.FAV_TYPE:
                    // 收藏夹按钮
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.user_center_fav), Toast.LENGTH_SHORT).show();
                    break;
                case HomeItemDataModel.ADDR_TYPE:
                    // 我的常用地点
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.user_center_addr), Toast.LENGTH_SHORT).show();
                    break;
                case HomeItemDataModel.OFFLINE_TYPE:
                    // 离线地图
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.user_center_page_offline_maps), Toast.LENGTH_SHORT).show();
                    break;
                case HomeItemDataModel.THEME_TYPE:
                    // 换肤
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.user_center_page_offline_maps), Toast.LENGTH_SHORT).show();
                    break;
                case HomeItemDataModel.CAR_SERVICE:
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.user_center_page_offline_maps), Toast.LENGTH_SHORT).show();
                    break;
                case HomeItemDataModel.SCHE_TYPE:
                    // 行程助手
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.user_center_page_offline_maps), Toast.LENGTH_SHORT).show();
                    break;
                case HomeItemDataModel.MEASSURE_TYPE:
                    // 测距
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.user_center_page_offline_maps), Toast.LENGTH_SHORT).show();
                    break;
                case HomeItemDataModel.RUNNING_ROUTE:
                    // 跑步路线
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.user_center_page_offline_maps), Toast.LENGTH_SHORT).show();
                    break;
                case HomeItemDataModel.VIDEO_TYPE:
                    // 直播
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.user_center_page_offline_maps), Toast.LENGTH_SHORT).show();
                    break;
                case HomeItemDataModel.NEWS_TYPE:
                    // 出行早晚报
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.user_center_page_offline_maps), Toast.LENGTH_SHORT).show();
                    break;
                case HomeItemDataModel.CHARGEPILE_TYPE:
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.user_center_page_offline_maps), Toast.LENGTH_SHORT).show();
                    break;
                case HomeItemDataModel.SHARE_BIKE_TYPE:
                    // 单车
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.user_center_page_offline_maps), Toast.LENGTH_SHORT).show();
                    break;
                case HomeItemDataModel.AUTO_WIFI:
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.user_center_page_offline_maps), Toast.LENGTH_SHORT).show();
                    break;
                case HomeItemDataModel.ORDER_CAR:
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.user_center_page_offline_maps), Toast.LENGTH_SHORT).show();
                    break;
                case HomeItemDataModel.VOICE_SKILL:
                    Toast.makeText(mContext, mContext.getResources()
                            .getString(R.string.user_center_page_offline_maps), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    }


}
