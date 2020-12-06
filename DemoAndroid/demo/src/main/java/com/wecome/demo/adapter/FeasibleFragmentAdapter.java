package com.wecome.demo.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wecome.demo.R;
import com.wecome.demo.model.FeasibleFragmentModel;

import java.util.List;

public class FeasibleFragmentAdapter extends BaseQuickAdapter<FeasibleFragmentModel
        .ResultsBean, BaseViewHolder>{

    private Activity mContext;

    public FeasibleFragmentAdapter(@Nullable List<FeasibleFragmentModel.ResultsBean> data) {
        super(data);
    }

    public FeasibleFragmentAdapter(int layoutResId) {
        super(layoutResId);
    }

    public FeasibleFragmentAdapter(Activity mContext, int layoutResId,
                                   @Nullable List<FeasibleFragmentModel.ResultsBean> data) {
        super(layoutResId, data);
        this.mContext = mContext;
        Log.e("lxl", "data=="+data.get(1).getWho());
    }

    @Override
    protected void convert(BaseViewHolder helper, FeasibleFragmentModel.ResultsBean item) {
        Log.e("lxl", "data=="+item.getWho());
        helper.setText(R.id.tv_des, item.getType()+ "--表示图片提供的意义和目的,希望大家喜欢")
                .setText(R.id.tv_name, item.getWho())
                .setText(R.id.tv_time, item.getDesc());
        Glide.with(mContext).load(item.getUrl()).into((ImageView) helper.getView(R.id.iv_im));
    }

}
