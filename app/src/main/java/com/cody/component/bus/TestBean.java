/*
 * ************************************************************
 * 文件：TestBean.java  模块：app  项目：component
 * 当前修改时间：2019年04月04日 18:43:04
 * 上次修改时间：2019年04月03日 19:42:04
 * 作者：Cody.yi   https://github.com/codyer
 *
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.component.bus;

/**
 * Created by xu.yi. on 2019/4/3.
 * LiveEventBus
 */
public class TestBean {
    private String name;
    private String code;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TestBean{");
        sb.append("name='").append(name).append('\'');
        sb.append(", code='").append(code).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public TestBean(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
