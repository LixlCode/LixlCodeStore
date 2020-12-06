/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package android.support.v4.app;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.baidu.mapframework.app.fpstack.TaskManagerFactory;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * SandboxFragment
 *
 * @author liguoqing
 * @version 1.0 10/27/15
 */
public class SandboxActivity extends FragmentActivity {

    private AssetManager    sandbox_am;
    private Resources       sandbox_res;
    private Resources.Theme sandbox_theme;
    private ClassLoader     sandbox_cl;
    private boolean initSuccess = false;

    private String comId;
    private String comPackageName;

    private FragmentHostCallback sandbox_hostCallback;

    public SandboxActivity(AssetManager am, String comId, String packageName, ClassLoader classLoader)
            throws RuntimeException {
        this.comId = comId;
        this.comPackageName = packageName;
        this.sandbox_am = am;
        this.sandbox_cl = classLoader;

        if (getOriginalActivity() == null || am == null || getOriginalActivity().getResources() == null) {
            throw new RuntimeException("Sandbox activity needs a valid activity context");
        }

        this.sandbox_res = new Resources(am, getOriginalActivity().getResources().getDisplayMetrics(),
                getOriginalActivity().getResources().getConfiguration());

        if (sandbox_res == null) {
            throw new RuntimeException("Sandbox activity creates resources failed");
        }

        // 替换ContextWrapper底层的ContextImpl对象来解决特定rom改写ContextWrapper接口导致的空指针异常
        if (getBaseContext() == null) {
            attachBaseContext(getOriginalActivity().getBaseContext());
        }

        this.sandbox_theme = getResources().newTheme();
        this.sandbox_theme.setTo(this.getTheme());
        int trickThemeId = getResources().getIdentifier("AppTheme", "style", packageName);
        if (trickThemeId != 0) {
            this.sandbox_theme.applyStyle(trickThemeId, true);
        }

        initSuccess = true;
        sandbox_hostCallback = new SandboxHostCallbacks();
        modifyFragmentController(FragmentController.createController(sandbox_hostCallback));
    }

