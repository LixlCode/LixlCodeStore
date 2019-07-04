package com.store.stack.other.mode;

import android.util.Log;

public class XiaoMing {

    private ICallback iCallback;

    // 小明实际做的事
    void work(ICallback iCallback, String str) {
        this.iCallback = iCallback;

        Log.e("lxl", "XiaoMing--work--" + str);

        Log.i("lxl", "小明说：「收到，我马上去」，过了半小时");
    }

    // 小明把事干完了，给妈妈一个反馈
    public void workDown(String str) {
        if (iCallback != null) {
            iCallback.setResult(str);
        }
    }

}
