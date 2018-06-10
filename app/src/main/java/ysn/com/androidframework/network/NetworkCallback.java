package ysn.com.androidframework.network;

import rx.Subscriber;
import ysn.com.androidframework.R;
import ysn.com.androidframework.util.ResUtil;

/**
 * 网络返回的回调接口，可以在此对异常数据进行处理
 *
 * @param <T>
 */
public abstract class NetworkCallback<T> extends Subscriber<T> {

    public abstract void onSuccess(T data);

    public abstract void onFailure(int code, String msg);

    @Override
    public void onCompleted() {

    }

    /**
     * 对错误进行统一处理
     */
    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            onFailure(apiException.resultCode, apiException.getMessage());
        } else {
            onFailure(0, ResUtil.getString(R.string.network_exception));
            e.printStackTrace();
        }
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }
}

