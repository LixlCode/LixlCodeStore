package com.baidu.mapframework.app.fpstack;

//import com.baidu.platform.comapi.DebugConfig;

import android.content.Context;

public class DebugUISetting {
    public boolean showPageName;
    public boolean showPageVelocity;
    public boolean useNewFramePage;

    private DebugUISetting(Context context) {
        refreshConfigValues();
    }

    private void refreshConfigValues() {
        showPageName = true;/*(Boolean) DebugConfig.getInstance().getDebugConfig("page_name_display", Boolean.class, false);*/
        showPageVelocity = false;
                /*(Boolean) DebugConfig.getInstance().getDebugConfig("show_page_velocity", Boolean.class, false);*/
        useNewFramePage = false;
                /*(Boolean) DebugConfig.getInstance().getDebugConfig("use_new_frame_page", Boolean.class, false);*/
    }

    private static DebugUISetting sSettingEntity;

    public static DebugUISetting getInstance(Context context) {
        if (sSettingEntity == null) {
            sSettingEntity = new DebugUISetting(context);
        }
        sSettingEntity.refreshConfigValues();
        return sSettingEntity;
    }
}
