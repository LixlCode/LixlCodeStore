package com.baidu.demo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.hicar.IRemoteService;
import com.baidu.hicar.IService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private IRemoteService myBinder;
    private MyServiceConnection myServiceConnection;
    private MySecondServiceConnection mySecondServiceConnection;
    private IService iService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_aidl = findViewById(R.id.btn_aidl);
        Button btn_aidl_2 = findViewById(R.id.btn_aidl_2);
        Button btn_aidl_3 = findViewById(R.id.btn_aidl_3);
        btn_aidl.setOnClickListener(this);
        btn_aidl_2.setOnClickListener(this);
        btn_aidl_3.setOnClickListener(this);

        firstService();
        secondService();
    }

    private void secondService() {
        // 开启远程服务
        Intent intent = new Intent();
        intent.setClassName("com.baidu.hicar",
                "com.baidu.hicar.BRemoteService");

        mySecondServiceConnection = new MySecondServiceConnection();
        bindService(intent, mySecondServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void firstService() {
        // 开启远程服务
        Intent intent = new Intent();
        intent.setClassName("com.baidu.hicar",
                "com.baidu.hicar.RemoteService");

        myServiceConnection = new MyServiceConnection();
        bindService(intent, myServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_aidl:
                try {
                    myBinder.remoteServiceMethod();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_aidl_2:
                try {
                    iService.callRemoteMethod();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_aidl_3:
                startActivity(new Intent(this, HiCarActivity.class));
                break;
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

    // 哈哈哈，爽歪歪
    class MySecondServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iService = IService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }


    @Override
    protected void onDestroy() {
        unbindService(myServiceConnection);
        unbindService(mySecondServiceConnection);
        super.onDestroy();
    }

}
