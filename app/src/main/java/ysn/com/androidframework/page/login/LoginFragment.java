package ysn.com.androidframework.page.login;

import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import ysn.com.androidframework.R;
import ysn.com.androidframework.page.MainActivity;
import ysn.com.androidframework.widget.mvp.BaseMvpFragment;

/**
 * 登录
 */
public class LoginFragment extends BaseMvpFragment<LoginPresenter> implements LoginContract.LoginView {

    @BindView(R.id.login_fragment_username_et)
    EditText usernameEt;

    @BindView(R.id.login_fragment_password_et)
    EditText passwordEt;

    @OnClick(R.id.login_fragment_login_bt)
    public void login(View v) {
        mPresenter.getArticleList(usernameEt.getText().toString().trim(), passwordEt.getText().toString().trim());
    }

    @OnClick(R.id.login_fragment_show_password_iv)
    public void showPassword(ImageView imageView) {
        if (passwordEt.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            imageView.setImageResource(R.drawable.icon_show_password_normal);
            passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            imageView.setImageResource(R.drawable.icon_show_password_selected);
            passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        Editable eatable = passwordEt.getText();
        Selection.setSelection(eatable, eatable.length());
    }

    @Override
    protected LoginPresenter getPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onLoading(String msg) {
        showProgressDialog(msg);
    }

    @Override
    public void onLoadComplete() {
        dismissProgressDialog();
    }

    @Override
    public void loginSuccess() {
        startActivity(MainActivity.class);
    }

    @Override
    public void loginFailure(String message) {
        showMessage(message);
    }
}
