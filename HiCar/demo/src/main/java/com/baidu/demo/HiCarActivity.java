package com.baidu.demo;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HiCarActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hicar);
        Button registerCard = findViewById(R.id.btn_registerCard);
        Button updateCard = findViewById(R.id.btn_updateCard);
        Button removeCard = findViewById(R.id.btn_removeCard);
        registerCard.setOnClickListener(this);
        updateCard.setOnClickListener(this);
        removeCard.setOnClickListener(this);

        HiCarServiceManage.getInstance(getApplicationContext()).bindRemoteCardService();
        registerBroadCastReceiver();
    }

    // 广播权限
    private final static String HICAR_PERMISSION = "com.huawei.hicar.HICAR_PERMISSION";
    // 动态注册广播
    private void registerBroadCastReceiver() {
        HiCarBroadCastReceiver hiCarBroadCastReceiver = new HiCarBroadCastReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.huawei.hicar.ACTION_HICAR_STARTED");
        intentFilter.addAction("com.huawei.hicar.ACTION_HICAR_STOPPED");
        intentFilter.addAction("com.huawei.hicar.ACTION_CARD_UPDATE");
        intentFilter.addAction("com.huawei.hicar.ACTION_CARD_REMOVE");
        intentFilter.addAction("com.huawei.hicar.ACTION_CARD_PAUSE");
        intentFilter.addAction("com.huawei.hicar.ACTION_CARD_RESUME");

        registerReceiver(hiCarBroadCastReceiver, intentFilter, HICAR_PERMISSION, null);
        Log.e("lxl", "广播动态注册完成");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        HiCarServiceManage.getInstance(getApplicationContext()).unbindRemoteCardService();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_registerCard:
                HiCarServiceManage.getInstance(getApplicationContext()).registerCard();
                break;
            case R.id.btn_updateCard:
                HiCarServiceManage.getInstance(getApplicationContext()).updateCard();
                break;
            case R.id.btn_removeCard:
                HiCarServiceManage.getInstance(getApplicationContext()).removeCard();
                break;
        }
    }


}
