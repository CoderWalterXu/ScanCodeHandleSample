package com.xlh.study.scancodehandlesample.bean;

/**
 * @author: Watler Xu
 * time:2020/8/7
 * description:
 * version:0.0.1
 */
public class HandleCaseBean {

    boolean isRequest;
    // 二维码信息
    String code;

    String msg;

    public HandleCaseBean(boolean isRequest, String code, String msg) {
        this.isRequest = isRequest;
        this.code = code;
        this.msg = msg;
    }

    public boolean isRequest() {
        return isRequest;
    }

    public void setRequest(boolean request) {
        isRequest = request;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
