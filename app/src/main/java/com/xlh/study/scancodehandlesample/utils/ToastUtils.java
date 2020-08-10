package com.xlh.study.scancodehandlesample.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;


/**
 * Created by admin on 2017/4/24.
 *
 * @version $Rev$
 * @time 2017/4/24 10:54
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */

public class ToastUtils {
    private static Toast toast;//在类前面声明吐司，确保在这个页面只有一个吐司

    public static void showShortToast(Context context, String msg) {


        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.cancel();//关闭吐司显示
            toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }

        //居中
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();//重新显示吐司
    }

    public static void showLongToast(Context context, String msg) {

        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.cancel();//关闭吐司显示
            toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }

        //居中
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();//重新显示吐司
    }

}
