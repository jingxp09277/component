/*
 * ************************************************************
 * 文件：BooleanLiveData.java  模块：lib-core  项目：component
 * 当前修改时间：2019年04月15日 22:51:24
 * 上次修改时间：2019年04月15日 22:51:23
 * 作者：Cody.yi   https://github.com/codyer
 *
 * 描述：lib-core
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.component.lib.safe;

/**
 * Created by xu.yi. on 2019/4/15.
 * component
 */
public class BooleanLiveData extends SafeMutableLiveData<Boolean> {
    public BooleanLiveData(final Boolean value) {
        super(value);
    }

    public void negation() {
        Boolean old = getValue();
        if (old != null) {
            postValue(!old);
        }
    }
}
