package com.wecome.demo.learn.observe;

import android.util.Log;

/**
 * 定义一个具体的观察者「普通的微信用户」
 */
public class ReaderObserver implements IObserver {

    // 微信用户姓名
    private String name;

    public ReaderObserver(String name) {
        this.name = name;
     }

    @Override
    public void reciveContent(String info) {
        Log.d("lxl", name + "注意，Personal发布了文章---" +info);
    }

    public String getName() {
        return this.name;
    }

}
