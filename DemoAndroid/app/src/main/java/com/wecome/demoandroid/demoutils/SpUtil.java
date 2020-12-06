package com.wecome.demoandroid.demoutils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/2/26.
 * SharedPreferences的封装
 *      用来保存应用中各种状态和小数据到本地的私有目录
 */

public class SpUtil {

    //私有目录下的文件名
    public static String FILENAME = "filename";
    //TODO 以下就是用到这个文件的所有的 key
    public static final String IS_APP_FIRST_OPEN = "is_app_first_open";


    /**
     * 将map集合映射添加到fileName的SharedPreferences中
     * @param context
     * @param fileName
     * @param map
     */

    public static void put(Context context, String fileName, Map<String, String> map) {
        if (map != null && map.size() > 0) {
            SharedPreferences preferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            Set<String> keySet = map.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = map.get(key);
                editor.putString(key, value);
            }
            editor.commit();
        }
    }

    /**
     * 将给定字符串添加到fileName的SharedPreferences中
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String fileName, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 将给定布尔值添加到fileName的SharedPreferences中
     * @param context
     * @param fileName
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String fileName, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 将给定长整型数据添加到fileName的SharedPreferences中
     * @param context
     * @param fileName
     * @param key
     * @param value
     */
    public static void putLong(Context context, String fileName, String key, long value) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 将给定整型数据添加到fileName的SharedPreferences中
     * @param context
     * @param fileName
     * @param key
     * @param value
     */
    public static void putInt(Context context, String fileName, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 删除本地文件中存在的注册信息
     * @param context
     * @param fileName
     * @param key
     * @return true--删除成功，false--尚未注册，删除失败
     */
    public static boolean deleteString(Context context, String fileName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        return editor.commit();
    }

    /**
     * 根据key获得SharedPreferences中的value
     * @param context
     * @param fileName
     * @param key
     * @return 如果SharedPreferences不存在key的映射，返回""
     */
    public static String getString(Context context, String fileName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    /**
     * 根据key获得SharedPreferences中的布尔值
     * @param context
     * @param fileName
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String fileName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return preferences.getBoolean(key, true);
    }

    /**
     * 根据key获得SharedPreferences中的长整型value
     * @param context
     * @param fileName
     * @param key
     * @return
     */
    public static long getLong(Context context, String fileName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preferences.getLong(key, 0);
    }

    /**
     * 根据key获得SharedPreferences中的整型value
     * @param context
     * @param fileName
     * @param key
     * @return
     */
    public static int getInt(Context context, String fileName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }


    /**
     * 根据文件名删除
     * @param context
     * @param fileName
     * @return
     */
    public static boolean removeByFileName(Context context,String fileName){
        boolean flag = true;
        try{
            SharedPreferences preferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.clear();
            edit.commit();
        }catch(Exception e){
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
}
