package com.xlh.study.scancodehandlesample.chain;

import com.xlh.study.scancodehandlesample.bean.HandleCaseBean;

/**
 * @author: Watler Xu
 * time:2020/8/7
 * description:责任链抽象接口
 * version:0.0.1
 */
public interface IHandleInterceptor {

    HandleCaseBean proceed(String code, IHandleInterceptor iHandleInterceptor);

}
