/*
 * ************************************************************
 * 文件：NoCatCreatedException.java  模块：http-cat  项目：component
 * 当前修改时间：2019年04月23日 18:23:19
 * 上次修改时间：2019年04月13日 08:43:54
 * 作者：Cody.yi   https://github.com/codyer
 *
 * 描述：http-cat
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.http.cat.exception;

/**
 * Created by xu.yi. on 2019/4/7.
 * component
 */
public class NoCatCreatedException extends RuntimeException {
    private static final long serialVersionUID = -4749478194441091368L;

    public NoCatCreatedException() {
        super("使用实例必须先调用createCat");
    }
}
