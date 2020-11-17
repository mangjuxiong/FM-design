package com.example.fm_design.interfaces;

import com.example.fm_design.base.IBasePresenter;

/**
 * Created by Lee on 2020/11/2.
 * Practice make perfect
 */
public interface IAlbumDetailPresenter extends IBasePresenter<IAlbumDetailViewCallback> {
    /**
     * 下拉刷新更多内容
     */
    void pull2RefreshMore();

    /**
     * 上拉加载更多
     */
    void loadMore();

    /**
     * 获取专辑详情
     * @param AlbumId
     * @param Page
     */
    void getAlbumDetail(int AlbumId,int Page);

}
