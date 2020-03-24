package ysn.com.androidframework.network.request;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import ysn.com.androidframework.model.annotation.Required;
import ysn.com.androidframework.model.bean.FileResult;
import ysn.com.androidframework.network.BaseNetworkRequest;
import ysn.com.androidframework.network.FileUpLoadRequestBody;
import ysn.com.androidframework.network.NetworkClient;

/**
 * @Author yangsanning
 * @ClassName FileNetworkRequest
 * @Description 一句话概括作用
 * @Date 2020/3/24
 * @History 2020/3/24 author: description:
 */
public class FileNetworkRequest extends BaseNetworkRequest {

    private static FileNetworkRequest instance;

    public static FileNetworkRequest get() {
        if (instance == null) {
            synchronized (FileNetworkRequest.class) {
                if (instance == null) {
                    instance = new FileNetworkRequest();
                }
            }
        }
        return instance;
    }

    /**
     * 上传图片
     */
    public void fileUpLoad(@Required List<String> imagePathList, Subscriber<FileResult> subscriber) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                // 表单类型
                .setType(MultipartBody.FORM)
                // 添加参数
                .addFormDataPart("uploadUser", "uploadUser");
        for (String imagePath : imagePathList) {
            File file = new File(imagePath);
            RequestBody fileUpLoadRequestBody = new FileUpLoadRequestBody(file, null);
            builder.addFormDataPart("uploadFile", file.getName(), fileUpLoadRequestBody);
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable<FileResult> observable = NetworkClient.getInstance().mService
                .fileUpLoad(parts)
                .map(new NetworkResultFun<>());
        toSubscribe(observable, subscriber);
    }
}
