package ysn.com.androidframework.network;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;
import ysn.com.androidframework.model.bean.Article;
import ysn.com.androidframework.model.bean.FileResult;
import ysn.com.androidframework.model.bean.User;
import ysn.com.androidframework.network.interceptor.BaseUrlInterceptor;
import ysn.com.androidframework.network.request.FileNetworkRequest;

/**
 * api接口
 */
public interface NetworkApiService {

    /**
     * 登陆
     *
     * @param phone    手机号
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST("/user/login")
    Observable<NetworkResult<User>> login(@Field("username") String phone, @Field("password") String password);

    /**
     * 登陆
     */
    @FormUrlEncoded
    @POST("/user/login")
    Observable<NetworkResult<User>> login(@Header(BaseUrlInterceptor.BASE_URL) String baseUrl,
                                          @Field("username") String phone, @Field("password") String password);

    /**
     * 按照作者昵称搜索文章
     *
     * @param page   页码
     * @param author 作者名字
     * @return 搜索文章
     */
    @GET("/article/list/{page}/json")
    Observable<NetworkResult<Article>> getArticleList(@Path("page") int page, @Query("author") String author);

    /**
     * 链接含有可变参数
     * https://www.wanandroid.com/ysn/jack/1
     */
    @GET("ysn{name}/{type}")
    Observable<NetworkResult<String>> getYsn(@Path("name") String name, @Path("type") String type);

    /**
     * 问号后面有多个可变参数
     * https://www.wanandroid.com/ysn?name=jack&type=1
     */
    @GET("ysn")
    Observable<NetworkResult<String>> getYsn(@Query("name") String name, @Query("type") int type);

    /**
     * 问号后面有多个可变参数, 且参数个数不定
     * https://www.wanandroid.com/ysn?name=jack&type=1&year=2020&...
     */
    @GET("ysn")
    Observable<NetworkResult<String>> getYsn(@QueryMap Map<String, Object> map);


    /** 文件
     * @see FileNetworkRequest *****************************************/

    /**
     * 上传文件
     */
    @Multipart
    @POST("ysn")
    Observable<NetworkResult<FileResult>> fileUpLoad(@Part List<MultipartBody.Part> partList);
}
