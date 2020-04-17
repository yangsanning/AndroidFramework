package ysn.com.androidframework.network.interceptor;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author yangsanning
 * @ClassName NetworkInterceptor
 * @Description 在此增加统一参数
 * @Date 2020/1/3
 * @History 2020/1/3 author: description:
 */
public class NetworkInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        //添加请求参数
        HttpUrl url = oldRequest.url().newBuilder()
            .addQueryParameter("token", "token")
            .build();

        //添加请求头
        Request request = oldRequest.newBuilder()
            .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
            .addHeader("Connection", "keep-alive")
            .addHeader("token", "token")
            .method(oldRequest.method(), oldRequest.body())
            .url(url)
            .build();
        return chain.proceed(request);
    }
}
