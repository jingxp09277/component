/*
 * ************************************************************
 * 文件：BaseBindActivity.java  模块：bind-ui  项目：component
 * 当前修改时间：2019年04月05日 17:32:51
 * 上次修改时间：2019年04月04日 13:35:31
 * 作者：Cody.yi   https://github.com/codyer
 *
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.component.base.app.activity;

import android.os.Bundle;
import android.view.View;
import com.cody.component.base.BR;

import com.cody.component.base.app.IBinding;
import com.cody.component.view.data.IViewData;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Created by xu.yi. on 2019/3/25.
 * dataBinding 基类
 */
public abstract class BaseBindActivity<B extends ViewDataBinding> extends BaseActivity implements IBinding, View.OnClickListener {
    private B mBinding;

    /**
     * 使用如下函数添加binding
     *
     * @see #bindViewData(int, IViewData)
     */
    protected abstract void bindViewData();

    /**
     * 子类提供有binding的资源ID
     */
    @LayoutRes
    protected abstract int getLayoutID();

    /**
     * 子Activity逻辑重载这个函数
     */
    @CallSuper
    @Override
    public void onBaseReady(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(this, getLayoutID());
        if (mBinding != null) {
            mBinding.setLifecycleOwner(this);
            mBinding.setVariable(BR.onClickListener, this);
            bindViewData();
        } else {
            setContentView(getLayoutID());
        }
    }

    /**
     * @param variableId the BR id of the variable to be set. For example, if the variable is
     *                   <code>x</code>, then variableId will be <code>BR.x</code>.
     * @param viewData   The new viewData of the variable to be set.
     */
    public void bindViewData(@IdRes int variableId, @Nullable IViewData viewData) {
        if (mBinding != null) {
            mBinding.setVariable(variableId, viewData);
        }
    }

    /**
     * 是否已经设置bind
     */
    @Override
    public boolean isBound() {
        return mBinding != null;
    }

    @Override
    public B getBinding() {
        if (mBinding == null) {
            throw new NullPointerException("You should bindViewData first!");
        }
        return mBinding;
    }
}