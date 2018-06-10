package ysn.com.androidframework.page.login;

import ysn.com.androidframework.model.annotation.Required;
import ysn.com.androidframework.widget.mvp.BaseView;

public interface LoginContract {

    interface LoginView extends BaseView {

        void loginSuccess();

        void loginFailure(String mesaage);
    }

    interface Presenter {

        void login(@Required String username, @Required String password);

    }
}
