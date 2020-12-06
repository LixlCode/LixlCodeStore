package com.wecome.demo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wecome.demo.R;

import java.util.HashMap;

/**
 * Created by chenjie07 on 17/8/27.
 */

public class SimulateEarth extends RelativeLayout implements ViewPager.OnPageChangeListener, View.OnClickListener {

    InternationalCard card;
    ViewPager mViewPager;
    EarthCountyAdapter mAdapter;
    int currentPostion = 0;

    public SimulateEarth(Context context) {
        super(context);
        init();
    }

    public SimulateEarth(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimulateEarth(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public SimulateEarth(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setCard(InternationalCard card) {
        this.card = card;
    }

    /**
     * 初始化View
     */
    private void init() {
        setBackgroundResource(R.mipmap.earth_bg);
        CircleClipRegion view = (CircleClipRegion) LayoutInflater.from(getContext())
                .inflate(R.layout.earth_country_layout, null);
        view.setClickListener(this);
        addView(view);
        LayoutParams params = (LayoutParams) view.getLayoutParams();
        params.addRule(CENTER_IN_PARENT);
        view.setLayoutParams(params);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mAdapter = new EarthCountyAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        requestLayout();
        if (mViewPager != null) {
            int position = currentPostion;
            if (currentPostion == 0) {
                position = EarthCountyAdapter.COUNT - 2;
            } else if (currentPostion == EarthCountyAdapter.COUNT - 1) {
                position = 1;
            }
            mViewPager.setCurrentItem(position, false);
        }
    }

    boolean isIdle = true;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffsetPixels == 0) {
            isIdle = true;
        } else {
            isIdle = false;
        }
        if (0 != positionOffsetPixels) {
            return;
        }

        if (position == 0) {
            position = EarthCountyAdapter.COUNT - 2;
        } else if (position == EarthCountyAdapter.COUNT - 1) {
            position = 1;
        }
        if (positionOffsetPixels == 0) {
            mViewPager.setCurrentItem(position, false);
        }
    }

    @Override
    public void onPageSelected(int position) {
        currentPostion = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 0表示东半球，表示西半球
     *
     * @return
     */
    public int getCurrentStatus() {
        return currentPostion % 2;
    }

    @Override
    public void onClick(View v) {
        if (isIdle && !card.isCardAnimation) {
            // ControlLogStatistics.getInstance().addLog("PCenterPG.internationalChartClickEarch");
            // 容错
            if (currentPostion == 0) {
                currentPostion = EarthCountyAdapter.COUNT - 2;
            }
            card.refreshCountyView((currentPostion - 1) % 2);
            mViewPager.setCurrentItem(currentPostion - 1, true);
        }
    }

    public static class EarthCountyAdapter extends PagerAdapter {
        public static final int COUNT = 4;
        HashMap<Integer, ImageView> cacheView = new HashMap<>();

        @Override
        public int getCount() {
            return COUNT;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = cacheView.get(position);
            if (view == null) {
                view = new ImageView(container.getContext());
                cacheView.put(position, view);
                if (position % 2 == 0) {
                    view.setBackgroundResource(R.mipmap.east_earth);
                } else {
                    view.setBackgroundResource(R.mipmap.west_earth);
                }
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
    }
}


