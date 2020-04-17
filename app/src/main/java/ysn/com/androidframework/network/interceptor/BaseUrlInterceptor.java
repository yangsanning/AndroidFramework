package ysn.com.androidframework.network.interceptor;

import android.text.TextUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author yangsanning
 * @ClassName NetworkInterceptor
 * @Description 在此进行BASE_URL的更换
 * @Date 2020/1/3
 * @History 2020/1/3 author: description:
 */
public class BaseUrlInterceptor implements Interceptor {

    public static final String BASE_URL = "BASE_URL";

    @Override
    public Response intercept(Chain chain) throws IOException {
        // 获取oldRequest
        Request oldRequest = chain.request();

        // 获取oldRequest的创建者builder
        Request.Builder builder = oldRequest.newBuilder();

        // 通过BASE_URL, 获取headers
        List<String> baseUrlList = oldRequest.headers(BASE_URL);
        if (baseUrlList != null && baseUrlList.size() > 0) {
            // 先将baseUrl移除, 以保证baseUrl仅用作app和okhttp之间
            builder.removeHeader(BASE_URL);

            // 获取原有的HttpUrl
            HttpUrl oldHttpUrl = oldRequest.url();

            // 匹配获得新的baseUrl
            String baseUrl = baseUrlList.get(0);
            HttpUrl newBaseUrl = TextUtils.isEmpty(baseUrl) ? oldHttpUrl : HttpUrl.parse(baseUrl);

            // 重建新的HttpUrl, 修改需要修改的url部分
            HttpUrl newHttpUrl = oldHttpUrl
                    .newBuilder()
                    .scheme(newBaseUrl.scheme())
                    .host(newBaseUrl.host())
                    .port(newBaseUrl.port())
                    .build();
            return chain.proceed(builder.url(newHttpUrl).build());
        } else {
            return chain.proceed(oldRequest);
        }
    }
}
