package com.baidu.demo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

public class HiCarLauncherContentProvider extends ContentProvider {
    
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@Nullable Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@Nullable Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@Nullable Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@Nullable Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@Nullable Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public Bundle call(@Nullable String method, @Nullable String arg, @Nullable Bundle extras) {

        Log.e("lxl", "HiCar调用了call()方法====lxl");

        if ("HicarStarted".equals(method)) {
            // 注册卡片
            Log.e("lxl", "HiCar调用了call()方法");
            HiCarServiceManage.getInstance(getContext()).registerCard();
        }
        return null;
    }

}
