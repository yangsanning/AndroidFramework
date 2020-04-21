package ysn.com.androidframework.network.request;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import ysn.com.androidframework.constant.UrlConstant;
import ysn.com.androidframework.model.annotation.Required;
import ysn.com.androidframework.model.bean.Article;
import ysn.com.androidframework.model.bean.User;
import ysn.com.androidframework.network.BaseNetworkRequest;
import ysn.com.androidframework.network.NetworkClient;
import ysn.com.androidframework.network.NetworkResult;

/**
 * 文章
 */
public class ArticleNetworkRequest extends BaseNetworkRequest {

    private static ArticleNetworkRequest instance;

    public static ArticleNetworkRequest getInstance() {
        if (instance == null) {
            synchronized (ArticleNetworkRequest.class) {
                if (instance == null) {
                    instance = new ArticleNetworkRequest();
                }
            }
        }
        return instance;
    }

    /**
     * 按照作者昵称搜索文章
     */
    public void getArticleList(@Required String username, @Required String password, @Required Subscriber<Article> subscribers) {
        Observable<Article> observable = NetworkClient.getInstance().mService
                .login(username, password)
                .flatMap(new Func1<NetworkResult<User>, Observable<Article>>() {
                    @Override
                    public Observable<Article> call(NetworkResult<User> userNetworkResult) {
                        return NetworkClient.getInstance().mService.getArticleList(0,
                                userNetworkResult.getData().getPublicName()).map(new NetworkResultFun<>());
                    }
                });
        toSubscribe(observable, subscribers);
    }
}
