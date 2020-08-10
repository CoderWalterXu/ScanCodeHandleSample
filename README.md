


# ScanCodeHandleSample
根据OkHttp使用责任链处理网络请求的方式，配合工厂模式和缓存，对项目中的扫码处理流程进行重构


# 原始代码
```
    /**
     * 码信息判空，空则终止，并Toast提示，否则处理码信息，并继续往下判断情况处理
     * 语音判断，是否语音播报中，是则终止，并Toast提示，否则继续往下判断情况处理
     * 时间判断，同码2s请求一次，是则终止，并Toast提示，否则开始网络请求
     */
    public void handleScanResult(String code) {

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
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastCheckTime > MIN_CLICK_DELAY_TIME) {
                lastCheckTime = currentTime;
                scanCodeNetRequest(code);
            } else {
                ToastUtils.showLongToast(this, "两次码一样");
            }
        } else {
            //两次码不一样，直接请求
            long currentTime = Calendar.getInstance().getTimeInMillis();
            lastCheckTime = currentTime;
            lastCheckNum = code;
            scanCodeNetRequest(code);
        }


    }
```

## 原始代码思考
所有的情况判断都在一个方法里，不便于阅读，如果有新增判断，还需要改动这段代码，容易出现问题。应易于扩展，且对修改关闭          


参考了OkHttp中的责任链模式             
将这三种情况拆分到三个类，并实现IHandleInterceptor接口

# 按照责任链模式重构

```
public interface IHandleInterceptor {

    HandleCaseBean proceed(String code, IHandleInterceptor iHandleInterceptor);

}
```

判空处理码信息
```
public class HandleCodeInterceptor implements IHandleInterceptor {

    @Override
    public HandleCaseBean proceed(String code, IHandleInterceptor iHandleInterceptor) {

        if (TextUtils.isEmpty(code)) {
            return new HandleCaseBean(false, code, "码信息不能为空");
        }

        // 处理非数字
        code = HandleCodeUtil.takeNum(code);

        // 当前处理完毕让下一个接着处理
        return iHandleInterceptor.proceed(code, iHandleInterceptor);
    }

}
```

语音判断
```
public class VoiceInterceptor implements IHandleInterceptor {

    boolean isSpeak;

    public VoiceInterceptor(boolean isSpeak) {
        this.isSpeak = isSpeak;
    }

    @Override
    public HandleCaseBean proceed(String code, IHandleInterceptor iHandleInterceptor) {
        if (isSpeak) {
            return new HandleCaseBean(false, code, "正在播报,请稍等!");
        }
        // 当前处理完毕让下一个接着处理
        return iHandleInterceptor.proceed(code, iHandleInterceptor);
    }
}

```

时间判断
```
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
        if (lastCheckNum.equals(code)) {
            //两次码一样，2s内请求一次
            // 获取当前系统时间
            long currentTime = Calendar.getInstance().getTimeInMillis();

            if (currentTime - lastCheckTime > MIN_CLICK_DELAY_TIME) {
                return new HandleCaseBean(true, code, "可以请求");
            } else {
                return new HandleCaseBean(false, code, "两次码一样");
            }
        } else {
            //两次码不一样，直接请求
            return new HandleCaseBean(true, code, "可以请求");
        }
    }
}
```

扫码处理的责任链管理类
```
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
```

使用时
```
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
```

## 责任链中的new HandleCaseBean代码思考
重构时发现new HandleCaseBean分散在每个Interceptor类中，如果要修改入参参数，得一个个找，有没有修改时不去找类，也不改动每个类的方法呢？           
想了想，可以通过工厂模式统一生产HandleCaseBean类

# 工厂模式生产HandleCaseBean类
```
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
```


接下来改动每个Interceptor类
```
return new HandleCaseBean(false, code, "码信息不能为空");
```
改为
```
return HandleCaseFactory.create(1,code);
```
其他改动类似。这样如果需要改动入参参数，比如提示这类，只需要在HandleCaseFactory改动即可，不影响具体的Interceptor类

## HandleCaseFactory类中的new HandleCaseBean思考
在实际项目中，扫码处理是很频繁的操作，各种异常提示也频频出现，背后new HandleCaseBean的次数也会异常多，能不能减少new的次数？           
可以使用HashMap缓存，把已有HandleCaseBean存起来，对应的key就是type值，下次直接从HashMap中取，不用再次new

# HandleCaseFactory类增加HashMap缓存
```
public class HandleCaseCacheFactory {

    static Map<Integer, HandleCaseBean> mHandleCaseBeanMap = new HashMap(5);

    public static HandleCaseBean create(int type, String code) {

        if (code == null) {
            throw new NullPointerException("code can not be null...");
        }

        if (mHandleCaseBeanMap.containsKey(type)) {
            return mHandleCaseBeanMap.get(type);
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
                mHandleCaseBeanMap.put(0, zero);
                return zero;
            case 1:
                HandleCaseBean one = new HandleCaseBean(false, code, "码信息不能为空");
                mHandleCaseBeanMap.put(1, one);
                return one;
            case 2:
                HandleCaseBean two = new HandleCaseBean(false, code, "正在播报,请稍等!");
                mHandleCaseBeanMap.put(2, two);
                return two;
            case 3:
                HandleCaseBean three = new HandleCaseBean(false, code, "两次码一样");
                mHandleCaseBeanMap.put(3, three);
                return three;
            default:
                HandleCaseBean defaultCase = new HandleCaseBean(false, code, "无匹配状态");
                mHandleCaseBeanMap.put(4, defaultCase);
                return defaultCase;
        }
    }

}
```


接下来改动每个Interceptor类
```
return HandleCaseFactory.create(1,code);
```
HandleCaseFactory改为HandleCaseCacheFactory
```
return HandleCaseCacheFactory.create(1, code);
```
其他改动类似。


# 参考资料
本文写作时，也参考了以下博客思路，对此表示感谢          

https://juejin.im/post/6844903710510989325


