package com.hl.foundation.frame.net;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * des:
 * Created by HL
 * on 2017-06-26.
 */

public class FileRequestBody extends RequestBody {

    private Object Tag;
    private long previousTime;
    protected RequestBody requestBody;
    protected UpLoadCallback callback;
    protected CountingSink countingSink;

    public FileRequestBody(RequestBody requestBody, UpLoadCallback callback) {
        this(requestBody, callback, null);
    }


    public FileRequestBody(RequestBody requestBody, UpLoadCallback callback, Object tag) {
        this.requestBody = requestBody;
        this.callback = callback;
        this.Tag = tag;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return requestBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        previousTime = System.currentTimeMillis();
        countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);

        requestBody.writeTo(bufferedSink);

        bufferedSink.flush();

    }

    public Object getTag() {
        return Tag;
    }

    public FileRequestBody setTag(Object tag) {
        Tag = tag;
        return this;
    }

    protected final class CountingSink extends ForwardingSink {

        private long bytesWritten = 0;
        long contentLength = 0L;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);

            if (contentLength == 0) {
                contentLength = contentLength();
            }
            bytesWritten += byteCount;
            if (callback != null) {
                long totalTime = (System.currentTimeMillis() - previousTime) / 1000;
                if (totalTime == 0) {
                    totalTime += 1;
                }
                long networkSpeed = bytesWritten / totalTime;
                int progress = (int) (bytesWritten * 100 / contentLength);
                boolean  complete = bytesWritten == contentLength;

                callback.onProgress(Tag == null? "": Tag,bytesWritten, progress, networkSpeed, complete);
            }
        }
    }
}
