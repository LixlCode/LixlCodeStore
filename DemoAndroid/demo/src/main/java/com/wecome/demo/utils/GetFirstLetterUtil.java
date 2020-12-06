package com.wecome.demo.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

/**
 * User: v_lixionglin
 * Date: 2017/12/22.
 * Time: 12:12 PM
 *
 * 获取汉字拼音的首字母的工具类
 */

public class GetFirstLetterUtil {

    static final int GB_SP_DIFF = 160;
    // 存放国标一级汉字不同读音的起始区位码
    static final int[] secPosValueList = {1601, 1637, 1833, 2078, 2274, 2302,
            2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027,
            4086, 4390, 4558, 4684, 4925, 5249, 5600};
    // 存放国标一级汉字不同读音的起始区位码对应读音
    static final char[] firstLetter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x',
            'y', 'z'};

    public static String getSpells(String characters) {
        StringBuffer buffer = new StringBuffer();
        char ch = characters.charAt(0);
        if (Pattern.compile("[0-9]*").matcher(ch + "").matches()) { // 数字
            buffer.append(String.valueOf("*"));
            return buffer.toString();
        } else if (Pattern.compile("[a-zA-Z]").matcher(ch + "").matches()) { // 字母
            buffer.append(String.valueOf(ch));
            return buffer.toString();
        } else if (Pattern.compile("[\u4e00-\u9fa5]").matcher(ch + "").matches()) { // 汉字
            char spell = getFirstLetter(ch);
            buffer.append(String.valueOf(spell));
        } else { // 其他
            buffer.append(String.valueOf("*"));
            return buffer.toString();
        }
        return buffer.toString();
    }

    //  获取一个汉字的首字母
    public static Character getFirstLetter(char ch) {
        byte[] uniCode = null;
        try {
            uniCode = String.valueOf(ch).getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
            return null;
        } else {
            return convert(uniCode);
        }
    }

    /**
     * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
     */
    static char convert(byte[] bytes) {
        char result = '-';
        int secPosValue = 0;
        int i;
        for (i = 0; i < bytes.length; i++) {
            bytes[i] -= GB_SP_DIFF;
        }
        secPosValue = bytes[0] * 100 + bytes[1];
        for (i = 0; i < 23; i++) {
            if (secPosValue >= secPosValueList[i]
                    && secPosValue < secPosValueList[i + 1]) {
                result = firstLetter[i];
                break;
            }
        }
        return result;
    }

}
