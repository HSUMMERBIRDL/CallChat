package com.hl.foundation.library.utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * des:
 * Created by HL
 * on 2017-05-02.
 */

public class StringUtils {


    /**
     * 获取UUID
     * @return
     */
    public  static String getUUID(){

        String uuid2 = UUID.randomUUID().toString();
        String replace = uuid2.replace("-", "");

        return  replace;
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(CharSequence input) {
        if (input == null || input.length() == 0)
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(String str1, String str2)
    {
        if ( str1 == null )
        {
            return str2 == null;
        }

        return str1.equals(str2);
    }

    /**
     * 是否是数字和字母X的组合
     * @param txt
     * @return
     */
    public static boolean isNumOrChartersX(String txt) {

        String regex="^[X0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher match=pattern.matcher(txt);
        boolean b=match.matches();
        return  b;
    }

    /**
     * 是否是数字
     * @param txt
     * @return
     */
    public static boolean isNum(String txt) {

        String regex="^[0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher match=pattern.matcher(txt);
        boolean b=match.matches();
        return  b;
    }
}
