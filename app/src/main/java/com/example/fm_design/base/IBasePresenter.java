package com.example.fm_design.base;

/**
 * Created by Lee on 2020/11/4.
 * Practice make perfect
 */
public interface IBasePresenter<T> {
    /**
     * 注册回调接口
     * @param t
     */
    void registerViewCallback(T t);

    /**
     * 取消注册回调接口
     * @param t
     */
    void unRegisterViewCallback(T t);
}
