package com.wecome.demo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wecome.demo.R;
import com.wecome.demo.model.InternationalModel;
import com.wecome.demo.utils.ScreenUtils;

import java.util.ArrayList;


/**
 * Created by chenjie07 on 17/8/26.
 * 国际化卡片
 */

public class InternationalCard extends RelativeLayout implements View.OnClickListener {
    private static final String ELLIPSIS = "...";
    public static final int MAX_LINES = 2;
    private View countryContainer;
    private View cityContainer;
    private SimulateEarth simulateEarth;
    ArrayList<TextView> countryNormals = new ArrayList<>();
    ArrayList<View> countryEmptys = new ArrayList<>();

    private TextView cityNameView;
    private TextView cityNumView;
    private TextView countryNameView;
    private View cityDetailView;
    private View cityMapView;
    private TextView totalView;

    InternationalModel data;
    public boolean isCardAnimation = false;

    public InternationalCard(Context context) {
        super(context);
        init();
    }

    public InternationalCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InternationalCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public InternationalCard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.user_sys_international_card, this);
        totalView = (TextView) view.findViewById(R.id.total_num);
        simulateEarth = (SimulateEarth) view.findViewById(R.id.simulate_earth);
        simulateEarth.setCard(this);
        countryContainer = view.findViewById(R.id.country_container);
        cityContainer = view.findViewById(R.id.city_container);
        cityContainer.setOnClickListener(this);
        view.findViewById(R.id.international_detail).setOnClickListener(this);
        initCountyView();
        initCityView();
    }

    private void initCountyView() {
        countryNormals.add((TextView) countryContainer.findViewById(R.id.country_normal_0));
        countryNormals.add((TextView) countryContainer.findViewById(R.id.country_normal_1));
        countryNormals.add((TextView) countryContainer.findViewById(R.id.country_normal_2));
        countryNormals.add((TextView) countryContainer.findViewById(R.id.country_normal_3));
        countryNormals.add((TextView) countryContainer.findViewById(R.id.country_normal_4));
        countryNormals.add((TextView) countryContainer.findViewById(R.id.country_normal_5));

        for (int i = 0; i < countryNormals.size(); i++) {
            countryNormals.get(i).setTag(R.id.user_international_tag, i);
            countryNormals.get(i).setOnClickListener(this);
        }
        countryEmptys.add(countryContainer.findViewById(R.id.country_empty_0));
        countryEmptys.add(countryContainer.findViewById(R.id.country_empty_1));
        countryEmptys.add(countryContainer.findViewById(R.id.country_empty_2));
        countryEmptys.add(countryContainer.findViewById(R.id.country_empty_3));
        countryEmptys.add(countryContainer.findViewById(R.id.country_empty_4));
        countryEmptys.add(countryContainer.findViewById(R.id.country_empty_5));
    }

    private void initCityView() {
        countryNameView = (TextView) cityContainer.findViewById(R.id.city_county);
        cityNumView = (TextView) cityContainer.findViewById(R.id.city_num);
        cityNameView = (TextView) cityContainer.findViewById(R.id.city_name);
        cityMapView = cityContainer.findViewById(R.id.city_map);
        cityDetailView = cityContainer.findViewById(R.id.city_detail);
    }

    public void notifyDataChanged(InternationalModel data) {
        cityContainer.setVisibility(INVISIBLE);
        countryContainer.setVisibility(VISIBLE);
        this.data = data;
        isCardAnimation = false;
        if (data.eastData.size() > 0) {
            simulateEarth.mViewPager.setCurrentItem(2);
        } else if (data.westData.size() > 0) {
            simulateEarth.mViewPager.setCurrentItem(1);
        }
        SpannableString str = makeSpan(data.totalNum + "个国家/地区", 5, 0xff68ce4f, 0xff999999);
        totalView.setText(str);
        setCountyView(simulateEarth.getCurrentStatus());
    }

    private SpannableString makeSpan(String s, int pos, int colorLeft, int colorRight) {
        pos = s.length() - pos;
        SpannableString spannableString = new SpannableString(s);
        spannableString.setSpan(new ForegroundColorSpan(colorLeft), 0, pos, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(colorRight), pos,
                s.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public void setCountyView(int status) {
        ArrayList<InternationalModel.CountryModel> source = null;
        if (status == 0) {
            source = data.eastData;
        } else {
            source = data.westData;
        }
        int count = source.size();
        for (int i = 0; i < count && i < 6; i++) {
            countryNormals.get(i).setVisibility(VISIBLE);
            countryEmptys.get(i).setVisibility(INVISIBLE);
            countryNormals.get(i).setText(InternationalModel.CountryModel.format(source.get(i).countryName));
        }

        for (int i = count; i < 6; i++) {
            countryNormals.get(i).setVisibility(INVISIBLE);
            countryEmptys.get(i).setVisibility(VISIBLE);
        }
        simulateEarth.setVisibility(VISIBLE);
    }

    public void refreshCountyView(int status) {
        ArrayList<InternationalModel.CountryModel> source = null;
        if (status == 0) {
            source = data.eastData;
        } else {
            source = data.westData;
        }
        ArrayList<View> currentVisibile = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (countryNormals.get(i).getVisibility() == VISIBLE) {
                currentVisibile.add(countryNormals.get(i));
            }
            if (countryEmptys.get(i).getVisibility() == VISIBLE) {
                currentVisibile.add(countryEmptys.get(i));
            }
        }
        ArrayList<View> nextVisible = new ArrayList<>();
        int count = source.size();
        for (int i = 0; i < count; i++) {
            countryNormals.get(i).setTag(source.get(i));
            nextVisible.add(countryNormals.get(i));
        }

        for (int i = count; i < 6; i++) {
            nextVisible.add(countryEmptys.get(i));
        }
        startClickEarthAnimation(currentVisibile, nextVisible);
    }

    private void startClickEarthAnimation(final ArrayList<View> current, final ArrayList<View> next) {
        for (int i = 0; i < current.size(); i++) {
            final int index = i;
            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    current.get(index).setVisibility(INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            current.get(index).startAnimation(scaleAnimation);
        }

        for (int i = 0; i < next.size(); i++) {
            final int index = i;
            final ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    next.get(index).setVisibility(VISIBLE);
                    if (next.get(index).getTag() != null) {
                        InternationalModel.CountryModel modle = (InternationalModel.CountryModel) next
                                .get(index).getTag();
                        ((TextView) next.get(index)).setText(InternationalModel.CountryModel.format(modle.countryName));
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    next.get(index).startAnimation(scaleAnimation);
                }
            }, 200);
        }
    }

    private void refreshCityView(InternationalModel.CountryModel model) {
        cityContainer.setVisibility(VISIBLE);
        countryNameView.setText(InternationalModel.CountryModel.format(model.countryName));
        cityNumView.setText(model.citys.size() + "个城市");

        String content = TextUtils.join("    ", model.citys);
        int width = ScreenUtils.dip2px(133);
        cityNameView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        StaticLayout layout = createStaticLayout(content, cityNameView, width);
        if (layout.getLineCount() > MAX_LINES) {
            cityNameView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
            layout = createStaticLayout(content, cityNameView, width);
            if (layout.getLineCount() > MAX_LINES) {
                int lineEndIndex = layout.getLineEnd(MAX_LINES - 1);
                String str = content.substring(0, lineEndIndex).trim();
                for (int i = 0; i < str.length(); i++) {
                    StaticLayout layout1 = createStaticLayout(str.substring(0, str.length() - i) + ELLIPSIS,
                            cityNameView, width);
                    if (layout1.getLineCount() <= MAX_LINES) {
                        cityNameView.setText(str.substring(0, str.length() - i) + ELLIPSIS);
                        return;
                    }
                }
            } else {
                cityNameView.setText(content);
            }
        } else {
            cityNameView.setText(content);
        }
    }

    private ArrayList<View> clickVisibleView;

    private void startClickCountryAniamtion(final int select) {
        clickVisibleView = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (countryNormals.get(i).getVisibility() == VISIBLE) {
                clickVisibleView.add(countryNormals.get(i));
            }
            if (countryEmptys.get(i).getVisibility() == VISIBLE) {
                clickVisibleView.add(countryEmptys.get(i));
            }
        }
        clickVisibleView.add(simulateEarth);
        for (int i = 0; i < clickVisibleView.size(); i++) {
            if (i == select) {
                AnimationSet animationSet = new AnimationSet(true);
                float scale = clickVisibleView.get(select).getWidth() * 1.0f / ScreenUtils.dip2px(37);
                ScaleAnimation scaleAnimation = new ScaleAnimation(scale, 1.0f, scale, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                int[] loc1 = new int[2];
                int[] loc2 = new int[2];
                clickVisibleView.get(select).getLocationOnScreen(loc1);
                countryNameView.getLocationOnScreen(loc2);
                int delta = (clickVisibleView.get(select).getWidth() - ScreenUtils.dip2px(37)) / 2;
                TranslateAnimation translateAnimation = new TranslateAnimation(loc1[0] - loc2[0] + delta, 0,
                        loc1[1] - loc2[1] + delta, 0);
                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(translateAnimation);
                animationSet.setDuration(500);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        clickVisibleView.get(select).setVisibility(INVISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        countryContainer.setVisibility(INVISIBLE);
                        isCardAnimation = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                countryNameView.startAnimation(animationSet);


            } else {
                final int index = i;
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(300);
                scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        clickVisibleView.get(index).setVisibility(INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                clickVisibleView.get(index).startAnimation(scaleAnimation);
            }
        }
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setStartOffset(300);
        cityDetailView.startAnimation(alphaAnimation);

        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, cityMapView.getHeight(), 0);
        translateAnimation.setDuration(200);
        translateAnimation.setStartOffset(300);
        cityMapView.startAnimation(translateAnimation);
    }

    public static InternationalModel getModel() {
        InternationalModel model = new InternationalModel();
        int count = (int) (7 * Math.random());
        for (int i = 0; i < count; i++) {
            InternationalModel.CountryModel country = new InternationalModel.CountryModel();
            country.countryName = "利比亚";
            country.citys.add("北京");
            country.citys.add("上海");
            country.citys.add("北京");
            country.citys.add("上海");
            country.citys.add("北京");
            country.citys.add("上海");

            model.eastData.add(country);
        }

        int count2 = (int) (7 * Math.random());
        for (int i = 0; i < count2; i++) {
            InternationalModel.CountryModel country = new InternationalModel.CountryModel();
            country.countryName = "印度尼西亚";
            country.citys.add("拉进来");
            country.citys.add("刻录机看");
            country.citys.add("刻录机看");
            country.citys.add("拉进来");
            country.citys.add("刻录机看");
            country.citys.add("刻录机看");
            country.citys.add("拉进来");
            country.citys.add("刻录机看");
            country.citys.add("刻录机看");
            country.citys.add("拉进来");
            country.citys.add("刻录机看");
            country.citys.add("刻录机看");
            model.westData.add(country);
        }
        return model;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.country_normal_0:
            case R.id.country_normal_1:
            case R.id.country_normal_2:
            case R.id.country_normal_3:
            case R.id.country_normal_4:
            case R.id.country_normal_5:
                if (isCardAnimation || !simulateEarth.isIdle) {
                    return;
                }
                // ControlLogStatistics.getInstance().addLog("PCenterPG.internationalChartClickCountry");
                int index = (int) v.getTag(R.id.user_international_tag);
                if (simulateEarth.getCurrentStatus() == 0) {
                    if (index < data.eastData.size()) {
                        isCardAnimation = true;
                        refreshCityView(data.eastData.get(index));
                        startClickCountryAniamtion(index);
                    }
                } else {
                    if (index < data.westData.size()) {
                        isCardAnimation = true;
                        refreshCityView(data.westData.get(index));
                        startClickCountryAniamtion(index);
                    }
                }
                break;
            case R.id.city_container:
                if (isCardAnimation) {
                    return;
                }
                reverseAnimation();
                break;
            case R.id.international_detail:
                if (!TextUtils.isEmpty(data.url)) {
                    // ControlLogStatistics.getInstance().addLog("PCenterPG.internationalChartToReview");
                    //gotoWebShell(data.url);
                }
                break;
            default:
                break;
        }
    }

    private void reverseAnimation() {
        isCardAnimation = true;
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(200);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cityContainer.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        cityContainer.startAnimation(animation);
        countryContainer.setVisibility(VISIBLE);
        setCountyView(simulateEarth.getCurrentStatus());

        for (int i = 0; i < clickVisibleView.size(); i++) {
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(300);
            scaleAnimation.setDuration(200);
            scaleAnimation.setStartOffset(200);
            clickVisibleView.get(i).startAnimation(scaleAnimation);
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                isCardAnimation = false;
            }
        }, 520);
    }

    private StaticLayout createStaticLayout(String content, TextView view, int width) {
        return new StaticLayout(content, view.getPaint(),
                width - view.getPaddingLeft() - view.getPaddingRight(),
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, false);
    }

//    private void gotoWebShell(String url) {
//        Bundle bundle = new Bundle();
//        bundle.putString(WebViewConst.WEBVIEW_URL_KEY, url);
//        int flag = 0;
//        flag |= WebShellPage.FLAG_ALLOW_WEBVIEW_BACK;
//        flag |= WebShellPage.FLAG_NOT_SHOW_BOTTOM_PANEL;
//        flag |= WebShellPage.FLAG_ATTACH_SETINFO_FUNC;
//        bundle.putInt(WebViewConst.WEBSHELL_FLAG_KEY, flag);
//
//        TaskManagerFactory.getTaskManager().navigateTo(getContext(), WebShellPage.class.getName(),
//                bundle);
//    }
}
