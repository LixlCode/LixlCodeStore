package com.wecome.demo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wecome.demo.R;
import com.wecome.demo.model.MonthReportModel;
import com.wecome.demo.model.SignModel;
import com.wecome.demo.utils.ScreenUtils;
import com.wecome.demo.utils.UserCenterUtils;

import java.util.ArrayList;

/**
 * Created by chenjie07 on 17/6/29.
 */

public class UserSignCard extends RelativeLayout implements View.OnClickListener {
    Status current = Status.NORMAL;
    View userSignCard;
    View userSignCardDetail;
    View userSignCardDefult;

    ArrayList<SignModel> signModels = new ArrayList<>();
    MonthReportModel monthReportModel;
    String totalNum = "";
    String totalTime = "";

    View userSignCircle1;
    View userSignCircle2;
    View userSignCircle3;
    View userSignCircle4;
    View userSignCircleDetail;
    View userSignPoiContainer;
    TextView signinCardDetail;

    enum Status {
        LOADING, // 等待数据中
        NORMAL,  // 有数据显示四个圈
        DETAIL,  // 点击后展示我常去的点
        NO_DATA  // 无签到数据显示默认
    }

    public UserSignCard(Context context) {
        super(context);
        init();
    }

    public UserSignCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UserSignCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public UserSignCard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        inflate(getContext(), R.layout.user_signin_card, this);
        userSignCard = findViewById(R.id.user_sigin_card);
        userSignCircle1 = userSignCard.findViewById(R.id.user_sign_circle1);

        userSignCircle1.setOnClickListener(this);
        userSignCircle2 = userSignCard.findViewById(R.id.user_sign_circle2);
        userSignCircle2.setOnClickListener(this);
        userSignCircle3 = userSignCard.findViewById(R.id.user_sign_circle3);
        userSignCircle3.setOnClickListener(this);
        userSignCircle4 = userSignCard.findViewById(R.id.user_sign_circle4);
        userSignCircle4.setOnClickListener(this);
        userSignCardDetail = findViewById(R.id.user_sign_card_detail);
        userSignCircleDetail = userSignCardDetail.findViewById(R.id.user_sign_detail);
        userSignCardDetail.setOnClickListener(this);
        userSignCardDefult = findViewById(R.id.user_sign_card_default);
        userSignCardDefult.setOnClickListener(this);
        userSignPoiContainer = userSignCardDetail.findViewById(R.id.user_sign_poi_container);
        signinCardDetail = (TextView) findViewById(R.id.signin_card_detail);
        signinCardDetail.setOnClickListener(this);

        float width = ScreenUtils.getScreenWidth(getContext()) * 0.9f;
        float height = ScreenUtils.dip2px(120) * 1.0f;
        int dip50 = ScreenUtils.dip2px(50);
        int dip40 = ScreenUtils.dip2px(40);
        int dip36 = ScreenUtils.dip2px(36);
        int dip30 = ScreenUtils.dip2px(30);
        int dip70 = ScreenUtils.dip2px(70);
        float centerX = width / 2;
        float centerY = height / 2;

        RelativeLayout.LayoutParams params1 = (LayoutParams) userSignCircle1.getLayoutParams();
        params1.setMargins((int) (centerX - (width - 2 * dip70) / 4 - dip50 / 2),
                (int) (centerY - height / 4 - dip50 / 2), 0, 0);
        userSignCircle1.setLayoutParams(params1);

        RelativeLayout.LayoutParams params2 = (LayoutParams) userSignCircle2.getLayoutParams();
        params2.setMargins((int) (centerX + (width - 2 * dip70) / 4 - dip40 / 2),
                (int) (centerY - height / 4 - dip40 / 2), 0, 0);
        userSignCircle2.setLayoutParams(params2);

        RelativeLayout.LayoutParams params3 = (LayoutParams) userSignCircle3.getLayoutParams();
        params3.setMargins((int) (centerX - (width - 2 * dip70) / 4 - dip36 / 2),
                (int) (centerY + height / 4 - dip36 / 2), 0, 0);
        userSignCircle3.setLayoutParams(params3);

        RelativeLayout.LayoutParams params4 = (LayoutParams) userSignCircle4.getLayoutParams();
        params4.setMargins((int) (centerX + (width - 2 * dip70) / 4 - dip30 / 2),
                (int) (centerY + height / 4 - dip30 / 2), 0, 0);
        userSignCircle4.setLayoutParams(params4);

