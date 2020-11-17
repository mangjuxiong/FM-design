package com.example.fm_design.interfaces;

import com.example.fm_design.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

/**
 * Created by Lee on 2020/11/4.
 * Practice make perfect
 */
public interface IPlayPresenter extends IBasePresenter<IPlayCallback> {
    /**
     *播放
     */
    void play();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 暂停
     */
    void pause();

    /**
     * 播放上一首
     */
    void playPre();

    /**
     * 播放下一首
     */
    void playNext();

    /**
     * 切换播放模式（单曲循环，随机播放等）
     * @param mode
     */
    void switchPlayMode(XmPlayListControl.PlayMode mode);

    /**
     * 获取播放列表
     */
    void getPlayList();

    /**
     * 根据节目的位置进行播放
     * @param index  节目在列表的位置
     */
    void playByIndex(int index);


    /**
     * 切换播放位置
     * @param progress
     */
    void seekTo(int progress);

    boolean isPlay();
 }
