package ysn.com.androidframework.widget.mvp;

/**
 * mvp的p的通用处理类
 *
 * @param <V>
 */
public class BasePresenter<V> {
    public V mView;

    public BasePresenter(V view) {
        this.mView = view;
    }
}
