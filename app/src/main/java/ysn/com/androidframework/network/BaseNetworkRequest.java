package ysn.com.androidframework.network;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import ysn.com.androidframework.constant.NetworkCode;

/**
 * 网络请求基础类
 */
public class BaseNetworkRequest {
    /**
     * 根据返回的状态,抛出异常或将返回数据的固定格式中的data值剥取出来
     *
     * @param <T> 具体业务所需的数据类型
     */
    public class NetworkResultFun<T> implements Func1<NetworkResult<T>, T> {

        @Override
        public T call(NetworkResult<T> tNetworkResult) {
            if (tNetworkResult.getErrorCode() < NetworkCode.CODE_SUCCESS_CRITICAL) {
                throw new ApiException(tNetworkResult.getErrorCode(), tNetworkResult.getErrorMsg());
            }
            return tNetworkResult.getData();
        }
    }

    /**
     * 切换线程,绑定观察者
     *
     * @param o   被观察者
     * @param s   观察者
     * @param <T> 泛型
     */
    protected <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        Scheduler io = Schedulers.io();
        o.subscribeOn(io)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(io)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
