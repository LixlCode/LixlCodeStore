package com.wecome.demo.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wecome.demo.R;
import com.wecome.demo.controller.HomePageController;
import com.wecome.demo.model.HomeItemDataModel;


/**
 * 首页常用功能Itemview
 * Created by zhaoliqiong on 17/7/5.
 */

public class HomeItemView extends ConstraintLayout {

    private ImageView mIcon;

    private TextView mTitle;

    private TextView mRedPoint;
    private TextView mNewPoint;
    private HomeItemDataModel mData;

    private Context mContext;

    public HomeItemView(Context context) {
        super(context);
        this.mContext = context;
    }

    public HomeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public HomeItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    private void initViews() {
        mIcon = (ImageView) findViewById(R.id.item_icon);
        mTitle = (TextView) findViewById(R.id.item_text);

        mNewPoint = (TextView) findViewById(R.id.icon_newpot);
        mRedPoint = (TextView) findViewById(R.id.icon_redpot);
    }

    public void onUpdate(HomeItemDataModel data, HomePageController.ToolsHeaderListener listener) {
        if (data == null) {
            return;
        }
        if (mIcon == null || mTitle == null) {
            initViews();
        }
        // mIcon.setImageRes(data.mIconResId);
        Glide.with(mContext).load(data.mIconResId).into(mIcon);

        mTitle.setText(getResources().getString(data.nameStringId));
        mData = data;

        if (data.isRedPoint) {
            mRedPoint.setVisibility(View.VISIBLE);
        } else {
            mRedPoint.setVisibility(View.GONE);
        }

        if (data.isNewPoint) {
            mNewPoint.setVisibility(View.VISIBLE);
        } else {
            mNewPoint.setVisibility(View.GONE);
        }
        setOnClickListener(listener);
    }
}