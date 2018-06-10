package ysn.com.androidframework.app;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.lazy.library.logging.Builder;
import com.lazy.library.logging.Logcat;

import ysn.com.androidframework.BuildConfig;
import ysn.com.androidframework.constant.Constant;
import ysn.com.androidframework.util.ResUtil;

public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initialize();
    }

    private void initialize() {
        ResUtil.inject(mInstance);
        initLogCat();
        Utils.init(this);
    }

    private void initLogCat() {
        Builder builder = Logcat.newBuilder();
        builder.topLevelTag(Constant.LOG_GLOBAL_TAG);
        if (BuildConfig.DEBUG) {
            builder.logCatLogLevel(Logcat.SHOW_ALL_LOG);
        } else {
            builder.logCatLogLevel(Logcat.SHOW_INFO_LOG | Logcat.SHOW_WARN_LOG | Logcat.SHOW_ERROR_LOG);
        }
        Logcat.initialize(this, builder.build());
    }

    public static MyApplication getInstance() {
        return mInstance;
    }
}
