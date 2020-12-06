package com.wecome.demo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wecome.demo.AppConfig;
import com.wecome.demo.MainApplication;
import com.wecome.demo.R;
import com.wecome.demo.model.InternationalCardModel;
import com.wecome.demo.model.InternationalModel;
import com.wecome.demo.model.MonthReportModel;
import com.wecome.demo.model.NaviCardModel;
import com.wecome.demo.model.NaviModel;
import com.wecome.demo.model.SignCardModel;
import com.wecome.demo.model.SignModel;
import com.wecome.demo.view.CustomGraphic;
import com.wecome.demo.view.InternationalCard;
import com.wecome.demo.view.UserSignCard;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chenjie07 on 17/6/20.
 */

public class GraphicPagerAdpater extends PagerAdapter {

    View naviCardTotalView;
    View naviCardDefault;
    TextView naviCardDetail;

    CustomGraphic naviCard;
    UserSignCard signCard;
    InternationalCard internationalCard;
    String uid = "";
    private long currentSignTime = 0;
    private long currentRecordTime = 0;
    int size = 4;

    SignCardModel signCardModel;
    NaviCardModel naviCardModel;
    InternationalCardModel internationaCardModel;
    MonthReportModel monthReportModel;


    public boolean isCardChange() {
        long newSignTime = AppConfig.getInstance().getUserSysSigninTm();
        boolean isSign = (newSignTime != currentSignTime);
        currentSignTime = newSignTime;

        long newRecordTime = AppConfig.getInstance().getTrackRecordTm();
        boolean isRecord = (newRecordTime != currentRecordTime);
        currentRecordTime = newRecordTime;

        return true;/*!uid.equals(AccountManager.getInstance().getUid()) || isSign || isRecord;*/

    }

    public GraphicPagerAdpater(Context context) {
        currentSignTime = AppConfig.getInstance().getUserSysSigninTm();
        currentRecordTime = AppConfig.getInstance().getTrackRecordTm();

        signCardModel = new SignCardModel();
        naviCardModel = new NaviCardModel();
        internationaCardModel = new InternationalCardModel();
        monthReportModel = new MonthReportModel();
    }


    public void setData(SignCardModel signCardModel, NaviCardModel naviCardModel,
                        InternationalCardModel interModel, MonthReportModel reportModel) {
        if (signCardModel.type == SignCardModel.State.SUCCESS) {
            setInfo();
        }
        this.signCardModel = signCardModel;
        this.naviCardModel = naviCardModel;
        this.internationaCardModel = interModel;
        this.monthReportModel = reportModel;

        addLog(naviCardModel.totalDistance, naviCardModel.data, signCardModel.data);
        if (internationaCardModel.data == null) {
            size = 3;
        } else {
            size = 4;
        }
        notifyDataSetChanged();
    }

