package com.wecome.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.wecome.demo.R;
import com.wecome.demo.utils.ScreenUtils;

/**
 * 地图通用CheckBox, 大小53dp*33dp, 圆的大小33dp*33dp
 *
 * @author wangyongyu
 * @date 16/5/5
 */
public class BMCheckBox extends FrameLayout {

    private static final int HEIGHT = 33;
    private static final int WIDTH = 53;

    private boolean   mIsChecked;
    private View background;
    private ImageView foreground;
    private int       len;

    public BMCheckBox(Context context) {
        this(context, null);
    }

    public BMCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BMCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a =
                context.obtainStyledAttributes(attrs, R.styleable.BMCheckBox, defStyleAttr, 0);
        mIsChecked = a.getBoolean(R.styleable.BMCheckBox_check, false);
        len = ScreenUtils.dip2px(WIDTH - HEIGHT, context);
        background = new View(context);
        int w = ScreenUtils.dip2px(WIDTH, context);
        int h = ScreenUtils.dip2px(HEIGHT, context);
        LayoutParams lp1 = new LayoutParams(w, h);
        lp1.setMargins(0, 0, 0, 0);
        lp1.gravity = Gravity.FILL | Gravity.CENTER;
        addView(background, lp1);

        foreground = new ImageView(context);
        foreground.setImageResource(R.mipmap.iv_checkbox_button);

        addView(foreground, new LayoutParams(h, h));
        updateUI();
        a.recycle();
    }

    /**
     * CHECKABLE样式，设置选中状态
     */
    public void updateUI() {

        if (mIsChecked) {
            // 选中
            background.setBackgroundResource(R.drawable.iv_checkbox_selected);
            LayoutParams lp = (LayoutParams) foreground.getLayoutParams();
            lp.gravity = Gravity.CENTER_VERTICAL | Gravity.FILL_VERTICAL;
            lp.setMargins(len, 0, 0, 0);
            foreground.setLayoutParams(lp);
        } else {
            background.setBackgroundResource(R.drawable.iv_checkbox_unselected);
            LayoutParams lp = (LayoutParams) foreground.getLayoutParams();
            lp.gravity = Gravity.CENTER_VERTICAL | Gravity.FILL_VERTICAL;
            lp.setMargins(0, 0, len, 0);
            foreground.setLayoutParams(lp);
        }
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean checked) {
        mIsChecked = checked;
        updateUI();
    }

    public void changeCheckStatus() {
        mIsChecked = !mIsChecked;
        updateUI();
    }

}
