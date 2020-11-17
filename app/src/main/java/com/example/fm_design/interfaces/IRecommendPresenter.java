package com.example.fm_design.interfaces;

import com.example.fm_design.base.IBasePresenter;

/**
 * 这个是用来获取内容的代码
 */
public interface IRecommendPresenter extends IBasePresenter<IRecommendViewCallback> {


    /**
     * 获取推荐内容
     */

    void getRecommendList() ;
    /**
     * 下拉刷新更多内容
     */
    void pull2RefreshMore();

    /**
     * 上拉加载更多
     */
    void loadMore();

}
