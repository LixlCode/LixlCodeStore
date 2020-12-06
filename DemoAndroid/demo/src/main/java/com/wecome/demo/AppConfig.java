package com.wecome.demo;

public class AppConfig {

    private static AppConfig singleton;

    public static AppConfig getInstance(){
        if (singleton == null) {
            synchronized (AppConfig.class) {
                if (singleton == null) {
                    singleton = new AppConfig();
                }
            }
        }
        return singleton;
    }


    public void setUserSysSigninTm(long tm) {
        //mPreferences.putLong(AccountManager.getInstance().getUid() + USER_SYS_SIGNIN_TM, tm);
    }

    public long getUserSysSigninTm() {
        return 0/*mPreferences.getLong(AccountManager.getInstance().getUid() + USER_SYS_SIGNIN_TM, 0)*/;
    }

    public void setTrackRecordTm(long tm) {
        //mPreferences.putLong(AccountManager.getInstance().getUid() + TRACK_USER_SYS_RECORD_TM, tm);
    }

    public long getTrackRecordTm() {
        return 0/*mPreferences.getLong(AccountManager.getInstance().getUid() + TRACK_USER_SYS_RECORD_TM, 0)*/;
    }

    public boolean isCurrentNaviCardMonth() {
        return true/*mPreferences.getBoolean(NAVI_CARD_WEEK_MONTH_SWITH, true)*/;
    }

    public void setCurrentNaviCardMonth(boolean flag) {
        // mPreferences.putBoolean(NAVI_CARD_WEEK_MONTH_SWITH, flag);
    }

    public void setSigninData(boolean isOpen) {
        //mPreferences.putBoolean(AccountManager.getInstance().getUid() + "user_signin_card", isOpen);
    }

    public boolean getSigninData() {
        return true /*mPreferences.getBoolean(AccountManager.getInstance().getUid() + "user_signin_card", true)*/;
    }

    public void setNaviData(boolean isOpen) {
        // mPreferences.putBoolean(AccountManager.getInstance().getUid() + "user_navi_card", isOpen);
    }

    public boolean getNaviData() {
        return true/*mPreferences.getBoolean(AccountManager.getInstance().getUid() + "user_navi_card", true)*/;
    }


}
