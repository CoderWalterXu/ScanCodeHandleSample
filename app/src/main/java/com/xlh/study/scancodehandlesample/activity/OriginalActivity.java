package com.xlh.study.scancodehandlesample.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.xlh.study.scancodehandlesample.R;
import com.xlh.study.scancodehandlesample.utils.HandleCodeUtil;
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
public class OriginalActivity extends AppCompatActivity {

    public static final int MIN_CLICK_DELAY_TIME = 2000;
    private long lastCheckTime = 0;
    private String lastCheckNum = "";

    boolean isSpeaking;

    String code;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);

    }

    public void scan(View view) {
        initData();
        handleScanResult(code);
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


    /**
     * 码信息判空，非空处理码信息，空则终止，并Toast提示，否则继续往下判断情况处理
     * 语音判断，是否语音播报中，是则终止，并Toast提示，否则继续往下判断情况处理
     * 时间判断，同码2s请求一次，是则终止，并Toast提示，否则开始网络请求
     */
    public void handleScanResult(String code) {

        LogUtils.e("扫码的内容：" + code);

        // 码信息判空
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showLongToast(this, "码信息不能为空");
            return;
        }

        // 处理非数字
        code = HandleCodeUtil.takeNum(code);

        // 语音判断
        if (isSpeskingJudge()) {
            ToastUtils.showLongToast(this, "正在播报,请稍等!");
            return;
        }

        // 时间判断
        if (lastCheckNum.equals(code)) {
            //两次码一样，2s内请求一次
            LogUtils.e("两次码一样,开始时间判断");
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastCheckTime > MIN_CLICK_DELAY_TIME) {
                lastCheckTime = currentTime;
                LogUtils.e("两次码一样,2s后第二次请求");
                scanCodeNetRequest(code);
            } else {
                LogUtils.e("两次码一样,2s内不请求");
                ToastUtils.showLongToast(this, "两次码一样");
            }
        } else {
            //两次码不一样，直接请求
            LogUtils.e("两次码不一样，直接请求");
            long currentTime = Calendar.getInstance().getTimeInMillis();
            lastCheckTime = currentTime;
            lastCheckNum = code;
            scanCodeNetRequest(code);
        }


    }

    private boolean isSpeskingJudge() {
        if (isSpeaking) {
            return true;
        } else {
            return false;
        }
    }

    private void scanCodeNetRequest(String code) {
        // 开始网络请求
        LogUtils.e("开始网络请求--code:" + code);
        ToastUtils.showLongToast(this,"开始网络请求");
    }


}
