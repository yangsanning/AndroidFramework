package ysn.com.androidframework.util;

import android.os.Handler;
import android.widget.Toast;

import ysn.com.androidframework.app.MyApplication;

/**
 * 通用toast类
 */
public class ToastUtils {
    private static Toast mToastNormal;
    private static long ThreadID = android.os.Process.myTid();
    private static Handler handler;

    /**
     * 普通的吐司提示
     *
     * @param message 吐司内容
     */
    public static void showNormalToast(String message) {
        long id = Thread.currentThread().getId();
        if (ThreadID == id) {
            makeToast(message);
        } else {
            if (handler == null) {
                handler = new Handler();
            }
            handler.post(() -> makeToast(message));
        }
    }

    public static void showNetworkError() {
        showNormalToast("请检查网络是否正常");
    }

    private static void makeToast(String msg) {
        ToastUtils.cancel();
        mToastNormal = Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT);
        mToastNormal.show();
    }

    public static void cancel() {
        if (mToastNormal != null) {
            mToastNormal.cancel();
        }
    }
}
