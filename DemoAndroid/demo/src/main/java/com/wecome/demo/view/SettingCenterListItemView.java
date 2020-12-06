package com.wecome.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wecome.demo.R;
import com.wecome.demo.constants.SettingCenterConstants;

public class SettingCenterListItemView extends LinearLayout {

    public enum ItemType {
        SINGLE, TOP, MIDDLE, BOTTOM
    }

    private ImageView redPoint;
    private ImageView rightArrow;
    private TextView leftText;
    private TextView leftSecondaryText;
    private View divider;
    private View topDivider;
    private View bottomDivider;
    private BMCheckBox checkBox;
    private ImageView iv_imag;

    public SettingCenterListItemView(Context context) {
        super(context);
        initView(context);
    }

    public SettingCenterListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SettingCenterListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        if (context == null) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            View view = inflater.inflate(R.layout.set_list_item, this);
            view.setBackgroundResource(R.drawable.common_background_white_item);
        } catch (Exception e) {
            // android.content.res.Resources$NotFoundException:
            // Resource is not a Drawable (color or path)
            // do nothing
        }

        redPoint = (ImageView) findViewById(R.id.iv_red_point);
        leftText = (TextView) findViewById(R.id.tv_left_text);
        rightArrow = (ImageView) findViewById(R.id.iv_right_arrow);
        iv_imag = (ImageView) findViewById(R.id.iv_imag);
        leftSecondaryText = (TextView) findViewById(R.id.tv_left_text_secondary);
        divider = findViewById(R.id.vw_divider);
        topDivider = findViewById(R.id.vw_divider_top);
        bottomDivider = findViewById(R.id.vw_divider_bottom);
        checkBox = (BMCheckBox) findViewById(R.id.iv_right_checkBox);
    }

    public void setLeftText(String text) {
        leftText.setText(text);
    }

    public TextView getLeftText() {
        return leftText;
    }

    public void showLeftImage(Boolean isShow) {
        if (isShow){
            iv_imag.setVisibility(VISIBLE);
        } else {
            iv_imag.setVisibility(GONE);
        }
    }

    public void setLeftImage(Context mContext, Bitmap bitmap) {
        Glide.with(mContext).load(bitmap).into(iv_imag);
    }

    public void setLeftSecondaryText(int visibility, String text) {
        int height;
        if (visibility != GONE && !TextUtils.isEmpty(text)) {
            // 如果第二行有文字，高度为65dp
            leftSecondaryText.setVisibility(VISIBLE);
            leftSecondaryText.setText(text);
            height = getContext().getResources().getDimensionPixelSize(R.dimen.user_center_list_item_double);
        } else {
            // 如果第二行没有文字，设置item高度为55dp
            leftSecondaryText.setVisibility(GONE);
            height = getContext().getResources().getDimensionPixelSize(R.dimen.user_center_list_item_single);
        }
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        this.setLayoutParams(params);
    }

    public void showRedPoint(boolean isShowRedPoint) {
        if (isShowRedPoint) {
            redPoint.setVisibility(VISIBLE);
        } else {
            redPoint.setVisibility(GONE);
        }
    }

    public void setType(ItemType type) {
        switch (type) {
            case SINGLE:
                divider.setVisibility(GONE);
                topDivider.setVisibility(VISIBLE);
                bottomDivider.setVisibility(VISIBLE);
                break;
            case TOP:
                divider.setVisibility(VISIBLE);
                topDivider.setVisibility(VISIBLE);
                bottomDivider.setVisibility(GONE);
                break;
            case MIDDLE:
                divider.setVisibility(VISIBLE);
                topDivider.setVisibility(GONE);
                bottomDivider.setVisibility(GONE);
                break;
            case BOTTOM:
                divider.setVisibility(VISIBLE);
                topDivider.setVisibility(GONE);
                bottomDivider.setVisibility(VISIBLE);
                break;

            default:
                break;
        }
    }

    /**
     * 设置ListItem 的样式
     *
     * @param style
     */
    public void setStyle(int style) {
        switch (style) {
            case SettingCenterConstants.EXPANDLIST_STYLE_CHECKADBE:
                rightArrow.setVisibility(GONE);
                checkBox.setVisibility(VISIBLE);
                break;
            case SettingCenterConstants.EXPANDLIST_STYLE_NORMAL:
                rightArrow.setVisibility(VISIBLE);
                checkBox.setVisibility(GONE);
                break;
            default:
                break;
        }
    }

    /**
     * CHECKABLE样式，设置选中状态
     *
     * @param checked
     */
    public void setChecked(boolean checked) {
        if (checkBox.getVisibility() != VISIBLE) {
            return;
        }
        checkBox.setChecked(checked);
    }

    public View getDivider() {
        return divider;
    }
}
