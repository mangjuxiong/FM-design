package com.example.fm_design.interfaces;

import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;
/**
 * Created by Lee on 2020/11/4.
 * Practice make perfect
 */
public interface IPlayCallback  {

    /**
     * 开始播放
     */
    void onPlayStart();

    /**
     * 播放暂停
     */
    void onPlayPause();

    /**
     * 停止播放
     */
    void onPlayStop();

    /**
     * 播放错误
     */
    void onPlayError();

    /**
     * 播放下一首
     * @param track
     */
    void nextPlay(Track track);

    /**
     * 播放上一首
     * @param track
     */
    void prePlay(Track track);

    /**
     * 播放列表数据加载完成
     * @param list 播放器列表数据
     */
    void onListLoaded(List<Track> list);

    /**
     * 播放器模式切换
     * @param playMode
     */
    void onPlayModeChange(XmPlayListControl.PlayMode playMode);

    /**
     * 播放进度条改变
     * @param currentProgress
     * @param total
     */
    void onProgressChange(int currentProgress,int total);

    /**
     * 加载广告
     */
    void onAdLoading();

    /**
     * 广告结束
     */
    void onAdFished();

    /**
     * 更新当前节目
     * @param track 节目
     */
    void onTrackUpdate(Track track,int playIndex);
}
