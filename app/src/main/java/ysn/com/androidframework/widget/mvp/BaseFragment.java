package ysn.com.androidframework.widget.mvp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;
import ysn.com.androidframework.util.ResUtil;
import ysn.com.androidframework.util.ToastUtils;

/**
 * 底层的fragment封装
 */
public abstract class BaseFragment extends RxFragment {

    /**
     * Acitivity对象
     **/
    protected Activity mActivity;
    /**
     * 当前显示的内容
     **/
    protected View mRootView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
    }

    public void showMessage(@NonNull String msg) {
        ToastUtils.showNormalToast(msg);
    }

    public void showMessage(@StringRes int resId) {
        ToastUtils.showNormalToast(ResUtil.getString(resId));
    }

    protected <T> T bindViewById(@IdRes int id) {
        //noinspection unchecked
        if (id < 0 || mRootView == null) {
            return null;
        }
        //noinspection unchecked
        return (T) mRootView.findViewById(id);
    }

    public void startActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    protected void finishActivity() {
        if (mActivity != null) {
            mActivity.finish();
        }
    }

    protected void finishActivity(Activity mActivity) {
        if (mActivity != null) {
            mActivity.finish();
        }
    }

    protected <T> T activityCast() {
        return (T) mActivity;
    }

//    protected RoomDatabaseHelper getDatabase() {
//        return RoomDatabaseHelper.getDefault(MyApplication.getInstance());
//    }

    public void showProgressDialog(String msg) {
        ((BaseActivity) mActivity).showProgressDialog(msg);
    }

    public void dismissProgressDialog() {
        ((BaseActivity) mActivity).dismissProgressDialog();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();
}
