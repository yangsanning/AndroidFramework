package ysn.com.androidframework.network;

import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lazy.library.logging.Logcat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ysn.com.androidframework.BuildConfig;
import ysn.com.androidframework.constant.Constant;
import ysn.com.androidframework.constant.UrlConstant;
import ysn.com.androidframework.network.typeadapter.DoubleTypeAdapter;
import ysn.com.androidframework.network.typeadapter.FloatTypeAdapter;
import ysn.com.androidframework.network.typeadapter.IntegerTypeAdapter;
import ysn.com.androidframework.network.typeadapter.LongTypeAdapter;

/**
 * @Author yangsanning
 * @ClassName NetworkClient
 * @Description 网络组建初始化
 * @Date 2018/6/10
 * @History 2018/6/10 author: description:
 */
public class NetworkClient {

    /**
     * 设缓存有效期为1天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;

    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     */
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;

    private static NetworkClient instance = null;
    public final NetworkApiService mService;

    public static NetworkClient getInstance() {
        if (instance == null) {
            instance = new NetworkClient();
        }
        return instance;
    }

    private NetworkClient() {
        Retrofit retrofit = new Retrofit.Builder()
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(buildPreventRubbishBackendGson()))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .baseUrl(UrlConstant.API_HOME)
            .build();
        mService = retrofit.create(NetworkApiService.class);
    }

    private static OkHttpClient getOkHttpClient() {
        Cache cache = new Cache(new File("", "HttpCache"), 1024 * 1024 * 10);
        return new OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(new NetworkInterceptor())
            .addInterceptor(rewriteCacheControlInterceptor)
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build();
    }

    /**
     * 防止Rubbish Backend的TypeAdapter
     */
    private Gson buildPreventRubbishBackendGson() {
        return new GsonBuilder()
            .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
            .registerTypeAdapter(int.class, new IntegerTypeAdapter())
            .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
            .registerTypeAdapter(double.class, new DoubleTypeAdapter())
            .registerTypeAdapter(Long.class, new LongTypeAdapter())
            .registerTypeAdapter(long.class, new LongTypeAdapter())
            .registerTypeAdapter(Float.class, new FloatTypeAdapter())
            .registerTypeAdapter(float.class, new FloatTypeAdapter())
            .create();
    }

    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static final Interceptor rewriteCacheControlInterceptor = chain -> {
        Request request = chain.request();
        if (!NetworkUtils.isConnected()) {
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build();
        }
        Response originalResponse = chain.proceed(request);
        if (NetworkUtils.isConnected()) {
            //有网的时候读接口上的@Headers里的配置，可以在这里进行统一的设置
            String cacheControl = request.cacheControl().toString();
            return originalResponse.newBuilder()
                .header("Cache-Control", cacheControl)
                .removeHeader("Pragma")
                .build();
        } else {
            return originalResponse.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_CONTROL_CACHE)
                .removeHeader("Pragma")
                .build();
        }
    };

    /**
     * 日志拦截器
     * 调用了response.body().string()方法，response中的流会被关闭，所以需要创建出一个新的response
     */
    private static final Interceptor loggingInterceptor = chain -> {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (BuildConfig.DEBUG && !"get".equalsIgnoreCase(request.method())) {
            printRequestLog(request);

            assert response.body() != null;
            MediaType mediaType = response.body().contentType();
            String string = response.body().string();
            Logcat.d().tag(Constant.LOG_HTTP_RESPONSE_BODY_TAG).msg(string).out();
            Logcat.json().tag(Constant.LOG_HTTP_RESPONSE_BODY_TAG).msg(string).out();
            return response.newBuilder().body(ResponseBody.create(mediaType, string)).build();
        } else {
            return response;
        }
    };

    private static void printRequestLog(Request request) throws IOException {
        RequestBody requestBody = request.body();
        StringBuilder body = new StringBuilder();
        if (requestBody != null) {
            if (requestBody instanceof MultipartBody) {
                MultipartBody multipartBody = (MultipartBody) requestBody;
                for (MultipartBody.Part part : multipartBody.parts()) {
                    Headers headers = part.headers();
                    assert headers != null;
                    int size = headers.size();
                    for (int i = 0; i < size; i++) {
                        body.append(headers.name(i));
                        body.append(headers.value(i));
                        if (i != size - 1) {
                            body.append("\r\n");
                        }
                    }
                }
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = Charset.forName("UTF-8");
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(Charset.forName("UTF-8"));
                }
                assert charset != null;
                body = new StringBuilder(buffer.readString(charset));
            }
        }
        Logcat.d().tag(Constant.LOG_HTTP_REQUEST_TAG).msg(request.url()).out();
        Logcat.d().tag(Constant.LOG_HTTP_REQUEST_TAG).msg(body.toString()).out();
    }
}
