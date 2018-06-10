package ysn.com.androidframework.app;

import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initialize();
    }

    private void initialize() {

    }

    public static MyApplication getInstance() {
        return mInstance;
    }
}
