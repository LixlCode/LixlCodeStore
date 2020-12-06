package com.wecome.demo.fragment.page;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapframework.app.fpstack.BasePage;
import com.baidu.mapframework.webview.core.CoreWebView;
import com.wecome.demo.R;
import com.wecome.demo.adapter.MapFramePageAdapter;
import com.wecome.demo.learn.mode.MomCallbackImpl;
import com.wecome.demo.learn.mode.XiaoMing;
import com.wecome.demo.learn.observe.PersonalSubject;
import com.wecome.demo.learn.observe.ReaderObserver;
import com.wecome.demo.learn.wolf.BuLieLang;
import com.wecome.demo.learn.wolf.LangWang;
import com.wecome.demo.learn.wolf.ZhenchaLang;
import com.wecome.demo.model.ItemDataBean;

import java.util.ArrayList;

public class MapFramePage extends BasePage {

    private CoreWebView mCoreWebView;
    private RecyclerView mRecyclerView;
    private ArrayList<ItemDataBean> datas;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_page_mapframe, null);
        initPageUi(view);
        learnDesignModel(view);
        return view;
    }

    private void learnDesignModel(View view) {
        // 测试回到接口
        view.findViewById(R.id.tv_test_icallback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XiaoMing xiaoMing = new XiaoMing();
                MomCallbackImpl momCallback = new MomCallbackImpl();

                momCallback.say(xiaoMing, "妈妈说：叫爸爸回来吃饭");

                // 妈妈说了让小明去叫爸爸，这时候妈妈在等一个小明的反馈，爸爸是回来了还是没有回来
                // 所以就开始回调了
                xiaoMing.workDown("小明说：我爸回来了");
            }
        });

        // 测试观察者模式（微信公号推送文章简单的实例）
        view.findViewById(R.id.tv_test_observer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建作者
                PersonalSubject personalSubject = new PersonalSubject();

                // 创建微信用户
                ReaderObserver zhangsan = new ReaderObserver("张三");
                ReaderObserver lisi = new ReaderObserver("李四");
                ReaderObserver wangwu = new ReaderObserver("王武");
                ReaderObserver zhaoliu = new ReaderObserver("赵六");

                // 微信用户订阅公众号
                personalSubject.attchObserver(zhangsan);
                personalSubject.attchObserver(lisi);
                personalSubject.attchObserver(wangwu);

                // 作者 personal 发布一篇文章
                personalSubject.submitContent("北上广不相信眼泪");

                boolean isAttch = personalSubject.isAttchObserver(zhaoliu);
                if (!isAttch) {
                    Log.e("lxl", zhaoliu.getName()
                            + "你好！你还没有关注personal，请关注先，谢谢");
                }
            }
        });

        // 观察者模式，再举一个例子----狼王开会
        view.findViewById(R.id.tv_test_wolf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取狼王的单例
                LangWang langWang = LangWang.getInstance();

                ZhenchaLang zhenchaLang = new ZhenchaLang(langWang);
                BuLieLang buLieLang = new BuLieLang(langWang);

                // 狼王下达命令，就是发送通知
                langWang.underCommand("1、分工合作，捕猎狼根据侦查狼反馈看机行事；" +
                        "2、侦查狼永远把危险放在第一位，遇到危险第一时间提醒大家撤退");
            }
        });

    }

    private void initPageUi(View view) {
        mCoreWebView = view.findViewById(R.id.cwv_CoreWebView);
        mRecyclerView = view.findViewById(R.id.rlv_recyclerListView);
        initRecyclerViewData();
        initRecyclerView();
    }

    private void initRecyclerViewData() {
        datas = new ArrayList<>();
        ItemDataBean infoItem;
        for (int i = 0; i < 100; i++) {
            infoItem = new ItemDataBean();
            infoItem.setMsg("我是第" + i + "条标题");
            datas.add(infoItem);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        MapFramePageAdapter adapter = new MapFramePageAdapter(R.layout.map_frame_page_item, datas);
        mRecyclerView.setAdapter(adapter);
    }

}
