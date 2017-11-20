package com.hl.foundation.frame.net;


public interface UpLoadCallback {
    void onProgress(Object tag, long bytesWritten, int progress, long speed, boolean done);
}
