package com.wecome.demoandroid.demoutils.response;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 * 存储要展示的数据---javaBean
 */

public class MainInfoBean implements MultiItemEntity{

    //定义条目类型
    public static final int TYPE_ITEM_NO_IMAGE = 1;
    public static final int TYPE_ITEM_ONE_IMAGE = 2;
    public static final int TYPE_ITEM_THREE_IMAGE = 3;
    private int itemType;

    public MainInfoBean(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    //要显示的数据
    public String title;
    public List<String> des = new ArrayList<>();

}
