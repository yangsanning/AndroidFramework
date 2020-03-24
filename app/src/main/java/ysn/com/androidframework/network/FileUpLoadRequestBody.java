package ysn.com.androidframework.network;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * @Author yangsanning
 * @ClassName FileUpLoadRequestBody
 * @Description 一句话概括作用
 * @Date 2020/3/24
 * @History 2020/3/24 author: description:
 */
public class FileUpLoadRequestBody extends RequestBody {

    private RequestBody requestBody;
    private BaseFileUpLoadCallback fileUpLoadCallback;

    public FileUpLoadRequestBody(File file, BaseFileUpLoadCallback fileUpLoadCallback) {
        this.requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        this.fileUpLoadCallback = fileUpLoadCallback;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        CountingSink countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        // 写入
        requestBody.writeTo(bufferedSink);

        // 必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    protected final class CountingSink extends ForwardingSink {

        private long bytesWritten = 0;
        private Handler handler;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(@NotNull Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);

            bytesWritten += byteCount;
            if (fileUpLoadCallback != null) {
                if (handler == null || handler.getLooper() != Looper.getMainLooper()) {
                    handler = new Handler(Looper.getMainLooper());
                }
                handler.post(() -> {
                    try {
                        fileUpLoadCallback.onProgressChange(bytesWritten, contentLength());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    public abstract class BaseFileUpLoadCallback<T> extends NetworkCallback<T> {

        /**
         * 监听进度的改变
         *
         * @param bytesWritten  当前大小
         * @param contentLength 总大小
         */
        public void onProgressChange(long bytesWritten, long contentLength) {
            onProgress((int) (bytesWritten * 100 / contentLength));
        }

        /**
         * 上传进度回调
         */
        public abstract void onProgress(int progress);
    }
}
