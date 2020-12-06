package com.baidu.demo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.baidu.hicar.IRemoteService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private IRemoteService myBinder;
    private MyServiceConnection myServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_aidl = findViewById(R.id.btn_aidl);
        btn_aidl.setOnClickListener(this);

        binderService();
    }

    private void binderService() {
        // 开启远程服务
        Intent intent = new Intent();
        intent.setClassName("com.baidu.hicar",
                "com.baidu.hicar.RemoteService");

        myServiceConnection = new MyServiceConnection();
        bindService(intent, myServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_aidl) {
            try {
                myBinder.remoteServiceMethod();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建服务连接对象，用于接收中间人
     */
    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = IRemoteService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    protected void onDestroy() {
        unbindService(myServiceConnection);
        super.onDestroy();
    }

}