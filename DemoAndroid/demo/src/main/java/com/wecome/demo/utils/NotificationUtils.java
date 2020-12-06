package com.wecome.demo.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.wecome.demo.R;

/**
 * 【适配Android O之适配Notification】
 * 将targetSdkVersion提到26以上的话，就必须为Notification设置channel了，不能为null。
 * Notification渠道号的使用，适配Android O的工具类
 */
public class NotificationUtils extends ContextWrapper {

    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";

    public NotificationUtils(Context context) {
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content) {
        return new Notification.Builder(getApplicationContext(), id)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);
    }

    public NotificationCompat.Builder getNotification_25(String title, String content) {
        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);
    }

    public void sendNotification(String title, String content) {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Notification notification = getChannelNotification
                    (title, content).build();
            getManager().notify(1, notification);
        } else {
            Notification notification = getNotification_25(title, content).build();
            getManager().notify(1, notification);
        }
    }




    /**
     * 创建快捷方式，兼容Android O及以上：
     */
    private void addShortcut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ShortcutManager scm = (ShortcutManager) getSystemService(SHORTCUT_SERVICE);
            Intent launcherIntent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);//设置网络页面intent
            ShortcutInfo si = new ShortcutInfo.Builder(this, "dataroam")
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_launcher_foreground))
                    .setShortLabel("网络设置")
                    .setIntent(launcherIntent)
                    .build();
            assert scm != null;
            scm.requestPinShortcut(si, null);
        } else {
            Intent addShortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");//"com.android.launcher.action.INSTALL_SHORTCUT"
            // 不允许重复创建
            addShortcutIntent.putExtra("duplicate", false);// 经测试不是根据快捷方式的名字判断重复的
            // 应该是根据快链的Intent来判断是否重复的,即Intent.EXTRA_SHORTCUT_INTENT字段的value
            // 但是名称不同时，虽然有的手机系统会显示Toast提示重复，仍然会建立快链
            // 屏幕上没有空间时会提示
            // 注意：重复创建的行为MIUI和三星手机上不太一样，小米上似乎不能重复创建快捷方式

            // 名字
            addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "网络设置");
            // 图标
            addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher_foreground));

            // 设置关联程序
            Intent launcherIntent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);//设置网络页面intent
            // 设置关联程序
            //  Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
            //  launcherIntent.setClass(CoolActivity.this, CoolActivity.class);
            //  launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

            // 发送广播
            sendBroadcast(addShortcutIntent);
        }
    }

}


//	用法：
//     NotificationUtils notificationUtils = new NotificationUtils(this);
//     notificationUtils.sendNotification("测试标题","测试内容");


