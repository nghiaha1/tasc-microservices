package com.tasc.model;

import lombok.Data;

@Data
public class BaseResponse<T> {

    private int code;

    private String message;

    private T data;

    public BaseResponse(){
        this.code = ERROR.SUCCESS.code;
        this.message = "SUCCESS";
    }

    public BaseResponse(String message) {
        this.code = ERROR.SUCCESS.code;
        this.message = message;
    }

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse(T data) {
        this.code = ERROR.SUCCESS.code;
        this.message = "SUCCESS";
        this.data = data;
    }
}
