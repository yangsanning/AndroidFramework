package ysn.com.androidframework.widget.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 通用的activity
 */
public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseActivity implements BaseView  {
    public P mPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter=getPresenter();
    }

    protected abstract P getPresenter();
}
