package ysn.com.androidframework.page;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lazy.library.logging.Logcat;

import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ysn.com.androidframework.R;
import ysn.com.androidframework.app.MyApplication;
import ysn.com.androidframework.model.bean.User;
import ysn.com.androidframework.model.db.RoomDatabaseHelper;
import ysn.com.androidframework.widget.mvp.BaseActivity;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_activity_total)
    TextView totalTextView;

    @OnClick({R.id.main_activity_completable, R.id.main_activity_single, R.id.main_activity_maybe})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_activity_completable:
                Completable.fromCallable(new Callable<List<Long>>() {
                    @Override
                    public List<Long> call() throws Exception {
                        User user = new User();
                        return RoomDatabaseHelper.getDefault(MyApplication.getInstance()).getUserDao().insert(user);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onComplete() {
                                Logcat.d("使用Completable插入数据", "onComplete");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Logcat.d("使用Completable插入数据", "onError");
                            }
                        });
                break;
            case R.id.main_activity_single:
                Single.fromCallable(new Callable<List<Long>>() {
                    @Override
                    public List<Long> call() throws Exception {
                        User user = new User();
                        return RoomDatabaseHelper.getDefault(MyApplication.getInstance()).getUserDao().insert(user);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<List<Long>>() {
                            @Override
                            public void onSubscribe(Disposable d) {


                            }

                            @Override
                            public void onSuccess(List<Long> o) {
                                for (Long data : o) {
                                    Logcat.d("使用Single插入数据", "onSuccess ==> " + data);
                                }

                            }

                            @Override
                            public void onError(Throwable e) {
                                Logcat.d("使用Single插入数据", "onError");

                            }
                        });
                break;
            case R.id.main_activity_maybe:
                Maybe.fromCallable(new Callable<List<Long>>() {
                    @Override
                    public List<Long> call() throws Exception {
                        User user = new User();
                        return RoomDatabaseHelper.getDefault(MyApplication.getInstance()).getUserDao().insert(user);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new MaybeObserver<List<Long>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(List<Long> longs) {
                                for (Long data : longs) {
                                    Logcat.d("使用Maybe插入数据", "onSuccess ==> " + data);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Logcat.d("使用Maybe插入数据", "onError");
                            }

                            @Override
                            public void onComplete() {
                                Logcat.d("使用Maybe插入数据", "onComplete");
                            }
                        });
                break;
            default:
                break;
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RoomDatabaseHelper.getDefault(MyApplication.getInstance()).getUserDao().getAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> dogs) throws Exception {
                        totalTextView.setText("当前总数" + dogs.size());
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }
}
