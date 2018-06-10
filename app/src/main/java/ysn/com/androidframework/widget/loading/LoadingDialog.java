package ysn.com.androidframework.widget.loading;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ysn.com.androidframework.R;
import ysn.com.androidframework.util.ToastUtils;

/**
 * loading加载圈
 */
public class LoadingDialog {

    private Dialog loadingDialog;
    private TextView tipText;
    private Runnable runnable;
    private Runnable delayRunnable;
    private View view;
    private Handler handler;
    private String msg;
    private long timeOut = 30000;

    public LoadingDialog(Activity activity) {
        init(activity);
        initTask();
    }

    public void init(Activity context) {
        // 首先得到整个View
        view = LayoutInflater.from(context).inflate(
                R.layout.view_loading_dialog, null);
        // 页面中显示文本
        tipText = view.findViewById(R.id.loading_msg);
        loadingDialog = new Dialog(context, R.style.dialog);
        handler = new Handler();
        // 创建自定义样式的Dialog
        setCanceledOnTouchOutside(true);
    }

    /**
     * 显示dialog
     *
     * @param msg
     */
    public void show(String msg) {
        this.msg = msg;
        handler.postDelayed(delayRunnable, 500);
    }

    private void showLoading() {
        setDialogText(msg);
        loadingDialog.show();
        handler.postDelayed(runnable, timeOut);
    }

    /**
     * 隐藏
     */
    public void dismiss() {
        long id = Thread.currentThread().getId();
        if (id == android.os.Process.myTid()) {
            loadingDialog.dismiss();
        } else {
            handler.post(() -> loadingDialog.dismiss());
        }
        handler.removeCallbacks(runnable);
        handler.removeCallbacks(delayRunnable);
    }

    /**
     * 超时
     */
    private void timeoutCancel(String msg) {
        boolean showing = loadingDialog.isShowing();
        ToastUtils.showNormalToast("网络超时");
        if (showing) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 设置超时
     */
    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * 设置dialog提示消息
     */
    public LoadingDialog setDialogText(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            tipText.setText(msg);
        }
        return this;
    }

    private void initTask() {
        runnable = () -> timeoutCancel("");

        delayRunnable = () -> showLoading();
    }

    /**
     * 设置返回键无效
     */
    public void setCanceledOnTouchOutside(boolean isClick) {
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        loadingDialog.setCancelable(isClick);
    }

    public void onDestroy() {
        loadingDialog.dismiss();
        handler.removeCallbacks(runnable);
    }
}
