package com.example.fm_design.interfaces;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * 通知ui
 */
public interface IRecommendViewCallback {

    /**
     * 获取推荐内容的结果
     * @param result
     */
    void onRecommendListLoaded(List<Album> result);

    /**
     * 网络错误
     */
    void onNetworkError();

    /**
     * 内容为空
     */
    void onEmpty();

    /**
     * 内容加载中
     */
    void onLoading();



}
