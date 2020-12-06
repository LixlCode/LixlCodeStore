package com.wecome.demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;

import com.wecome.demo.R;
import com.wecome.demo.constants.SettingCenterConstants;
import com.wecome.demo.model.HomeExpandableListModel;
import com.wecome.demo.view.HomeExpandableListItemView;

import java.util.ArrayList;

public class HomeExpandableListAdapter extends BaseExpandableListAdapter {

    private ArrayList<ArrayList<HomeExpandableListModel>> mGroupChildArray
            = new ArrayList<ArrayList<HomeExpandableListModel>>();

    private Context mContext;

    public HomeExpandableListAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList<HomeExpandableListModel> list) {
        mGroupChildArray.add(list);
        notifyDataSetChanged();
    }

    public void clearData() {
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
    public ArrayList<HomeExpandableListModel> getGroup(int i) {
        return mGroupChildArray.size() > i ? mGroupChildArray.get(i) : null;
    }

    @Override
    public HomeExpandableListModel getChild(int i, int i2) {
        return mGroupChildArray.size() > i ? (mGroupChildArray.get(i).size() > i2 ? mGroupChildArray.get(i).get(i2)
                : null) : null;
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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        HomeExpandableListModel item = getChild(groupPosition, childPosition);//mGroupChildArray.get(groupPosition).get(childPosition);
        if (item == null) {
            return null;
        }

        if (convertView == null) {
            convertView = new HomeExpandableListItemView(mContext);
        }
        HomeExpandableListItemView itemView = (HomeExpandableListItemView) convertView;

        // 策略：
        // 1.list(当前分组，下同)只有一个的情况，模式当然是single_line啦=,=
        // 2.list.size>1&&位置为最后一个，item模式变为bottom
        // 3.list.size>1&&位置为第一个，item模式变为top
        // 4.其他情况，item模式为middle.
        if (mGroupChildArray.size() > groupPosition && mGroupChildArray.get(groupPosition).size() == 1) {
            itemView.setType(HomeExpandableListItemView.ITEM_TYPE.SINGLE);
        } else if (mGroupChildArray.size() > groupPosition
                && childPosition == mGroupChildArray.get(groupPosition).size() - 1) {
            itemView.setType(HomeExpandableListItemView.ITEM_TYPE.BOTTOM);
        } else if (childPosition == 0) {
            itemView.setType(HomeExpandableListItemView.ITEM_TYPE.TOP);
        } else {
            itemView.setType(HomeExpandableListItemView.ITEM_TYPE.MIDDLE);
        }

        itemView.showRedPoint(shouldDisplayRedPoint(item.getId(), item.isShowRedPoint()));
        itemView.setLeftText(mContext.getString(item.getLeftText()));

        itemView.setRightText(item.isShowRightText() ? View.VISIBLE : View.GONE, item
                .getRightText());
        itemView.setSpecialRightText(item.isShowSpecialRightText() ? View.VISIBLE : View.GONE,
                item.getSpecialRightText());

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

    private boolean shouldDisplayRedPoint(int userCenterItemId, boolean isShouldShowRedPoint) {
        switch (userCenterItemId) {
            case SettingCenterConstants.USER_OFFLINE:
                return false;
            case SettingCenterConstants.USER_FEEDBACK:
                return isShouldShowRedPoint;
            case SettingCenterConstants.USER_ACTIVITY:
                return false;
            default:
                return false;
        }
    }

}
