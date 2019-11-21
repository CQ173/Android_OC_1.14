package com.huoniao.oc.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/9/8.
 */

public class ToastUtils {
    private static Toast toast;

    public static void showToast(Context context,
                                 String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    /**
     *
     * @param context 上下文
     * @param content 提示内容
     */
    public static void showLongToast(Context context,
                                 String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_LONG);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
