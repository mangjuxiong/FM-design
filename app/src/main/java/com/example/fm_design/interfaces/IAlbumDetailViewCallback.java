package com.example.fm_design.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import  java.util.List;
/**
 * Created by Lee on 2020/11/2.
 * Practice make perfect
 */
public interface IAlbumDetailViewCallback {

    /**
     * 详情页面加载方法
     * @param tracks
     */
    void onDetailListLoaded(List<Track> tracks);


    /**
     * 网络错误
     */
    void onNetworkError(int i, String s);

    void onAlbumLoaded(Album album);
}
