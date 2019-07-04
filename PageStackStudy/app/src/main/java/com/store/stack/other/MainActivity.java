package com.store.stack.other;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.store.stack.other.mode.MomCallbackImpl;
import com.store.stack.other.mode.XiaoMing;
import com.store.stack.other.observe.PersonalSubject;
import com.store.stack.other.observe.ReaderObserver;
import com.store.stack.other.wolf.BuLieLang;
import com.store.stack.other.wolf.LangWang;
import com.store.stack.other.wolf.ZhenchaLang;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 测试回到接口
        findViewById(R.id.tv_test_icallback).setOnClickListener(new View.OnClickListener() {
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
        findViewById(R.id.tv_test_observer).setOnClickListener(new View.OnClickListener() {
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
                    Log.e("lxl", zhaoliu.getName() + "你好！你还没有关注personal，请关注先，谢谢");
                }
            }
        });


        // 观察者模式，再举一个例子----狼王开会
        findViewById(R.id.tv_test_wolf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取狼王的单例
                LangWang langWang = LangWang.getInstance();

                ZhenchaLang zhenchaLang = new ZhenchaLang(langWang);
                BuLieLang buLieLang = new BuLieLang(langWang);

                // 狼王下达命令，就是发送通知
                langWang.underCommand("1、分工合作，捕猎狼根据侦查狼反馈看机行事；2、侦查狼永远把危险放在第一位，遇到危险第一时间提醒大家撤退");
            }
        });

    }

}
