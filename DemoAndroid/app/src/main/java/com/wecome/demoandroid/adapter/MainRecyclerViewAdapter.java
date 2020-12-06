package com.wecome.demoandroid.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wecome.demoandroid.R;
import com.wecome.demoandroid.demoutils.response.MainInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 * 首页RecyclerView的适配器
 */

public class MainRecyclerViewAdapter extends BaseMultiItemQuickAdapter<MainInfoBean,BaseViewHolder> {

    public MainRecyclerViewAdapter(List<MainInfoBean> data) {
        super(data);
        addItemType(MainInfoBean.TYPE_ITEM_NO_IMAGE, R.layout.main_fragment_item_no_img);
        addItemType(MainInfoBean.TYPE_ITEM_ONE_IMAGE, R.layout.main_fragment_item_one_img);
        addItemType(MainInfoBean.TYPE_ITEM_THREE_IMAGE, R.layout.main_fragment_item_three_img);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MainInfoBean mainInfoBean) {
        switch (mainInfoBean.getItemType()) {
            case MainInfoBean.TYPE_ITEM_NO_IMAGE:
                baseViewHolder.setImageResource(R.id.iv_main_fragment_one_image,
                        R.mipmap.ic_launcher_round)
                        .setText(R.id.tv_main_fragment_one_name, mainInfoBean.title)
                        .setText(R.id.tv_main_fragment_one_desc, mainInfoBean.des.get(0))
                        .addOnClickListener(R.id.btn_down);
                break;
            case MainInfoBean.TYPE_ITEM_ONE_IMAGE:
                baseViewHolder.setImageResource(R.id.iv_one_img,R.mipmap.head)
                        .setText(R.id.tv_one_img,mainInfoBean.des.get(0));
                break;
            case MainInfoBean.TYPE_ITEM_THREE_IMAGE:
                baseViewHolder.setImageResource(R.id.iv_three_img_1, R.mipmap.three)
                        .setImageResource(R.id.iv_three_img_2, R.mipmap.three2)
                        .setImageResource(R.id.iv_three_img_3, R.mipmap.three3)
                        .setText(R.id.tv_three_img_1,mainInfoBean.des.get(0))
                        .setText(R.id.tv_three_img_2,mainInfoBean.des.get(0))
                        .setText(R.id.tv_three_img_3,mainInfoBean.des.get(0));
                break;
        }
    }

}
