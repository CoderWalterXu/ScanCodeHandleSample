package com.xlh.study.scancodehandlesample.chain.interceptor;

import android.text.TextUtils;

import com.xlh.study.scancodehandlesample.bean.HandleCaseBean;
import com.xlh.study.scancodehandlesample.chain.IHandleInterceptor;
import com.xlh.study.scancodehandlesample.factory.HandleCaseCacheFactory;
import com.xlh.study.scancodehandlesample.utils.HandleCodeUtil;
import com.xlh.study.scancodehandlesample.utils.LogUtils;

/**
 * @author: Watler Xu
 * time:2020/8/7
 * description: 处理二维码信息
 * version:0.0.1
 */
public class HandleCodeInterceptor implements IHandleInterceptor {

    @Override
    public HandleCaseBean proceed(String code, IHandleInterceptor iHandleInterceptor) {

        LogUtils.e("HandleCodeInterceptor开始");
        if (TextUtils.isEmpty(code)) {
//            return new HandleCaseBean(false, code, "码信息不能为空");
//            return HandleCaseFactory.create(1,code);
            return HandleCaseCacheFactory.create(1, code);
        }

        // 处理非数字
        code = HandleCodeUtil.takeNum(code);

        LogUtils.e("HandleCodeInterceptor--处理后的code:" + code);

        // 当前处理完毕让下一个接着处理
        return iHandleInterceptor.proceed(code, iHandleInterceptor);
    }

}
