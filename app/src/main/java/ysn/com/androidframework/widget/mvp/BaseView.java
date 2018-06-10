package ysn.com.androidframework.widget.mvp;

/**
 * mvp的View通用回调接口
 */
public interface BaseView {

    void onLoading(String msg);

    void onLoadComplete();
}
