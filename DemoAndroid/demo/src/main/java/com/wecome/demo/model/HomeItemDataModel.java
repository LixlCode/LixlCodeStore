package com.wecome.demo.model;

public class HomeItemDataModel {

    public static final int OFFLINE_TYPE = 1;
    public static final int FAV_TYPE = 3;
    public static final int ADDR_TYPE = 4;
    public static final int TRACK_TYPE = 5;
    public static final int SCHE_TYPE = 6;
    public static final int MEASSURE_TYPE = 7;
    public static final int THEME_TYPE = 8;
    public static final int CHARGEPILE_TYPE = 9;
    public static final int VIDEO_TYPE = 10;
    public static final int NEWS_TYPE = 11;
    public static final int SHARE_BIKE_TYPE = 12;
    public static final int RUNNING_ROUTE = 14;
    public static final int AUTO_WIFI = 15;
    public static final int CAR_SERVICE = 16;
    public static final int ORDER_CAR = 17;
    public static final int VOICE_SKILL = 18;

    public int nameStringId;
    public int mIconResId;
    public int mEventType;
    public boolean isRedPoint = false;
    public boolean isNewPoint = false;

    public HomeItemDataModel(int nameId,
                             int id,
                             int type,
                             boolean redpoint,
                             boolean newPoint) {
        nameStringId = nameId;
        mIconResId = id;
        mEventType = type;
        isRedPoint = redpoint;
        isNewPoint = newPoint;
    }
}
