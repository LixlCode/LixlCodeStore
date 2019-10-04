package com.baidu.hicar;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class RemoteService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    // 提供中间人
    IRemoteService.Stub myBinder = new IRemoteService.Stub() {

        @Override
        public void remoteServiceMethod() throws RemoteException {
            remoteMethod();
        }
    };

    // 暴露给远程调用的方法
    Handler handler = new Handler();
    public void remoteMethod() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),
                        "Hello aidl，远程应用的方法执行了", Toast.LENGTH_LONG).show();
            }
        });
    }


}
