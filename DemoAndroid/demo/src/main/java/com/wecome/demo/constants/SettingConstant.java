package com.wecome.demo.constants;

/**
 * Created by ouyangkun on 2014/4/29.
 */
public class SettingConstant {
    public static final int POISOURCE = 1;
    public static final int ROUTESOURCE = 2;
    public static final int ENTER = 1;
    public static final int EXIT = 2;


    public static final float PressAlphaTextVal = 0.40f; // 文字变色透明度
    public static final float PressAlphaIconVal = 0.40f; // 文字+Icon变色透明度


    public static class IndoorParam {
        public static final String BUILDING_NAME = "building_name";
        public static final String BUILDING_ID = "building_id";
        public static final String SCENE_SEARCH = "scene_search";
    }

    public class PageFlag {
        private static final int PAGE_FLAG_BASE = 1000;
        public static final int CHOOSE_PHOTO_PAGE = PAGE_FLAG_BASE + 1;
        public static final int ADD_COMMENT_PAGE = PAGE_FLAG_BASE + 2;

        private PageFlag() {
        }
    }
}
