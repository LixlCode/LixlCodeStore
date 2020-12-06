package com.wecome.demo.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wecome.demo.R;
import com.wecome.demo.model.SearchModel;

import java.util.ArrayList;

public class SearchAdapter extends BaseQuickAdapter<SearchModel, BaseViewHolder> {

    public SearchAdapter(@Nullable ArrayList<SearchModel> data) {
        super(R.layout.search_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchModel item) {
        helper.setText(R.id.tv_item, item.key);
    }
}
