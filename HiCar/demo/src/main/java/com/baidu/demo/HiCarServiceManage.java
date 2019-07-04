package com.baidu.demo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.RemoteViews;

import com.huawei.hicar.IRemoteCardService;

public class HiCarServiceManage {

    private final static String HI_CAR_PACKAGE = "com.huawei.hicar";
    private final static String ACTION_CONNECT = "com.huawei.hicar.ACTION_CONNECT";

    private IRemoteCardService hiCarBinder;
    private int cardId; // 地图在HiCar中的唯一标识

    private Context mContext;

    private volatile static HiCarServiceManage mInstance = null;

    private HiCarServiceManage(Context context) {
        this.mContext = context;
    }

    public static HiCarServiceManage getInstance(Context context) {
        if (mInstance == null) {
            synchronized (HiCarServiceManage.class) {
                if (mInstance == null) {
                    mInstance = new HiCarServiceManage(context);
                }
            }
        }
        return mInstance;
    }

    public void bindRemoteCardService() {
        Intent intent = new Intent();
        intent.setAction(ACTION_CONNECT);
        intent.setPackage(HI_CAR_PACKAGE);
        mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.e("lxl", "绑定远程服务");
    }

    public void unbindRemoteCardService() {
        mContext.unbindService(serviceConnection);
        Log.e("lxl", "解绑远程服务");
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            hiCarBinder = IRemoteCardService.Stub.asInterface(service);
            Log.e("lxl", "远程服务联接=="+hiCarBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void removeCard() {
        Log.e("lxl", "removeCard");
        try {
            hiCarBinder.removeCard(cardId);
            Log.e("lxl", "删除卡片");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void updateCard( ) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.hicar_remote_view_card);
        remoteViews.setTextViewText(R.id.tv_navi, "导航概况");

        Bundle bundle = new Bundle();
        bundle.putInt("cardType", 1); // 1代表是ongoing卡片
        bundle.putInt("priority", 0); // 优先级最低
        try {
            hiCarBinder.updateCard(cardId, remoteViews, bundle);
            Log.e("lxl", "更新卡片");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void registerCard() {
        Bundle bundle = new Bundle();
        bundle.putInt("cardType", 1); // 1代表是ongoing卡片
        bundle.putInt("priority", 0); // 优先级最低
        try {
            // 包名："com.baidu.demo"
            cardId = hiCarBinder.registerCard(mContext.getPackageName(), bundle);
            Log.e("lxl", "注册卡片=="+cardId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


}
