package com.baidu.hicar;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class BRemoteService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }


    // 创建中间人对象，用来暴露remoteMethod()方法
    class MyBinder extends IService.Stub {

        @Override
        public void callRemoteMethod() throws RemoteException {
            // 调用暴露出来的方法
            remoteMethod();
        }
    }


    // 暴露远程服务中供其他app调用的方法
    Handler handler = new Handler();
    public void remoteMethod(){

        handler.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程名："+Thread.currentThread().getName());

                //toast属于更新UI，必须放到主线程中
                Toast.makeText(getApplicationContext(),
                        "远程应用的方法执行了", Toast.LENGTH_LONG).show();
            }
        });
    }

}
