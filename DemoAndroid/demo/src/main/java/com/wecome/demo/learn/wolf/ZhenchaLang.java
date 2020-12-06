package com.wecome.demo.learn.wolf;

import android.util.Log;

/**
 * 侦查狼，另一个观察者
 */
public class ZhenchaLang extends NormalWolf {

    public ZhenchaLang(IWolf iWolf) {
        this.iWolf = iWolf;
        this.iWolf.attchObserver(this);
    }

    @Override
    public void reciveCommand(String string) {
        Log.i("lxl", "侦查狼：狼王命令：" + string);
    }

}
