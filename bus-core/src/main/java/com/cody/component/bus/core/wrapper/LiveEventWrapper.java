/*
 * ************************************************************
 * 文件：LiveEventWrapper.java  模块：bus-core  项目：component
 * 当前修改时间：2019年04月23日 18:23:19
 * 上次修改时间：2019年04月13日 08:43:55
 * 作者：Cody.yi   https://github.com/codyer
 *
 * 描述：bus-core
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.component.bus.core.wrapper;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by xu.yi. on 2019/3/31.
 * 和lifecycle绑定的事件总线
 * 每添加一个observer，LiveEventWrapper 的序列号增加1，并赋值给新加的observer，
 * 每次消息更新使用目前的序列号进行请求，持有更小的序列号才需要获取变更通知。
 * <p>
 * 解决会收到注册前发送的消息更新问题
 */
@SuppressWarnings("unused")
final public class LiveEventWrapper<T> {
    private int mSequence = 0;
    private final MutableLiveData<ValueWrapper<T>> mMutableLiveData;

    public LiveEventWrapper() {
        mMutableLiveData = new MutableLiveData<>();
    }

    public void observeForever(@NonNull final ObserverWrapper<T> observer) {
        observer.sequence = mSequence++;
        mMutableLiveData.observeForever(filterObserver(observer));
    }

    public void removeObserver(@NonNull ObserverWrapper<T> observer) {
        mMutableLiveData.removeObserver(filterObserver(observer));
    }

    public void removeObservers(@NonNull LifecycleOwner owner) {
        mMutableLiveData.removeObservers(owner);
    }

    @Nullable
    public T getValue() {
        if (mMutableLiveData.getValue() == null) {
            return null;
        }
        return mMutableLiveData.getValue().value;
    }

    public boolean hasObservers() {
        return mMutableLiveData.hasObservers();
    }

    public boolean hasActiveObservers() {
        return mMutableLiveData.hasActiveObservers();
    }

    /**
     * 设置监听之前发送的消息也可以接受到
     */
    public void observeAny(@NonNull LifecycleOwner owner, @NonNull ObserverWrapper<T> observer) {
        observer.sequence = -1;
        mMutableLiveData.observe(owner, filterObserver(observer));
    }

    /**
     * 设置监听之前发送的消息不可以接受到
     */
    public void observe(@NonNull LifecycleOwner owner, @NonNull ObserverWrapper<T> observer) {
        observer.sequence = mSequence++;
        mMutableLiveData.observe(owner, filterObserver(observer));
    }

    public void postValue(T value) {
        mMutableLiveData.postValue(new ValueWrapper<>(value, mSequence));
    }

    public void setValue(T value) {
        mMutableLiveData.setValue(new ValueWrapper<>(value, mSequence));
    }


    @NonNull
    private Observer<ValueWrapper<T>> filterObserver(@NonNull final ObserverWrapper<T> observerWrapper) {
        if (observerWrapper.observer != null) {
            return observerWrapper.observer;
        }
        return observerWrapper.observer = new Observer<ValueWrapper<T>>() {
            @Override
            public void onChanged(@Nullable ValueWrapper<T> valueWrapper) {
                if (valueWrapper != null && valueWrapper.sequence > observerWrapper.sequence) {
                    observerWrapper.onChanged(valueWrapper.value);
                }
            }
        };
    }
}
