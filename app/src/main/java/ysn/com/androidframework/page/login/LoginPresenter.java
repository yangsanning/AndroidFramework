package ysn.com.androidframework.page.login;

import android.support.annotation.NonNull;

import ysn.com.androidframework.R;
import ysn.com.androidframework.model.bean.Article;
import ysn.com.androidframework.model.bean.User;
import ysn.com.androidframework.network.NetworkCallback;
import ysn.com.androidframework.network.request.ArticleNetworkRequest;
import ysn.com.androidframework.network.request.LoginNetworkRequest;
import ysn.com.androidframework.util.ResUtil;
import ysn.com.androidframework.widget.mvp.BasePresenter;

public class LoginPresenter extends BasePresenter<LoginContract.LoginView> implements LoginContract.Presenter {

    public LoginPresenter(LoginContract.LoginView view) {
        super(view);
    }

    @Override
    public void login(@NonNull String username, @NonNull String password) {
        mView.onLoading(ResUtil.getString(R.string.progress_dialog_loading));
        LoginNetworkRequest.getInstance().login(username, password, new NetworkCallback<User>() {
            @Override
            public void onSuccess(User user) {
                mView.onLoadComplete();
                mView.loginSuccess();
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.onLoadComplete();
                mView.loginFailure(msg);
            }
        });
    }

    @Override
    public void getArticleList(String username, String password) {
        ArticleNetworkRequest.getInstance().getArticleList(username, password, new NetworkCallback<Article>() {
            @Override
            public void onSuccess(Article data) {
                mView.onLoadComplete();
                mView.loginSuccess();
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.onLoadComplete();
                mView.loginFailure(msg);
            }
        });
    }
}
