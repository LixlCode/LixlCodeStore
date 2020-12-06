package com.wecome.demo.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志保存类
 * Created by v_lixionglin on 17/12/19.
 */

public class LogWriteUtil {

    private static final int MAX_CACHE_SIZE = 1; //50;
    private static final boolean DEBUG = true;
    private static String ROOT_PATH;

    private String mNormalFileName;
    private List<String> mNormalLogList;

    private String mNetLogFileName;
    private List<String> mNetLogList;

    private LogHandler mHandler;
    private boolean mInit = false;

    private static final class Holder {
        public static LogWriteUtil INSTANCE = new LogWriteUtil();
    }

    private LogWriteUtil() {
        HandlerThread thread = new HandlerThread("nlp log write thread");
        thread.start();
        mHandler = new LogHandler(thread.getLooper());
        ROOT_PATH = getSdcardPath() + File.separator + "log";
        Log.e("Lixl", "ROOT_PATH : " + ROOT_PATH);
    }

    public static LogWriteUtil getInstance() {
        return Holder.INSTANCE;
    }

    public void init() {
        if (!DEBUG || mInit) {
            return;
        }
        mNormalFileName = getFileTime() + "_" + "normallog.txt";
        mNetLogFileName = getFileTime() + "_" + "netlog.txt";
        mInit = true;
    }

    public void unInit() {
        if (!DEBUG || !mInit) {
            return;
        }
        mInit = false;

        // flush conmmon日志
        if (mNormalLogList != null) {
            mNormalLogList.add("end time:" + getCurrentTime());
            sendMessage(mNormalLogList, mNormalFileName);
            mNormalLogList.clear();
        }
        // flush 网络日志
        if (mNetLogList != null) {
            mNetLogList.add("end time:" + getCurrentTime());
            sendMessage(mNetLogList, mNetLogFileName);
            mNetLogList.clear();
        }
    }

    public void logNormal(String log) {
        if (!DEBUG || !mInit) {
            return;
        }

        if (mNormalLogList == null) {
            mNormalLogList = new ArrayList<String>(MAX_CACHE_SIZE);
        }
        StringBuilder builder = new StringBuilder();
        builder.append(getCurrentTime()).append(" ")
                .append(System.currentTimeMillis()).append(" ")
                .append(log);

        mNormalLogList.add(builder.toString());

        if (mNormalLogList.size() >= MAX_CACHE_SIZE) {
            sendMessage(mNormalLogList, mNormalFileName);
            mNormalLogList.clear();
        }
    }

    public void logNetInfo(String log) {
        if (!DEBUG || !mInit) {
            return;
        }

        if (mNetLogList == null) {
            mNetLogList = new ArrayList<String>(MAX_CACHE_SIZE);
        }
        StringBuilder builder = new StringBuilder();
        builder.append(getCurrentTime()).append(" ")
                .append(System.currentTimeMillis()).append(" ")
                .append(log);

        mNetLogList.add(builder.toString());

        if (mNetLogList.size() >= MAX_CACHE_SIZE) {
            sendMessage(mNetLogList, mNetLogFileName);
            mNetLogList.clear();
        }
    }

    private void sendMessage(List<String> list, String path) {
        Message msg = mHandler.obtainMessage();
        msg.obj = new LogMessage(new ArrayList<String>(list), path);
        mHandler.sendMessage(msg);
    }

    @SuppressLint ("SimpleDateFormat")
    private String getCurrentTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        return sDateFormat.format(new java.util.Date());
    }

    @SuppressLint ("SimpleDateFormat")
    private String getFileTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM_dd_HH_mm");
        return sDateFormat.format(new java.util.Date());
    }

    private static class LogHandler extends Handler {

        public LogHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.obj == null || !(msg.obj instanceof LogMessage)) {
                return;
            }
            LogMessage logs = (LogMessage) msg.obj;
            if (logs.list != null && logs.list.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (String loc : logs.list) {
                    sb.append(loc);
                    sb.append("\n");
                }
                writeLog(logs.path, sb.toString());
            }
        }

        private void writeLog(String fileName, String log) {
            if (TextUtils.isEmpty(fileName) || TextUtils.isEmpty(log)) {
                return;
            }

            String path = createRootPath();

            String filePath = path + File.separator + fileName;
            Log.e("Lixl", "writeLog filePath : " + filePath);
            File file = new File(filePath);
            FileWriter writer = null;
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                writer = new FileWriter(file, true);
                writer.write(log + "\n");
                writer.flush();
            } catch (IOException e) {
                Log.e("Lixl", "writeLog IOException message: " + e.getMessage());
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        Log.e("Lixl", "writeLog finally IOException message: " + e.getMessage());
                    }
                }
            }
        }

        private String createRootPath() {
            File file = new File(ROOT_PATH);
            if (!file.exists()) {
                boolean flag = file.mkdirs();
                if (!flag) {
                    Log.e("Lixl", "createRootPath error ROOT_PATH = " + ROOT_PATH);
                }
            }
            return file.getAbsolutePath();
        }
    }

    private class LogMessage {
        private List<String> list;
        private String path;

        private LogMessage(List<String> list, String path) {
            this.list = list;
            this.path = path;
        }
    }

    private String getSdcardPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if(sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        if (sdDir != null) {
            return sdDir.toString();
        }
        return "";
    }
}