    public void resetView() {
        signCardModel.type = SignCardModel.State.LOADING;
        signCardModel.totalNum = "";
        signCardModel.timeDutation = "";
        signCardModel.data = new ArrayList<>();

        naviCardModel.type = NaviCardModel.State.LOADING;
        naviCardModel.totalDistance = 0;
        naviCardModel.data = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void resetInfo() {
        uid = "";
        currentSignTime = 0;
    }

    private void setInfo() {
        //uid = AccountManager.getInstance().getUid();
        //currentSignTime = UserCenterConfig.getInstance().getUserSysSigninTm();
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;
        switch (position) {
            case 0:
                createSignView();
                view = signCard;
                if (signCardModel.type == SignCardModel.State.LOADING) {
                    signCard.resetView();
                } else {
                    if (signCardModel.data.isEmpty()) {
                        if (signCardModel.type == SignCardModel.State.SUCCESS) {
                            AppConfig.getInstance().setSigninData(false);
                        }
                        signCard.showDefault(monthReportModel);
                    } else {
                        AppConfig.getInstance().setSigninData(true);
                        signCard.setData(signCardModel.data, signCardModel.totalNum,
                                signCardModel.timeDutation, monthReportModel);
                    }
                }
                break;
            case 1:
                createNaviView();
                if (naviCardModel.type == NaviCardModel.State.LOADING) {
                    if (naviCard != null) {
                        naviCard.setTotalData(naviCardModel.data);
                    }
                    boolean hasData = /*UserCenterConfig.getInstance().getNaviData()*/true;
                    if (hasData) {
                        naviCard.setVisibility(View.VISIBLE);
                        naviCardDefault.setVisibility(View.GONE);
                    } else {
                        naviCard.setVisibility(View.GONE);
                        naviCardDefault.setVisibility(View.VISIBLE);
                    }
                    naviCardDetail.setVisibility(View.GONE);
                } else {
                    if (naviCardModel.totalDistance == 0 || naviCardModel.data.size() != CustomGraphic.MONTH) {
                        if (naviCardModel.type == NaviCardModel.State.SUCCESS) {
                            AppConfig.getInstance().setNaviData(false);
                        }
                        naviCardDefault.setVisibility(View.VISIBLE);
                        naviCard.setVisibility(View.GONE);
                        if (monthReportModel != null && monthReportModel.hasNewReport == 1
                                && !TextUtils.isEmpty(monthReportModel.title)) {
                            naviCardDetail.setText(monthReportModel.title);
                            naviCardDetail.setVisibility(View.VISIBLE);
                        } else {
                            naviCardDetail.setVisibility(View.GONE);
                        }
                    } else {
                        AppConfig.getInstance().setNaviData(true);
                        naviCardDefault.setVisibility(View.GONE);
                        naviCard.setVisibility(View.VISIBLE);
                        naviCard.setTotalData(naviCardModel.data);
                        if (monthReportModel != null && monthReportModel.hasNewReport == 1
                                && !TextUtils.isEmpty(monthReportModel.title)) {
                            naviCardDetail.setText(monthReportModel.title);
                        } else {
                            naviCardDetail.setText("出行回顾");
                        }
                        naviCardDetail.setVisibility(View.VISIBLE);
                    }
                }
                view = naviCardTotalView;
                break;
            case 2:
                createInternationalView();
                if (internationaCardModel.data != null) {
                    internationalCard.notifyDataChanged(internationaCardModel.data);
                } else {
                    internationalCard.notifyDataChanged(new InternationalModel());
                }
                view = internationalCard;
                break;
            default:
                break;
        }

        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public float getPageWidth(int position) {
        return 0.9f;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /**
     * 创建导航卡片
     */
    private void createNaviView() {
        if (naviCardTotalView == null) {
            naviCardTotalView = LayoutInflater.from(MainApplication.getContext()).inflate(R.layout.user_navi_card,
                    null);
            naviCard = (CustomGraphic) naviCardTotalView.findViewById(R.id.user_navi_card);
            naviCardDefault = naviCardTotalView.findViewById(R.id.user_navi_card_default);
            naviCardDetail = (TextView) naviCardTotalView.findViewById(R.id.navi_card_detail);
            naviCardDetail.setVisibility(View.GONE);

            naviCardDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ControlLogStatistics.getInstance().addLog("PCenterPG.naviChartEmptyClick");
                    // MToast.show("去首页点击「路线」发起导航吧！");
                }
            });

            naviCardDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (monthReportModel != null && monthReportModel.hasNewReport == 1
                            && !TextUtils.isEmpty(monthReportModel.title)) {
                        // ControlLogStatistics.getInstance().addLog("PCenterPG.chartClickReport");
                        // UserCenterUtils.goToMonthReport(monthReportModel.type, monthReportModel.date);
                    } else {
                        // ControlLogStatistics.getInstance().addLog("PCenterPG.naviChartToReview");
                        // UserCenterUtils.goToCardDetail("navi");
                    }
                }
            });
        }
    }

    private void createSignView() {
        // 初始化签到卡片
        if (signCard == null) {
            signCard = new UserSignCard(MainApplication.getContext());
        }
    }

    private synchronized void createInternationalView() {
        if (internationalCard == null) {
            internationalCard = new InternationalCard(MainApplication.getContext());
        }
    }

    private void addLog(long total, ArrayList<NaviModel> data, ArrayList<SignModel> signModels) {

        int navi;
        int check;
        if (total == 0 || data.size() != CustomGraphic.MONTH) {
            navi = 0;
        } else {
            navi = 1;
        }

        if (signModels.isEmpty()) {
            check = 0;
        } else {
            check = 1;
        }
        JSONObject params = new JSONObject();
        try {
            params.put("navi", navi + "");
            params.put("checkin", check + "");
            //ControlLogStatistics.getInstance().addLogWithArgs("PCenterPG.chartDataDetail", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
