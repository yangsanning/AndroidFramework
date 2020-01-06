package ysn.com.androidframework.network;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
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
     * 链接含有可变参数时的书写列子
     * https://www.wanandroid.com/ysn/jack/1
     */
    @GET("ysn{name}/{type}")
    Observable<NetworkResult<String>> getYsn(@Path("name") String name, @Path("type") String type);
}
