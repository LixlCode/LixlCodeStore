package com.wecome.demo.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wecome.demo.R;

public class HomeExpandableListItemView extends LinearLayout {

    public enum ITEM_TYPE {
        SINGLE, TOP, MIDDLE, BOTTOM
    }

    private ImageView redPoint;
    private TextView leftText;
    private TextView rightText;
    private TextView specialRightText;
    private View topDivider;
    private View topEmpty;
    private Context mContext;

    public HomeExpandableListItemView(Context context) {
        super(context);
        initView(context);
    }

    public HomeExpandableListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HomeExpandableListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        if (context == null) {
            return;
        }
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.home_list_item, this);
        redPoint = (ImageView) findViewById(R.id.iv_red_point);
        leftText = (TextView) findViewById(R.id.tv_left_text);
        rightText = (TextView) findViewById(R.id.tv_right_text);
        specialRightText = (TextView) findViewById(R.id.tv_unsync_count);
        topDivider = findViewById(R.id.vw_divider_top);
        topEmpty = findViewById(R.id.top_empty);
    }

    public void setLeftText(String text) {
        leftText.setText(text);
    }

    public void setRightText(int visibility, String text) {
        if (visibility != GONE && !TextUtils.isEmpty(text)) {
            specialRightText.setVisibility(GONE);
            rightText.setVisibility(VISIBLE);
            rightText.setText(text);
        } else {
            rightText.setVisibility(GONE);
        }
    }

    public void setSpecialRightText(int visibility, String text) {
        if (visibility != GONE && !TextUtils.isEmpty(text)) {
            rightText.setVisibility(GONE);
            specialRightText.setVisibility(VISIBLE);
            specialRightText.setText(text);
        } else {
            specialRightText.setVisibility(GONE);
        }
    }

    public void showRedPoint(boolean isShowRedPoint) {
        if (isShowRedPoint) {
            redPoint.setVisibility(VISIBLE);
        } else {
            redPoint.setVisibility(GONE);
        }
    }

    public void setType(ITEM_TYPE type) {
        switch (type) {
            case SINGLE:
                topDivider.setVisibility(VISIBLE);
                topEmpty.setVisibility(VISIBLE);
                break;
            case TOP:
                topDivider.setVisibility(VISIBLE);
                topEmpty.setVisibility(VISIBLE);
                break;
            case MIDDLE:
                topDivider.setVisibility(GONE);
                break;
            case BOTTOM:
                topDivider.setVisibility(GONE);
                break;
        }
    }

}
