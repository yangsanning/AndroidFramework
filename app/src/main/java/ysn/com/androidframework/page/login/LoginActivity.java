package ysn.com.androidframework.page.login;

import ysn.com.androidframework.R;
import ysn.com.androidframework.widget.mvp.BaseActivity;

/**
 * 登录页
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        showLoginFragment();
    }

    public void showLoginFragment() {
        replaceFragment(R.id.login_activity_container_ft, findFragment(LoginFragment.class, LoginFragment::new));
    }
}