    /**
     * 适配 support v4 r23 实现
     */
    private void modifyFragmentController(FragmentController controller) {
        Field fragmentsField = null;
        try {
            fragmentsField = FragmentActivity.class.getDeclaredField("mFragments");
            fragmentsField.setAccessible(true);
            fragmentsField.set(this, controller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private FragmentActivity getOriginalActivity() {
        return (FragmentActivity) TaskManagerFactory.getTaskManager().getContainerActivity();
    }

    public boolean initSuccess() {
        return initSuccess;
    }

    public String getComId() {
        return this.comId;
    }

    public String getComPackageName() {
        return this.comPackageName;
    }

    public FragmentHostCallback getSandboxHostCallback() {
        return sandbox_hostCallback;
    }

    @Override
    public Window getWindow() {
        return getOriginalActivity().getWindow();
    }

    @Override
    public WindowManager getWindowManager() {
        return getOriginalActivity().getWindowManager();
    }

    public FragmentActivity getSandboxActivity() {
        return getOriginalActivity();
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        getOriginalActivity().setRequestedOrientation(requestedOrientation);
    }

    @Override
    public boolean isFinishing() {

        return getOriginalActivity().isFinishing();
    }

    @Override
    public AssetManager getAssets() {

        if (this.sandbox_am != null) {
            return this.sandbox_am;
        }
        return getOriginalActivity().getAssets();
    }

    @Override
    public Resources getResources() {
        if (this.sandbox_res != null) {
            return this.sandbox_res;
        }
        return getOriginalActivity().getResources();
    }

    /**
     * begin override activity method
     **/

    @Override
    public LayoutInflater getLayoutInflater() {

        return getOriginalActivity().getLayoutInflater();
    }

    boolean fmHacked = false;

    @Override
    public FragmentManager getSupportFragmentManager() {
        return super.getSupportFragmentManager();
    }


    private void hackFragmentManagerImpl() {
        try {
            Field fmImpl = FragmentActivity.class.getDeclaredField("mFragments");
            fmImpl.setAccessible(true);
            Method attachActivityMethod =
                    Class.forName("android.support.v4.app.FragmentManagerImpl").getDeclaredMethod("attachActivity",
                            FragmentActivity.class,
                            getClassLoader().loadClass("android.support.v4.app.FragmentContainer"),
                            android.support.v4.app.Fragment.class);

            Field fc = FragmentActivity.class.getDeclaredField("mContainer");
            fc.setAccessible(true);

            attachActivityMethod.invoke(fmImpl.get(this), this, fc.get(this), null);

        } catch (NoSuchFieldException e) {

        } catch (NoSuchMethodException e) {

        } catch (ClassNotFoundException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        } catch (InvocationTargetException e) {

        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    /**
     * end override activity method
     **/

    @Override
    public PackageManager getPackageManager() {

        return getOriginalActivity().getPackageManager();
    }

    @Override
    public ContentResolver getContentResolver() {

        return getOriginalActivity().getContentResolver();
    }

    @Override
    public Looper getMainLooper() {

        return getOriginalActivity().getMainLooper();
    }

    @Override
    public Context getApplicationContext() {

        return getOriginalActivity().getApplicationContext();
    }

    @Override
    public Resources.Theme getTheme() {

        if (this.sandbox_theme != null) {
            return this.sandbox_theme;
        }
        return getOriginalActivity().getTheme();
    }

    @Override
    public ClassLoader getClassLoader() {

        if (this.sandbox_cl != null) {
            return this.sandbox_cl;
        }
        return getOriginalActivity().getClassLoader();
    }

    @Override
    public String getPackageName() {
        return getOriginalActivity().getPackageName();
    }

    @Override
    public ApplicationInfo getApplicationInfo() {

        return getOriginalActivity().getApplicationInfo();
    }

    @Override
    public String getPackageResourcePath() {

        return getOriginalActivity().getPackageResourcePath();
    }

    @Override
    public String getPackageCodePath() {

        return getOriginalActivity().getPackageCodePath();
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {

        return getOriginalActivity().getSharedPreferences(name, mode);
    }

    @Override
    public FileInputStream openFileInput(String name) throws FileNotFoundException {

        return getOriginalActivity().openFileInput(name);
    }

    @Override
    public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {

        return getOriginalActivity().openFileOutput(name, mode);
    }

    @Override
    public File getFileStreamPath(String name) {

        return getOriginalActivity().getFileStreamPath(name);
    }

    @Override
    public File getFilesDir() {

        return getOriginalActivity().getFilesDir();
    }

    @Override
    public File getExternalFilesDir(String type) {

        return getOriginalActivity().getExternalFilesDir(type);
    }

    @Override
    public File getObbDir() {

        return getOriginalActivity().getObbDir();
    }

    @Override
    public File getCacheDir() {

        return getOriginalActivity().getCacheDir();
    }

    @Override
    public File getExternalCacheDir() {

        return getOriginalActivity().getExternalCacheDir();
    }

    @Override
    public String[] fileList() {

        return getOriginalActivity().fileList();
    }

    @Override
    public File getDir(String name, int mode) {

        return getOriginalActivity().getDir(name, mode);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {

        return getOriginalActivity().openOrCreateDatabase(name, mode, factory);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory,
            DatabaseErrorHandler errorHandler) {

        return getOriginalActivity().openOrCreateDatabase(name, mode, factory, errorHandler);
    }

    @Override
    public File getDatabasePath(String name) {

        return getOriginalActivity().getDatabasePath(name);
    }

    @Override
    @Deprecated
    public Drawable getWallpaper() {

        return getOriginalActivity().getWallpaper();
    }

    @Override
    @Deprecated
    public Drawable peekWallpaper() {

        return getOriginalActivity().peekWallpaper();
    }

    @Override
    @Deprecated
    public int getWallpaperDesiredMinimumWidth() {

        return getOriginalActivity().getWallpaperDesiredMinimumWidth();
    }

    @Override
    @Deprecated
    public int getWallpaperDesiredMinimumHeight() {

        return getOriginalActivity().getWallpaperDesiredMinimumHeight();
    }

    @Override
    @Deprecated
    public void clearWallpaper() throws IOException {

        getOriginalActivity().clearWallpaper();
    }

    @Override
    public void removeStickyBroadcast(Intent intent) {

        getOriginalActivity().removeStickyBroadcast(intent);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {

        return getOriginalActivity().registerReceiver(receiver, filter);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission,
            Handler scheduler) {

        return getOriginalActivity().registerReceiver(receiver, filter, broadcastPermission, scheduler);
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {

        return getOriginalActivity().bindService(service, conn, flags);
    }

    @Override
    public Object getSystemService(String name) {

        return getOriginalActivity().getSystemService(name);
    }

    @Override
    public int checkPermission(String permission, int pid, int uid) {

        return getOriginalActivity().checkPermission(permission, pid, uid);
    }

    @Override
    public int checkCallingPermission(String permission) {

        return getOriginalActivity().checkCallingPermission(permission);
    }

    @Override
    public int checkCallingOrSelfPermission(String permission) {

        return getOriginalActivity().checkCallingOrSelfPermission(permission);
    }

    @Override
    public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {

        getOriginalActivity().grantUriPermission(toPackage, uri, modeFlags);
    }

    @Override
    public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {

        return getOriginalActivity().checkUriPermission(uri, pid, uid, modeFlags);
    }

    @Override
    public int checkCallingUriPermission(Uri uri, int modeFlags) {

        return getOriginalActivity().checkCallingUriPermission(uri, modeFlags);
    }

    @Override
    public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {

        return getOriginalActivity().checkCallingOrSelfUriPermission(uri, modeFlags);
    }

    @Override
    public int checkUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid,
            int modeFlags) {

        return getOriginalActivity().checkUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags);
    }

    @Override
    public boolean deleteFile(String name) {

        return getOriginalActivity().deleteFile(name);
    }

    @Override
    public boolean deleteDatabase(String name) {

        return getOriginalActivity().deleteDatabase(name);
    }

    @Override
    public String[] databaseList() {

        return getOriginalActivity().databaseList();
    }

    @Override
    public void enforcePermission(String permission, int pid, int uid, String message) {

        getOriginalActivity().enforcePermission(permission, pid, uid, message);
    }

    @Override
    public void enforceCallingPermission(String permission, String message) {

        getOriginalActivity().enforceCallingPermission(permission, message);
    }

    @Override
    public void enforceCallingOrSelfPermission(String permission, String message) {

        getOriginalActivity().enforceCallingOrSelfPermission(permission, message);
    }

    @Override
    public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) {

        getOriginalActivity().enforceUriPermission(uri, pid, uid, modeFlags, message);
    }

    @Override
    public void enforceCallingUriPermission(Uri uri, int modeFlags, String message) {

        getOriginalActivity().enforceCallingUriPermission(uri, modeFlags, message);
    }

    @Override
    public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) {

        getOriginalActivity().enforceCallingOrSelfUriPermission(uri, modeFlags, message);
    }

    @Override
    public void enforceUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid,
            int modeFlags, String message) {

        getOriginalActivity().enforceUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags, message);
    }

    @Override
    public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {

        return getOriginalActivity().createPackageContext(packageName, flags);
    }

    @Override
    public void sendBroadcast(Intent intent) {

        getOriginalActivity().sendBroadcast(intent);
    }

    @Override
    public void sendBroadcast(Intent intent, String receiverPermission) {

        getOriginalActivity().sendBroadcast(intent, receiverPermission);
    }

    @Override
    public void revokeUriPermission(Uri uri, int modeFlags) {

        getOriginalActivity().revokeUriPermission(uri, modeFlags);
    }

    @Override
    public void sendOrderedBroadcast(Intent intent, String receiverPermission) {

        getOriginalActivity().sendBroadcast(intent, receiverPermission);
    }

    @Override
    public void sendOrderedBroadcast(Intent intent, String receiverPermission, BroadcastReceiver resultReceiver,
            Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

        getOriginalActivity().sendOrderedBroadcast(intent, receiverPermission, resultReceiver, scheduler, initialCode,
                initialData, initialExtras);
    }

    @Override
    public void sendStickyBroadcast(Intent intent) {

        getOriginalActivity().sendStickyBroadcast(intent);
    }

    @Override
    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, Handler scheduler,
            int initialCode, String initialData, Bundle initialExtras) {

        getOriginalActivity().sendStickyOrderedBroadcast(intent, resultReceiver, scheduler, initialCode, initialData,
                initialExtras);
    }

    @Override
    public void setTheme(int resid) {

        getOriginalActivity().setTheme(resid);
    }

    @Override
    @Deprecated
    public void setWallpaper(Bitmap bitmap) throws IOException {

        getOriginalActivity().setWallpaper(bitmap);
    }

    @Override
    @Deprecated
    public void setWallpaper(InputStream data) throws IOException {

        getOriginalActivity().setWallpaper(data);
    }

    @Override
    public void startActivities(Intent[] intents) {

        getOriginalActivity().startActivities(intents);
    }

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        getOriginalActivity().startActivityFromFragment(fragment, intent, requestCode);
    }


    @Override
    public void startActivity(Intent intent) {

        getOriginalActivity().startActivity(intent);
    }

    @Override
    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues,
            int extraFlags) throws IntentSender.SendIntentException {

        getOriginalActivity().startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags);
    }

    @Override
    public boolean startInstrumentation(ComponentName className, String profileFile, Bundle arguments) {

        return getOriginalActivity().startInstrumentation(className, profileFile, arguments);
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {

        getOriginalActivity().unregisterReceiver(receiver);
    }

    @Override
    public ComponentName startService(Intent service) {

        return getOriginalActivity().startService(service);
    }

    @Override
    public boolean stopService(Intent service) {

        return getOriginalActivity().stopService(service);
    }

    @Override
    public void unbindService(ServiceConnection conn) {

        getOriginalActivity().unbindService(conn);
    }

    public void requestPermissionsFromFragment(Fragment fragment, String[] permissions, int requestCode) {
        if (requestCode == -1) {
            ActivityCompat.requestPermissions(getOriginalActivity(), permissions, requestCode);
        } else if ((requestCode & -256) != 0) {
            throw new IllegalArgumentException("Can only use lower 8 bits for requestCode");
        } else {
            this.mRequestedPermissionsFromFragment = true;
            ActivityCompat.requestPermissions(getOriginalActivity(), permissions,
                    (fragment.mIndex + 1 << 8) + (requestCode & 255));
        }
    }


    class SandboxHostCallbacks extends FragmentHostCallback<FragmentActivity> {
        public SandboxHostCallbacks() {
            super(SandboxActivity.this);
        }

        public void onDump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            getOriginalActivity().dump(prefix, fd, writer, args);
        }

        public boolean onShouldSaveFragmentState(Fragment fragment) {
            return !getOriginalActivity().isFinishing();
        }

        public LayoutInflater onGetLayoutInflater() {
            return LayoutInflater.from(getActivity()).cloneInContext(getActivity());
        }

        public FragmentActivity onGetHost() {
            return SandboxActivity.this;
        }

        public void onSupportInvalidateOptionsMenu() {
            getOriginalActivity().supportInvalidateOptionsMenu();
        }

        public void onStartActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
            getOriginalActivity().startActivityFromFragment(fragment, intent, requestCode);
        }

        public void onStartActivityFromFragment(Fragment fragment, Intent intent, int requestCode,
                                                @Nullable Bundle options) {
            getOriginalActivity().startActivityFromFragment(fragment, intent, requestCode);
        }

        public void onRequestPermissionsFromFragment(@NonNull Fragment fragment, @NonNull String[] permissions,
                                                     int requestCode) {
            SandboxActivity.this.requestPermissionsFromFragment(fragment, permissions, requestCode);
        }

        public boolean onShouldShowRequestPermissionRationale(@NonNull String permission) {
            return ActivityCompat.shouldShowRequestPermissionRationale(getOriginalActivity(), permission);
        }

        public boolean onHasWindowAnimations() {
            return getOriginalActivity().getWindow() != null;
        }

        public int onGetWindowAnimations() {
            Window w = getOriginalActivity().getWindow();
            return w == null ? 0 : w.getAttributes().windowAnimations;
        }

        public void onAttachFragment(Fragment fragment) {
            getOriginalActivity().onAttachFragment(fragment);
        }

        @Nullable
        public View onFindViewById(int id) {
            return getOriginalActivity().findViewById(id);
        }

        public boolean onHasView() {
            Window w = getOriginalActivity().getWindow();
            return w != null && w.peekDecorView() != null;
        }
    }

}
