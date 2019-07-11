# AndroidFramework

## [网络组件NetworkClient]

#### 日志拦截器
调用了response.body().string()方法，response中的流会被关闭，所以需要创建出一个新的response
``` java
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
```


#### TypeAdapter
用来应付接口数据格式返回错误, 例如Long返回了null
```  java
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
```


#### 云端响应头拦截器
用来配置缓存策略
``` java 
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
```

---

## [依赖统一管理configs.gradle]

```android
ext {
    version = [
            minSdkVersion    : 19,
            compileSdkVersion: 27,
            targetSdkVersion : 27,
            versionCode      : 10000,
            versionName      : "1.0.0",
    ]

    dependVersion = [
            support_version: "27.1.1",
            retrofit       : "2.4.0",
            rxJava         : "2.1.12",
            rxAndroid      : "2.0.2",
            rxLife         : "1.0",
            butterknife    : "8.5.1",
    ]

    supportLib = [
            v7               : "com.android.support:appcompat-v7:$dependVersion.support_version",
            design           : "com.android.support:design:$dependVersion.support_version",
            cardview         : "com.android.support:cardview-v7:$dependVersion.support_version",
            constraint_layout: "com.android.support.constraint:constraint-layout:1.1.0",
    ]

    retrofitLib = [
            retrofit       : "com.squareup.retrofit2:retrofit:$dependVersion.retrofit",
            retrofit_gson  : "com.squareup.retrofit2:converter-gson:$dependVersion.retrofit",
            retrofit_rxjava: "com.squareup.retrofit2:adapter-rxjava:$dependVersion.retrofit",
            retrofit_log   : "com.squareup.okhttp3:logging-interceptor:3.10.0",
    ]

    rxJavaLib = [
            rxJava       : "io.reactivex.rxjava2:rxjava:$dependVersion.rxJava",
            rxAndroid    : "io.reactivex.rxjava2:rxandroid:$dependVersion.rxAndroid",
            rxLife       : "com.trello:rxlifecycle:$dependVersion.rxLife",
            rxLife_common: "com.trello:rxlifecycle-components:$dependVersion.rxLife",
    ]

    butterknifeLib = [
            butterknife: "com.jakewharton:butterknife:$dependVersion.butterknife",
    ]
    butterknifeCompiler = "com.jakewharton:butterknife-compiler:$dependVersion.butterknife"

    supportLibs = supportLib.values()
    retrofitLibs = retrofitLib.values()
    rxJavaLibs = rxJavaLib.values()
    butterknifeLibs = butterknifeLib.values()
}
```

#### 在需要引用的地方引入对应的Libs
``` android
    // Retrofit
    implementation rootProject.ext.retrofitLibs

    // RxJava
    implementation rootProject.ext.rxJavaLibs

    // ButterKnife注解
    implementation rootProject.ext.butterknifeLibs
    annotationProcessor rootProject.ext.butterknifeCompiler
``


[网络组件NetworkClient]: https://github.com/yangsanning/AndroidFramework/blob/master/app/src/main/java/ysn/com/androidframework/network/NetworkClient.java

[依赖统一管理configs.gradle]:https://github.com/yangsanning/AndroidFramework/blob/master/configs.gradle

