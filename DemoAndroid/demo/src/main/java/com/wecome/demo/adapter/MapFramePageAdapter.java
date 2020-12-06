package com.wecome.demo.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wecome.demo.R;
import com.wecome.demo.model.ItemDataBean;

import java.util.List;

public class MapFramePageAdapter extends BaseQuickAdapter<ItemDataBean, BaseViewHolder> {

    public MapFramePageAdapter(int layoutResId, @Nullable List<ItemDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ItemDataBean item) {
        helper.setText(R.id.tv_item, item.getMsg());
    }

}
