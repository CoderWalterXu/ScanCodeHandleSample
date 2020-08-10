package com.xlh.study.scancodehandlesample.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Watler Xu
 * time:2020/8/7
 * description:
 * version:0.0.1
 */
public class HandleCodeUtil {

    /**
     * 从字符串中取出数字
     *
     * @param str
     * @return
     */
    public static String takeNum(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

}
