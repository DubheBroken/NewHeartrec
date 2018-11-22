package com.dubhe.broken.newheartrec.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lrony on 2018/4/7.
 */
public class ToastUtil {

    private static Toast mToast;

    public static void showToast(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void showToast(Context context, int resID) {
        if (mToast == null) {
            mToast = Toast.makeText(context, resID, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resID);
        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void showToastLong(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            mToast.setText(msg);
        }
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    public static void showToastLong(Context context, int resID) {
        if (mToast == null) {
            mToast = Toast.makeText(context, resID, Toast.LENGTH_LONG);
        } else {
            mToast.setText(resID);
        }
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }
}
