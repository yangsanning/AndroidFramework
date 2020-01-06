package ysn.com.androidframework.network;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;
import ysn.com.androidframework.model.bean.User;

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
}
