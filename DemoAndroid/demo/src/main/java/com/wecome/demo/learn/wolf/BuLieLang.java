package com.wecome.demo.learn.wolf;

import android.util.Log;

/**
 * 捕猎狼，另一个观察者
 */
public class BuLieLang extends NormalWolf {

    public BuLieLang(IWolf iWolf) {
        this.iWolf = iWolf;
        this.iWolf.attchObserver(this);
    }


    @Override
    public void reciveCommand(String string) {
        Log.i("lxl", "捕猎狼：狼王命令：" + string);
    }

}