        resetView();
    }

    public void setData(ArrayList<SignModel> data, String totalNum, String totalTime, MonthReportModel reportModel) {
        monthReportModel = reportModel;
        current = Status.NORMAL;
        signModels.clear();
        signModels.addAll(data);
        this.totalNum = totalNum;
        this.totalTime = totalTime;
        userSignCard.setVisibility(VISIBLE);
        showCircle();
        userSignCardDetail.setVisibility(GONE);
        userSignCardDefult.setVisibility(GONE);
        if (monthReportModel != null && monthReportModel.hasNewReport == 1
                && !TextUtils.isEmpty(monthReportModel.title)) {
            signinCardDetail.setText(monthReportModel.title);
        } else {
            signinCardDetail.setText("出行回顾");
        }
        signinCardDetail.setVisibility(VISIBLE);
        setCircleContent(data);
        setTotalInfo(totalNum, totalTime);
        postInvalidate();
    }

    public void showDefault(MonthReportModel monthReportModel) {
        this.monthReportModel = monthReportModel;
        current = Status.NO_DATA;
        signModels.clear();
        userSignCard.setVisibility(GONE);
        userSignCardDetail.setVisibility(GONE);
        userSignCardDefult.setVisibility(VISIBLE);
        if (monthReportModel != null && monthReportModel.hasNewReport == 1
                && !TextUtils.isEmpty(monthReportModel.title)) {
            signinCardDetail.setText(monthReportModel.title);
            signinCardDetail.setVisibility(VISIBLE);
        } else {
            signinCardDetail.setVisibility(GONE);
        }
    }


    public void resetView() {
        if (current == Status.LOADING) {
            return;
        }
        current = Status.LOADING;
        signModels.clear();
        resetTotalInfo();
        resetCircleContent();
        resetTotalInfo();
        if (/*UserCenterConfig.getInstance().getSigninData()*/true) {
            userSignCard.setVisibility(VISIBLE);
            showCircle();
            userSignCardDefult.setVisibility(GONE);
        } else {
            userSignCard.setVisibility(GONE);
            userSignCardDefult.setVisibility(VISIBLE);
        }
        signinCardDetail.setVisibility(GONE);
        userSignCardDetail.setVisibility(GONE);
    }


    /**
     * 把四个圆圈的内容值为空
     */
    private void resetCircleContent() {
        userSignCircle1.setAlpha(0.6f);
        userSignCircle1.setVisibility(VISIBLE);
        ((TextView) userSignCircle1.findViewById(R.id.user_sign_num1)).setText("");
        ((TextView) userSignCircle1.findViewById(R.id.user_sign_name1)).setText("");

        userSignCircle2.setAlpha(0.6f);
        userSignCircle2.setVisibility(VISIBLE);
        ((TextView) userSignCircle2.findViewById(R.id.user_sign_num2)).setText("");
        ((TextView) userSignCircle2.findViewById(R.id.user_sign_name2)).setText("");

        userSignCircle3.setAlpha(0.6f);
        userSignCircle3.setVisibility(VISIBLE);
        ((TextView) userSignCircle3.findViewById(R.id.user_sign_num3)).setText("");
        ((TextView) userSignCircle3.findViewById(R.id.user_sign_name3)).setText("");

        userSignCircle4.setAlpha(0.6f);
        userSignCircle4.setVisibility(VISIBLE);
        ((TextView) userSignCircle4.findViewById(R.id.user_sign_num4)).setText("");
        ((TextView) userSignCircle4.findViewById(R.id.user_sign_name4)).setText("");
    }

    private void setCircleContent(ArrayList<SignModel> data) {
        for (int i = 0; i < data.size(); i++) {
            SignModel model = data.get(i);
            if (i == 0) {
                userSignCircle1.setAlpha(1f);
                userSignCircle1.setVisibility(VISIBLE);
                Spannable spannable = new SpannableString(model.num);
                spannable.setSpan(new AbsoluteSizeSpan(16, true), 0, model.num.length() - 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new AbsoluteSizeSpan(9, true), model.num.length() - 1, model.num.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ((TextView) userSignCircle1.findViewById(R.id.user_sign_num1)).setText(spannable);
                ((TextView) userSignCircle1.findViewById(R.id.user_sign_name1)).setText(model.name);
            }
            if (i == 1) {
                userSignCircle2.setAlpha(1f);
                userSignCircle2.setVisibility(VISIBLE);
                Spannable spannable = new SpannableString(model.num);
                spannable.setSpan(new AbsoluteSizeSpan(12, true), 0, model.num.length() - 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new AbsoluteSizeSpan(7, true), model.num.length() - 1, model.num.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ((TextView) userSignCircle2.findViewById(R.id.user_sign_num2)).setText(spannable);
                ((TextView) userSignCircle2.findViewById(R.id.user_sign_name2)).setText(model.name);
            }
            if (i == 2) {
                userSignCircle3.setAlpha(1f);
                userSignCircle3.setVisibility(VISIBLE);
                Spannable spannable = new SpannableString(model.num);
                spannable.setSpan(new AbsoluteSizeSpan(12, true), 0, model.num.length() - 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new AbsoluteSizeSpan(7, true), model.num.length() - 1, model.num.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ((TextView) userSignCircle3.findViewById(R.id.user_sign_num3)).setText(spannable);
                ((TextView) userSignCircle3.findViewById(R.id.user_sign_name3)).setText(model.name);
            }
            if (i == 3) {
                userSignCircle4.setAlpha(1f);
                userSignCircle4.setVisibility(VISIBLE);
                Spannable spannable = new SpannableString(model.num);
                spannable.setSpan(new AbsoluteSizeSpan(12, true), 0, model.num.length() - 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new AbsoluteSizeSpan(7, true), model.num.length() - 1, model.num.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ((TextView) userSignCircle4.findViewById(R.id.user_sign_num4)).setText(spannable);
                ((TextView) userSignCircle4.findViewById(R.id.user_sign_name4)).setText(model.name);
            }
        }
    }

    private void resetTotalInfo() {
        ((TextView) userSignCard.findViewById(R.id.user_sigin_total_num)).setText("--次");
        ((TextView) userSignCard.findViewById(R.id.user_sigin_total_time)).setText("");
    }

    private void setTotalInfo(String totalInfo, String totalTime) {
        ((TextView) userSignCard.findViewById(R.id.user_sigin_total_num)).setText(totalInfo);
        ((TextView) userSignCard.findViewById(R.id.user_sigin_total_time)).setText(totalTime);
    }

    private void setDetailInfo(SignModel model) {
        Spannable spannable = new SpannableString(model.num);
        spannable.setSpan(new AbsoluteSizeSpan(16, true), 0, model.num.length() - 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new AbsoluteSizeSpan(9, true), model.num.length() - 1, model.num.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        ((TextView) userSignCardDetail.findViewById(R.id.user_sign_num)).setText(spannable);
        ((TextView) userSignCardDetail.findViewById(R.id.user_sign_name)).setText(model.name);
        int i = 0;
        for (i = 0; i < model.poiName.size(); i++) {
            String poi = model.poiName.get(i);
            if (i == 0) {
                ((TextView) userSignCardDetail.findViewById(R.id.user_sign_poi1)).setText(poi);
                (userSignCardDetail.findViewById(R.id.user_sign_poi1)).setVisibility(VISIBLE);
            }
            if (i == 1) {
                ((TextView) userSignCardDetail.findViewById(R.id.user_sign_poi2)).setText(poi);
                (userSignCardDetail.findViewById(R.id.user_sign_poi2)).setVisibility(VISIBLE);
            }
            if (i == 2) {
                ((TextView) userSignCardDetail.findViewById(R.id.user_sign_poi3)).setText(poi);
                (userSignCardDetail.findViewById(R.id.user_sign_poi3)).setVisibility(VISIBLE);
            }
        }
        for (int j = i; j < 3; j++) {
            if (j == 0) {
                (userSignCardDetail.findViewById(R.id.user_sign_poi1)).setVisibility(GONE);
            }
            if (j == 1) {
                (userSignCardDetail.findViewById(R.id.user_sign_poi2)).setVisibility(GONE);
            }
            if (j == 2) {
                (userSignCardDetail.findViewById(R.id.user_sign_poi3)).setVisibility(GONE);
            }
        }
    }

    boolean isAnimation = false;

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.signin_card_detail) {
            if (monthReportModel != null && monthReportModel.hasNewReport == 1
                    && !TextUtils.isEmpty(monthReportModel.title)) {
                // ControlLogStatistics.getInstance().addLog("PCenterPG.chartClickReport");
                // UserCenterUtils.goToMonthReport(monthReportModel.type, monthReportModel.date);
            } else {
                // ControlLogStatistics.getInstance().addLog("PCenterPG.checkinChartToReview");
                // UserCenterUtils.goToCardDetail("signin");
            }
            return;
        }
        if (isAnimation || current == Status.LOADING) {
            return;
        }

        if (current == Status.NO_DATA && v.getId() == R.id.user_sign_card_default) {
            // ControlLogStatistics.getInstance().addLog("PCenterPG.checkinChartEmptyClick");
            Bundle bundle = new Bundle();
            // bundle.putBoolean(UserSysLvSignPage.SIGNIN_SUCCESS_GOTO_BARRAGE_PAGE, true);
//            TaskManagerFactory.getTaskManager().navigateTo(getContext(),
//                    UserSysLvSignPage.class.getName(), bundle);
            return;
        }
        if (current == Status.NORMAL) {
            // ControlLogStatistics.getInstance().addLog("PCenterPG.checkinChartCircleClick");
            switch (v.getId()) {
                case R.id.user_sign_circle1:
                    if (signModels.size() > 0) {
                        current = Status.DETAIL;
                        setDetailInfo(signModels.get(0));
                        userSignCardDetail.setVisibility(VISIBLE);
                        startAnimation(v, 0);
                    }
                    break;
                case R.id.user_sign_circle2:
                    if (signModels.size() > 1) {
                        current = Status.DETAIL;
                        setDetailInfo(signModels.get(1));
                        userSignCardDetail.setVisibility(VISIBLE);
                        startAnimation(v, 1);
                    }
                    break;
                case R.id.user_sign_circle3:
                    if (signModels.size() > 2) {
                        current = Status.DETAIL;
                        setDetailInfo(signModels.get(2));
                        userSignCardDetail.setVisibility(VISIBLE);
                        startAnimation(v, 2);
                    }
                    break;
                case R.id.user_sign_circle4:
                    if (signModels.size() > 3) {
                        current = Status.DETAIL;
                        setDetailInfo(signModels.get(3));
                        userSignCardDetail.setVisibility(VISIBLE);
                        startAnimation(v, 3);
                    }
                    break;
                default:
                    break;
            }
        }

        if (current == Status.DETAIL) {
            switch (v.getId()) {
                case R.id.user_sign_card_detail:
                    // ControlLogStatistics.getInstance().addLog("PCenterPG.checkinChartCircleClick");
                    current = Status.NORMAL;
                    showCircle();
                    startReverseAnimtion();
                    break;
                default:
                    break;
            }
        }
    }


    AnimationSet detailAnimationSet;
    AnimationSet circleAnimationSet1;
    AnimationSet circleAnimationSet2;
    AnimationSet circleAnimationSet3;
    AlphaAnimation poiDetailAnimation;
    int currentIndex;

    private void startAnimation(View view, final int index) {
        isAnimation = true;
        currentIndex = index;
        float scale = view.getWidth() * 1.0f / ScreenUtils.dip2px(60);

        float diffX = 0;
        float diffY = 0;
        if (index == 0) {
            diffX = -(ScreenUtils.getScreenWidth(getContext()) * 0.9f - 2 * ScreenUtils.dip2px(70)) / 4;
            diffY = -getHeight() / 4;
        } else if (index == 1) {
            diffX = (ScreenUtils.getScreenWidth(getContext()) * 0.9f - 2 * ScreenUtils.dip2px(70)) / 4;
            diffY = -getHeight() / 4;
        } else if (index == 2) {
            diffX = -(ScreenUtils.getScreenWidth(getContext()) * 0.9f - 2 * ScreenUtils.dip2px(70)) / 4;
            diffY = getHeight() / 4;
        } else {
            diffX = (ScreenUtils.getScreenWidth(getContext()) * 0.9f - 2 * ScreenUtils.dip2px(70)) / 4;
            diffY = getHeight() / 4;
        }
        detailAnimationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(scale, 1.0f, scale, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        TranslateAnimation translateAnimation = new TranslateAnimation(diffX, 0, diffY, 0);
        detailAnimationSet.addAnimation(scaleAnimation);
        detailAnimationSet.addAnimation(translateAnimation);
        detailAnimationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        detailAnimationSet.setDuration(300);

        circleAnimationSet1 = getCircleAnimationSet();
        circleAnimationSet2 = getCircleAnimationSet();
        circleAnimationSet3 = getCircleAnimationSet();

        detailAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                hideCircle();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        userSignCircleDetail.startAnimation(detailAnimationSet);

        if (index == 0) {
            userSignCircle1.setVisibility(INVISIBLE);
            userSignCircle2.startAnimation(circleAnimationSet2);
            userSignCircle3.startAnimation(circleAnimationSet3);
            userSignCircle4.startAnimation(circleAnimationSet1);
        } else if (index == 1) {
            userSignCircle1.startAnimation(circleAnimationSet1);
            userSignCircle2.setVisibility(INVISIBLE);
            userSignCircle3.startAnimation(circleAnimationSet3);
            userSignCircle4.startAnimation(circleAnimationSet2);
        } else if (index == 2) {
            userSignCircle1.startAnimation(circleAnimationSet1);
            userSignCircle2.startAnimation(circleAnimationSet2);
            userSignCircle3.setVisibility(INVISIBLE);
            userSignCircle4.startAnimation(circleAnimationSet3);
        } else {
            userSignCircle1.startAnimation(circleAnimationSet1);
            userSignCircle2.startAnimation(circleAnimationSet2);
            userSignCircle3.startAnimation(circleAnimationSet3);
            userSignCircle4.setVisibility(INVISIBLE);
        }
        poiDetailAnimation = new AlphaAnimation(0.0f, 1.0f);
        poiDetailAnimation.setDuration(300);

        userSignPoiContainer.startAnimation(poiDetailAnimation);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                hideCircle();
                userSignCircleDetail.clearAnimation();
                userSignPoiContainer.clearAnimation();
                userSignCircle1.clearAnimation();
                userSignCircle2.clearAnimation();
                userSignCircle3.clearAnimation();
                userSignCircle4.clearAnimation();
                isAnimation = false;
            }
        }, detailAnimationSet.getDuration());
    }

    AnimationSet getCircleAnimationSet() {
        AnimationSet circleAnimationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        ScaleAnimation scaleAnimation1 = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        circleAnimationSet.addAnimation(alphaAnimation);
        circleAnimationSet.addAnimation(scaleAnimation1);
        circleAnimationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        circleAnimationSet.setDuration(300);
        return circleAnimationSet;
    }

    private void startReverseAnimtion() {
        isAnimation = true;
        detailAnimationSet.setInterpolator(new ReverseInterpolator(new AccelerateDecelerateInterpolator()));
        userSignCircleDetail.startAnimation(detailAnimationSet);
        circleAnimationSet1.setInterpolator(new ReverseInterpolator(new AccelerateDecelerateInterpolator()));
        circleAnimationSet2.setInterpolator(new ReverseInterpolator(new AccelerateDecelerateInterpolator()));
        circleAnimationSet3.setInterpolator(new ReverseInterpolator(new AccelerateDecelerateInterpolator()));
        showCircle();
        poiDetailAnimation.setInterpolator(new ReverseInterpolator());

        detailAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showCircle();
                userSignCardDetail.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (currentIndex == 0) {
            userSignCircle1.setVisibility(INVISIBLE);
            userSignCircle2.startAnimation(circleAnimationSet2);
            userSignCircle3.startAnimation(circleAnimationSet3);
            userSignCircle4.startAnimation(circleAnimationSet1);
        } else if (currentIndex == 1) {
            userSignCircle1.startAnimation(circleAnimationSet1);
            userSignCircle2.setVisibility(INVISIBLE);
            userSignCircle3.startAnimation(circleAnimationSet3);
            userSignCircle4.startAnimation(circleAnimationSet2);
        } else if (currentIndex == 2) {
            userSignCircle1.startAnimation(circleAnimationSet1);
            userSignCircle2.startAnimation(circleAnimationSet2);
            userSignCircle3.setVisibility(INVISIBLE);
            userSignCircle4.startAnimation(circleAnimationSet3);
        } else {
            userSignCircle1.startAnimation(circleAnimationSet1);
            userSignCircle2.startAnimation(circleAnimationSet2);
            userSignCircle3.startAnimation(circleAnimationSet3);
            userSignCircle4.setVisibility(INVISIBLE);
        }
        userSignPoiContainer.startAnimation(poiDetailAnimation);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                showCircle();
                userSignCardDetail.setVisibility(GONE);
                userSignCircleDetail.clearAnimation();
                userSignPoiContainer.clearAnimation();
                userSignCircle1.clearAnimation();
                userSignCircle2.clearAnimation();
                userSignCircle3.clearAnimation();
                userSignCircle4.clearAnimation();
                isAnimation = false;
            }
        }, detailAnimationSet.getDuration());
    }

    private void showCircle() {
        userSignCircle1.setVisibility(VISIBLE);
        userSignCircle2.setVisibility(VISIBLE);
        userSignCircle3.setVisibility(VISIBLE);
        userSignCircle4.setVisibility(VISIBLE);
    }

    private void hideCircle() {
        userSignCircle1.setVisibility(INVISIBLE);
        userSignCircle2.setVisibility(INVISIBLE);
        userSignCircle3.setVisibility(INVISIBLE);
        userSignCircle4.setVisibility(INVISIBLE);
    }

    public static final class ReverseInterpolator implements Interpolator {
        private final Interpolator delegate;

        public ReverseInterpolator(Interpolator delegate) {
            this.delegate = delegate;
        }

        public ReverseInterpolator() {
            this(new LinearInterpolator());
        }

        @Override
        public float getInterpolation(float input) {
            return 1 - delegate.getInterpolation(input);
        }
    }
}
