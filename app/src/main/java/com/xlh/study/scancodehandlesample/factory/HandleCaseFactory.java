package com.xlh.study.scancodehandlesample.factory;

import com.xlh.study.scancodehandlesample.bean.HandleCaseBean;

/**
 * @author: Watler Xu
 * time:2020/8/7
 * description: 生产HandleCaseBean对象
 * version:0.0.1
 */
public class HandleCaseFactory {

    public static HandleCaseBean create(int type, String code) {

        if (code == null) {
            throw new NullPointerException("code can not be null...");
        }

        /**
         * 0 可以请求
         * 1 码信息不能为空
         * 2 正在播报,请稍等
         * 3 两次码一样
         */
        switch (type) {
            case 0:
                HandleCaseBean zero = new HandleCaseBean(true, code, "可以请求");
                return zero;
            case 1:
                HandleCaseBean one = new HandleCaseBean(false, code, "码信息不能为空");
                return one;
            case 2:
                HandleCaseBean two = new HandleCaseBean(false, code, "正在播报,请稍等!");
                return two;
            case 3:
                HandleCaseBean three = new HandleCaseBean(false, code, "两次码一样");
                return three;
            default:
                HandleCaseBean defaultCase = new HandleCaseBean(false, code, "无匹配状态");
                return defaultCase;
        }
    }

}
