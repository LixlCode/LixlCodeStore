package com.wecome.demo.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * wifi工具类
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
 * <p>
 * Created by xupenghui on 2018/2/26.
 */

public class WifiUtils {

    private static final String TAG = "WifiUtils";

    /**
     * 打开WIFI
     *
     * @param context
     *
     * @return
     */
    public static boolean openWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {

            // Android Q对wifi限制的适配
            if (Build.VERSION.SDK_INT > 28) {
//                adapterAndroidQWifiLimitDialog(context, context.getResources().getString(R.string.wifi_limit_hint_1));
                if (wifiManager.isWifiEnabled()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return wifiManager.setWifiEnabled(true);
            }
        } else {
            return false;
        }

    }

//    private static void adapterAndroidQWifiLimitDialog(final Context context, String message) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        View inflate = LayoutInflater.from(context).inflate(R.layout.wifi_limit_dialog, null);
//        builder.setView(inflate);
//        builder.show();
//        TextView tv_message = inflate.findViewById(R.id.tv_message);
//        tv_message.setText(message);
//        inflate.findViewById(R.id.tv_open_wifi).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//            }
//        });
//    }

    /**
     * 关闭wifi
     *
     * @param context
     *
     * @return
     */
    public static boolean closeWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {

            // Android Q对wifi限制的适配
            if (/*Build.VERSION.SDK_INT > 28*/true) {
//                adapterAndroidQWifiLimitDialog(context, context.getResources().getString(R.string.wifi_limit_hint_2));
                if (!wifiManager.isWifiEnabled()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return wifiManager.setWifiEnabled(false);
            }
        } else {
            return false;
        }

    }

    /**
     * 获取WIFI状态
     *
     * @param context
     *
     * @return
     */
    public static int getWifiState(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            return wifiManager.getWifiState();
        } else {
            return WifiManager.WIFI_STATE_UNKNOWN;
        }
    }

    /**
     * WIFI是否打开状态
     *
     * @param context
     *
     * @return
     */
    public static boolean isWifiOpened(Context context) {
        int state = getWifiState(context);
        if (state == WifiManager.WIFI_STATE_ENABLED
                || state == WifiManager.WIFI_STATE_ENABLING) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Wifi是否连接
     *
     * @param context
     *
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            try {
                NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                return info != null ? info.isConnected() : false;
            } catch (Throwable e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Wifi列表是否为空
     *
     * @param context
     *
     * @return
     */
    public static boolean isWiFiListEmpty(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            List list = wifiManager.getScanResults();
            return list == null ? true : list.isEmpty();
        } else {
            return true;
        }
    }

}