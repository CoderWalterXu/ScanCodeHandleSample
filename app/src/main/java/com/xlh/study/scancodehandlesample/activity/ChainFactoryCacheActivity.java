package com.xlh.study.scancodehandlesample.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xlh.study.scancodehandlesample.R;
import com.xlh.study.scancodehandlesample.bean.HandleCaseBean;
import com.xlh.study.scancodehandlesample.chain.HandleChain;
import com.xlh.study.scancodehandlesample.chain.interceptor.HandleCodeInterceptor;
import com.xlh.study.scancodehandlesample.chain.interceptor.TimeInterceptor;
import com.xlh.study.scancodehandlesample.chain.interceptor.VoiceInterceptor;
import com.xlh.study.scancodehandlesample.utils.LogUtils;
import com.xlh.study.scancodehandlesample.utils.ToastUtils;

import java.util.Calendar;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: Watler Xu
 * time:2020/8/7
 * description:
 * version:0.0.1
 */
public class ChainFactoryCacheActivity extends AppCompatActivity {

    private long lastCheckTime = 0;
    private String lastCheckNum = "";

    boolean isSpeaking;

    String code;

    HandleChain handleChain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
    }

    public void scan(View view) {
        initData();
        initChain();
        handleScanResult();
    }


    /**
     * 责任链
     */
    private void initChain() {
        handleChain = new HandleChain();
        handleChain
                // 添加处理码拦截器
                .addHandleChain(new HandleCodeInterceptor())
                // 添加语音判断拦截器
                .addHandleChain(new VoiceInterceptor(isSpeaking))
                // 添加时间判断拦截器
                .addHandleChain(new TimeInterceptor(lastCheckNum, lastCheckTime));
    }

    /**
     * 测试不同情况
     */
    private void initData() {
        // 不同码
        Random r1 = new Random();
        int ran1 = r1.nextInt(3);
        if (0 == ran1) {
            code = "";
        } else if (1 == ran1) {
            code = "123456";
        } else if (2 == ran1) {
            code = "123456nnn\r\n";
        }


        // 是否语音播报
        Random r2 = new Random();
        int ran2 = r2.nextInt(2);
        if (0 == ran2) {
            isSpeaking = false;
        } else {
            isSpeaking = true;
        }

        // 走完所有拦截器的测试数据
//        code = "123456nnn\r\n";
//        isSpeaking = false;

        LogUtils.e("随机生成的码：" + code + "    语音播报状态：" + isSpeaking);

    }

    private void handleScanResult() {
        // 责任链开始处理并返回处理结果
        HandleCaseBean handleCaseBean = handleChain.proceed(code, handleChain);

        String handleCode = handleCaseBean.getCode();
        LogUtils.e("处理后的code:" + handleCode);

        // 开始网络核销，或Toast异常信息
        if (handleCaseBean.isRequest()) {
            // 传入处理后的code
            scanCodeNetRequest(handleCode);
        } else {
            ToastUtils.showLongToast(this, handleCaseBean.getMsg() + "");
        }

        lastCheckTime = Calendar.getInstance().getTimeInMillis();
        lastCheckNum = handleCode;

    }

    private void scanCodeNetRequest(String code) {
        // 开始网络请求
        LogUtils.e("开始网络请求--code:" + code);
        ToastUtils.showLongToast(this,"开始网络请求");
    }

}
