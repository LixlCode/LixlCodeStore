package com.wecome.demo.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wecome.demo.R;
import com.wecome.demo.constants.SettingCenterConstants;
import com.wecome.demo.model.SettingCenterModel;
import com.wecome.demo.model.SettingPageModel;
import com.wecome.demo.view.SettingCenterListItemView;

import java.util.ArrayList;
import java.util.List;

public class SettingAdapter extends /*BaseQuickAdapter<SettingPageModel, BaseViewHolder>*/
                                    BaseExpandableListAdapter {

//    private Activity mContext;
//
//    public SettingAdapter(int layoutResId) {
//        super(layoutResId);
//    }
//
//    public SettingAdapter(@Nullable List<SettingPageModel> data) {
//        super(data);
//    }
//
//    public SettingAdapter(Activity mContext, int layoutResId, @Nullable List<SettingPageModel> data) {
//        super(layoutResId, data);
//        this.mContext = mContext;
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, SettingPageModel item) {
//        helper.setText(R.id.tv_setting, item.getStr());
//        Glide.with(mContext).load(item.getImg()).into((ImageView) helper.getView(R.id.iv_image));
//    }

    private ArrayList<ArrayList<SettingPageModel>>
            mGroupChildArray = new ArrayList<ArrayList<SettingPageModel>>();


    private Context mContext;

    public SettingAdapter(Context context) {
        mContext = context;
    }


    public void setData(ArrayList<SettingPageModel> list) {
        mGroupChildArray.add(list);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mGroupChildArray != null) {
            mGroupChildArray.clear();
        }
    }


    @Override
    public int getGroupCount() {
        return mGroupChildArray.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mGroupChildArray.size() > i ? mGroupChildArray.get(i).size() : 0;
    }

    @Override
    public ArrayList<SettingPageModel> getGroup(int i) {
        return mGroupChildArray.size() > i ? mGroupChildArray.get(i) : null;
    }

    @Override
    public SettingPageModel getChild(int i, int i2) {
        return mGroupChildArray.size() > i ? (mGroupChildArray.get(i).size() > i2 ? mGroupChildArray.get(i).get(i2) : null) : null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i2) {
        return i2;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new View(mContext);
        }

        int height = 0;
        if (groupPosition != 0) {
            height = mContext.getResources().getDimensionPixelSize(R.dimen.user_center_group_margin);
        }
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        convertView.setLayoutParams(params);
        convertView.setBackgroundResource(R.color.common_background);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        SettingPageModel item = getChild(groupPosition, childPosition);
        if (item == null) {
            return null;
        }

        if (convertView == null) {
            convertView = new SettingCenterListItemView(mContext);
        }
        SettingCenterListItemView itemView = (SettingCenterListItemView) convertView;

        if (itemView.getDivider() == null) {
            return convertView;
        }
        // 策略：
        // 1.list(当前分组，下同)只有一个的情况，模式当然是single_line啦=,=
        // 2.list.size>1&&位置为最后一个，item模式变为bottom
        // 3.list.size>1&&位置为第一个，item模式变为top
        // 4.其他情况，item模式为middle.
        if (mGroupChildArray.size() > groupPosition && mGroupChildArray.get(groupPosition).size() == 1) {
            itemView.setType(SettingCenterListItemView.ItemType.SINGLE);
        } else if (mGroupChildArray.size() > groupPosition
                && childPosition == mGroupChildArray.get(groupPosition).size() - 1) {
            itemView.setType(SettingCenterListItemView.ItemType.BOTTOM);
        } else if (childPosition == 0) {
            itemView.setType(SettingCenterListItemView.ItemType.TOP);
        } else {
            itemView.setType(SettingCenterListItemView.ItemType.MIDDLE);
        }

        itemView.showRedPoint(item.isShowRedPoint());
        itemView.setLeftText(mContext.getString(item.getLeftText()));
        if (item.isShowSecondaryLeftText()) {
            itemView.setLeftSecondaryText(View.VISIBLE, mContext.getString(item.getSecondaryLeftText()));
        } else {
            itemView.setLeftSecondaryText(View.GONE, "");
        }

        if (item.getStyle() == SettingCenterConstants.EXPANDLIST_STYLE_CHECKADBE) {
            itemView.setStyle(SettingCenterConstants.EXPANDLIST_STYLE_CHECKADBE);
            itemView.setChecked(item.isChecked());
        } else {
            itemView.setStyle(SettingCenterConstants.EXPANDLIST_STYLE_NORMAL);
        }

        itemView.showLeftImage(true);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), item.getLeftImgResId());
        itemView.setLeftImage(mContext, bitmap);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


}
