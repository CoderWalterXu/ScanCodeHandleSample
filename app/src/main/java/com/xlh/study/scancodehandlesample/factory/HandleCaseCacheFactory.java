package com.xlh.study.scancodehandlesample.factory;

import com.xlh.study.scancodehandlesample.bean.HandleCaseBean;
import com.xlh.study.scancodehandlesample.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Watler Xu
 * time:2020/8/7
 * description: 生产HandleCaseBean对象,加入缓存
 * version:0.0.1
 */
public class HandleCaseCacheFactory {

    static Map<Integer, HandleCaseBean> mHandleCaseBeanMap = new HashMap(5);

    public static HandleCaseBean create(int type, String code) {

        if (code == null) {
            throw new NullPointerException("code can not be null...");
        }

        if (mHandleCaseBeanMap.containsKey(type)) {
            LogUtils.e("Map中有  " + type + "，直接返回");
            return mHandleCaseBeanMap.get(type);
        }

        LogUtils.e("Map中没有" + type + "，开始创建");

        /**
         * 0 可以请求
         * 1 码信息不能为空
         * 2 正在播报,请稍等
         * 3 两次码一样
         */
        switch (type) {
            case 0:
                LogUtils.e("0 可以请求--new");
                HandleCaseBean zero = new HandleCaseBean(true, code, "可以请求");
                mHandleCaseBeanMap.put(0, zero);
                return zero;
            case 1:
                LogUtils.e("1 码信息不能为空--new");
                HandleCaseBean one = new HandleCaseBean(false, code, "码信息不能为空");
                mHandleCaseBeanMap.put(1, one);
                return one;
            case 2:
                LogUtils.e("2 正在播报,请稍等--new");
                HandleCaseBean two = new HandleCaseBean(false, code, "正在播报,请稍等!");
                mHandleCaseBeanMap.put(2, two);
                return two;
            case 3:
                LogUtils.e("3 两次码一样--new");
                HandleCaseBean three = new HandleCaseBean(false, code, "两次码一样");
                mHandleCaseBeanMap.put(3, three);
                return three;
            default:
                LogUtils.e("4 无匹配状态--new");
                HandleCaseBean defaultCase = new HandleCaseBean(false, code, "无匹配状态");
                mHandleCaseBeanMap.put(4, defaultCase);
                return defaultCase;
        }
    }

}
