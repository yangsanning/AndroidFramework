package ysn.com.androidframework.widget.mvp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import butterknife.ButterKnife;
import ysn.com.androidframework.util.ResUtil;
import ysn.com.androidframework.util.ToastUtils;
import ysn.com.androidframework.widget.loading.LoadingDialog;

/**
 * 底层的activity封装
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    private LoadingDialog loadingDialog;
    private Map<String, Fragment> fragments = new ArrayMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bugFix();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initDialog();
        setContentView(getLayoutId());
        initView();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    private void initDialog() {
        loadingDialog = new LoadingDialog(this);
    }

    public void showProgressDialog(String msg) {
        loadingDialog.show(msg);
    }

    public void dismissProgressDialog() {
        loadingDialog.dismiss();
    }

    private void bugFix() {
        String brand = Build.BRAND;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (brand != null && brand.equals("OPPO")) {
                oppoActionBar();
            }
            if (brand != null && brand.equals("Xiaomi")) {
                xiaomiUi();
            }
        }
        /**6.0*/
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            android6SystemUi();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void oppoActionBar() {
        getWindow().addFlags(0x80000000);
        getWindow().setStatusBarColor(0x0);
        getWindow().getDecorView().setSystemUiVisibility(0x10);
    }

    /**
     * 设置Android状态栏的字体颜色，状态栏为亮色的时候字体和图标是黑色，状态栏为暗色的时候字体和图标为白色
     */
    private void xiaomiUi() {
        try {
            Window window = getWindow();
            Class clazz = getWindow().getClass();
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (true) {
                //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {
                //清除黑色字体
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * android6.0+系统隐藏状态栏阴影
     */
    private void android6SystemUi() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadingDialog.onDestroy();
    }

    public void showMessage(@NonNull String msg) {
        ToastUtils.showNormalToast(msg);
    }

    public void showMessage(@StringRes int resId) {
        ToastUtils.showNormalToast(ResUtil.getString(resId));
    }

    public final void replaceFragmentToBackStack(@IdRes int containerViewId, Fragment fragment) {
        if (fragment != null) {
            replaceFragment(containerViewId, fragment, fragment.getClass().getCanonicalName(), true);
        }
    }

    public final void replaceFragment(@IdRes int containerViewId, Fragment fragment) {
        if (fragment != null) {
            replaceFragment(containerViewId, fragment, fragment.getClass().getCanonicalName(), false);
        }
    }

    public void replaceFragment(@IdRes int containerViewId, @NonNull Fragment fragment, String className, boolean isAddToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment containerFragment = fragmentManager.findFragmentById(containerViewId);
        if (containerFragment == null) {
            fragmentTransaction.add(containerViewId, fragment, className);
        } else {
            fragmentTransaction.replace(containerViewId, fragment, className);
        }

        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(className);
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    public <T extends Fragment> T findFragment(@NonNull Class<T> clazz, @NonNull final FragmentCallBack fragmentCallBack) {
        String key = clazz.getName();
        Fragment fragment = fragments.get(key);
        if (fragment == null) {
            fragment = fragmentCallBack.onCreateFragment();
            fragments.put(key, fragment);
        }
        //noinspection unchecked
        return (T) fragment;
    }

    public interface FragmentCallBack {
        Fragment onCreateFragment();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        return false;
    }
}
