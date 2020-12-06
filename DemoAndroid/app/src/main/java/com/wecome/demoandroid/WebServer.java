package com.wecome.demoandroid;

import com.wecome.demoandroid.demoutils.response.MainInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/15.
 * 模拟服务器，提供数据
 */

public class WebServer {

    /**
     * 返回多种类型的数据
     * @return
     */
    public static List<MainInfoBean> getMultiNewsList() {
        List<MainInfoBean> data = new ArrayList<>();

        for (int i=0; i < 4; i++){
            MainInfoBean data1 = new MainInfoBean(1);
            data1.title = "标题"+i;
            data1.des.add("描述描述描述描述描述描述");
            data.add(data1);
        }

        MainInfoBean data2 = new MainInfoBean(2);
        data2.title = "标题";
        data2.des.add("描述描述描述描述描述描述");
        data.add(data2);

        for (int i=0; i < 6; i++){
            MainInfoBean data1 = new MainInfoBean(1);
            data1.title = "标题"+i;
            data1.des.add("描述描述描述描述描述描述");
            data.add(data1);
        }

        MainInfoBean data3 = new MainInfoBean(3);
        data3.title = "标题";
        data3.des.add("好图片");
        data.add(data3);

        return data;
    }

}
