package com.xlh.study.scancodehandlesample.chain.interceptor;

import com.xlh.study.scancodehandlesample.bean.HandleCaseBean;
import com.xlh.study.scancodehandlesample.chain.IHandleInterceptor;
import com.xlh.study.scancodehandlesample.factory.HandleCaseCacheFactory;
import com.xlh.study.scancodehandlesample.utils.LogUtils;

import java.util.Calendar;

/**
 * @author: Watler Xu
 * time:2020/8/7
 * description:
 * version:0.0.1
 */
public class TimeInterceptor implements IHandleInterceptor {

    // 间隔时间
    private static final int MIN_CLICK_DELAY_TIME = 2000;
    // 上次时间
    private long lastCheckTime = 0;
    // 上次码
    private String lastCheckNum = "";

    public TimeInterceptor(String lastCheckNum, long lastCheckTime) {
        this.lastCheckNum = lastCheckNum;
        this.lastCheckTime = lastCheckTime;
    }

    @Override
    public HandleCaseBean proceed(String code, IHandleInterceptor iHandleInterceptor) {
        LogUtils.e("TimeInterceptor开始");
        LogUtils.e("TimeInterceptor--code：" + code);
        LogUtils.e("TimeInterceptor--lastCheckNum：" + lastCheckNum);
        LogUtils.e("TimeInterceptor--lastCheckTime：" + lastCheckTime);
        if (lastCheckNum.equals(code)) {
            //两次码一样，2s内请求一次
            LogUtils.e("TimeInterceptor--两次码一样，开始时间判断");
            // 获取当前系统时间
            long currentTime = Calendar.getInstance().getTimeInMillis();
            LogUtils.e("TimeInterceptor--currentTime：" + currentTime);

            if (currentTime - lastCheckTime > MIN_CLICK_DELAY_TIME) {
                LogUtils.e("TimeInterceptor--两次码一样,2s后第二次请求");
//                return new HandleCaseBean(true, code, "可以请求");
//                return HandleCaseFactory.create(0,code);
                return HandleCaseCacheFactory.create(0, code);
            } else {
                LogUtils.e("TimeInterceptor--两次码一样,2s内不请求");
//                return new HandleCaseBean(false, code, "两次码一样");
//                return HandleCaseFactory.create(3,code);
                return HandleCaseCacheFactory.create(3, code);
            }
        } else {
            //两次码不一样，直接请求
            LogUtils.e("TimeInterceptor--两次码不一样，直接请求");
//            return new HandleCaseBean(true, code, "可以请求");
//            return HandleCaseFactory.create(0,code);
            return HandleCaseCacheFactory.create(0, code);
        }
    }
}
