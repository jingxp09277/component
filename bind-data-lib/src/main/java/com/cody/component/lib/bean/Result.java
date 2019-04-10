/*
 * ************************************************************
 * 文件：Result.java  模块：http-core  项目：component
 * 当前修改时间：2019年04月06日 01:12:01
 * 上次修改时间：2019年03月25日 09:23:00
 * 作者：Cody.yi   https://github.com/codyer
 *
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.component.lib.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xu.yi. on 2019/4/6.
 *
 */
public class Result<T> {

    @SerializedName("code")
    private int code;

    @SerializedName(value = "message", alternate = {"message", "msg"})
    private String message;

    @SerializedName(value = "data", alternate = {"data", "dataMap"})
    private T data;

    public Result() {
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}