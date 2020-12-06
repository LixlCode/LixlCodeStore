package com.wecome.demo.model;

import java.util.ArrayList;

/**
 * Created by chenjie07 on 17/8/28.
 * 国际化卡片数据
 */

public class InternationalModel {
    public String url;
    public int totalNum;
    public ArrayList<CountryModel> eastData = new ArrayList<>();
    public ArrayList<CountryModel> westData = new ArrayList<>();

    public static class CountryModel {
        public static String format(String str) {
            if (str.length() > 4) {
                str = (str.substring(0, 3) + "...");
            }
            if (str.length() >= 4) {
                StringBuffer buffer = new StringBuffer(str);
                buffer.insert(2, "\n");
                return buffer.toString();
            }
            return str;
        }

        public String countryName;
        public ArrayList<String> citys = new ArrayList<>();
    }
}
