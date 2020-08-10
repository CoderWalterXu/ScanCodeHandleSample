package com.xlh.study.scancodehandlesample.chain.interceptor;

import com.xlh.study.scancodehandlesample.bean.HandleCaseBean;
import com.xlh.study.scancodehandlesample.chain.IHandleInterceptor;
import com.xlh.study.scancodehandlesample.factory.HandleCaseCacheFactory;
import com.xlh.study.scancodehandlesample.utils.LogUtils;

/**
 * @author: Watler Xu
 * time:2020/8/7
 * description:
 * version:0.0.1
 */
public class VoiceInterceptor implements IHandleInterceptor {

    boolean isSpeak;

    public VoiceInterceptor(boolean isSpeak) {
        this.isSpeak = isSpeak;
    }

    @Override
    public HandleCaseBean proceed(String code, IHandleInterceptor iHandleInterceptor) {
        LogUtils.e("VoiceInterceptor开始");
        LogUtils.e("VoiceInterceptor--isSpeak：" + isSpeak);
        if (isSpeak) {
//            return new HandleCaseBean(false, code, "正在播报,请稍等!");
//            return HandleCaseFactory.create(2,code);
            return HandleCaseCacheFactory.create(2, code);
        }
        // 当前处理完毕让下一个接着处理
        return iHandleInterceptor.proceed(code, iHandleInterceptor);
    }
}
