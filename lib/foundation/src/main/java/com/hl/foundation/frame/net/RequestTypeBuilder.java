package com.hl.foundation.frame.net;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * des: 请求类型创建
 * Created by HL
 * on 2017-06-26.
 */

public class RequestTypeBuilder {


    public static RequestBody createRequestBody(File file){

//        RequestBody requestFile = RequestBody.create(null,file);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/mp3"), file);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

//        RequestBody requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        return requestFile;
    }

    public static RequestBody createRequestBody(String description){

        RequestBody requestDescription = RequestBody.create(MediaType.parse("multipart/form-data"), description);
        return requestDescription;
    }

    public static MultipartBody.Part createMultipartBodyPart(String name, String filename, RequestBody body){

        MultipartBody.Part part = MultipartBody.Part.createFormData("file", filename, body);
        return part;
    }

    public static FileRequestBody createFileRequestBody(Object tag,RequestBody requestBody, UpLoadCallback callback){

        return new FileRequestBody(requestBody,callback,tag);
    }
}
