package com.baidu.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HiCarBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String started = intent.getStringExtra("ACTION_HICAR_STARTED");
        String stopped = intent.getStringExtra("ACTION_HICAR_STOPPED");
        String update = intent.getStringExtra("ACTION_CARD_UPDATE");
        String remove = intent.getStringExtra("ACTION_CARD_REMOVE");
        String pause = intent.getStringExtra("ACTION_CARD_PAUSE");
        String resume = intent.getStringExtra("ACTION_CARD_RESUME");

        Log.e("lxl", "接受到HiCar广播====="+started
                                            +",,====="+stopped
                                            +",,===="+update
                                            +",, ===="+remove
                                            +",,===="+pause
                                            +",,====="+resume
                                            +",,-----action===="+action );

        if ("com.huawei.hicar.ACTION_HICAR_STARTED".equals(action)) {
            HiCarServiceManage.getInstance(context).registerCard();
            HiCarServiceManage.getInstance(context).updateCard();
        } else if ("com.huawei.hicar.ACTION_HICAR_STOPPED".equals(action)) {
            HiCarServiceManage.getInstance(context).removeCard();
        }

    }

}
