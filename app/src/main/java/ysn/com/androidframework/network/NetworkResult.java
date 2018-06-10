package ysn.com.androidframework.network;

/**
 * 服务器返回的数据
 *
 * @param <T>
 */
public class NetworkResult<T> {

    private String errorMsg;
    private int errorCode;
    private T data;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
