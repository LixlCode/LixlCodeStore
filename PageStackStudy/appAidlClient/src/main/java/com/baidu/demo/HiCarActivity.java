package com.baidu.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
