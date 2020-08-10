package com.xlh.study.scancodehandlesample.chain;

import com.xlh.study.scancodehandlesample.bean.HandleCaseBean;
import com.xlh.study.scancodehandlesample.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Watler Xu
 * time:2020/8/7
 * description: 扫码处理的责任链管理类
 * version:0.0.1
 */
public class HandleChain implements IHandleInterceptor {

    // 所有Interceptor列表
    private List<IHandleInterceptor> mInterceptorList = new ArrayList<>();
    // 索引，用于遍历所有Interceptor列表
    private int index = 0;

    // 添加Interceptor
    public HandleChain addHandleChain(IHandleInterceptor iHandleInterceptor) {
        mInterceptorList.add(iHandleInterceptor);
        return this;
    }

    @Override
    public HandleCaseBean proceed(String code, IHandleInterceptor iHandleInterceptor) {
        LogUtils.e("index:" + index);

        if (index >= mInterceptorList.size()) {
            throw new AssertionError();
        }
        // 获取当前Interceptor
        IHandleInterceptor currentInterceptor = mInterceptorList.get(index);
        // 修改索引值，以便下次回调获取下个节点，达到遍历效果
        index++;
        // 调用当前Interceptor处理方法
        return currentInterceptor.proceed(code, this);
    }
}
