package ysn.com.androidframework.network;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import ysn.com.androidframework.constant.UrlConstant;
import ysn.com.androidframework.model.bean.User;
import ysn.com.androidframework.network.NetworkResult;

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
    @POST(UrlConstant.URL_LOGIN)
    Observable<NetworkResult<User>> login(@Field("username") String phone, @Field("password") String password);
}
