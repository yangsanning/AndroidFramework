package ysn.com.androidframework.network.request;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import ysn.com.androidframework.constant.UrlConstant;
import ysn.com.androidframework.model.annotation.Required;
import ysn.com.androidframework.model.bean.User;
import ysn.com.androidframework.network.BaseNetworkRequest;
import ysn.com.androidframework.network.NetworkClient;

/**
 * 登录事件绑定
 */
public class LoginNetworkRequest extends BaseNetworkRequest {

    private static LoginNetworkRequest instance;

    public static LoginNetworkRequest getInstance() {
        if (instance == null) {
            synchronized (LoginNetworkRequest.class) {
                if (instance == null) {
                    instance = new LoginNetworkRequest();
                }
            }
        }
        return instance;
    }

    /**
     * 登录
     */
    public void login(@Required String username, @Required String password, @Required Subscriber<User> subscribers) {
        Observable<User> observable = NetworkClient.getInstance().mService
                .login("https://www.baidu.com/", username, password)
                .delay(UrlConstant.DELAY_SUBSCRIBERS_MILLIS_SHORT, TimeUnit.MILLISECONDS)
                .map(new NetworkResultFun<>());
        toSubscribe(observable, subscribers);
    }
}
