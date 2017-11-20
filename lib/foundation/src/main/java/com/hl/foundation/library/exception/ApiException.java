package com.hl.foundation.library.exception;

/**
 * des:
 * Created by HL
 * on 2017-05-02.
 */

public class ApiException extends RuntimeException{


    private int code;
    private String message;


    public ApiException(int code,String message) {
        this.code = code;
        this.message = message;
    }


    public int getCode() {

        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
