package com.baidu.observer.mode;

import android.util.Log;

public class MomCallbackImpl implements ICallback {

    // 小明去干事
    public void say(XiaoMing xiaoMing, String str) {
        xiaoMing.work(this, str);
    }

    // 收到小明的反馈
    @Override
    public void setResult(String str) {
        Log.i("lxl", "MomCallbackImpl--setResult--" + str);

        eat();
    }

    private void eat() {
        Log.e("lxl", "妈妈说：好的，知道了，洗洗手吃饭吧");
    }

}
