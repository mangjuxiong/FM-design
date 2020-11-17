package com.example.fm_design.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.fm_design.base.BaseApplication;
import com.example.fm_design.interfaces.IPlayCallback;
import com.example.fm_design.interfaces.IPlayPresenter;
import com.example.fm_design.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP;

/**
 * Created by Lee on 2020/11/4.
 * Practice make perfect
 */
public class PlayerPresenter implements IPlayPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {


    private List<IPlayCallback> mIPlayCallbacks =new ArrayList<>();
    private static final String TAG = "PlayerPresenter";
    private final XmPlayerManager mPlayerManager;
    private Track mCurrentTrack;
    private int mCurrentIndex=0;
    private final SharedPreferences mPlayModSP;

//    PLAY_MODEL_LIST
//    PLAY_MODEL_LIST_LOOP
//    PLAY_MODEL_SINGLE_LOOP
//    PLAY_MODEL_RANDOM
    public static final int PLAY_MODEL_LIST_INT=0;
    public static final int PLAY_MODEL_LIST_LOOP_INT=1;
    public static final int PLAY_MODEL_SINGLE_LOOP_INT=2;
    public static final int PLAY_MODEL_RANDOM_INT=3;
    //SP的key和name
    public static final String PLAY_MODE_SP_NAME="playMod";
    public static final String PLAY_MODE_SP_KEY="currentPlayMode";
    private XmPlayListControl.PlayMode mCurrentPlayMode= PLAY_MODEL_LIST;

    /**
     * 单例模式
     */
    private PlayerPresenter(){
        mPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
        //广告相关的接口
        mPlayerManager.addAdsStatusListener(this);
        //注册播放器状态相关的接口
        mPlayerManager.addPlayerStatusListener(this);
        mPlayModSP = BaseApplication.getAppContext().getSharedPreferences(PLAY_MODE_SP_NAME, Context.MODE_PRIVATE);
    };

    private static PlayerPresenter sPlayerPresenter=null;


    public static synchronized PlayerPresenter getPlayerPresenter(){
        if (sPlayerPresenter == null) {
            sPlayerPresenter=new PlayerPresenter();
        }
        return sPlayerPresenter;
    }


    private boolean isPlayListSet=false;
    public void setPlayList(List<Track> list, int playIndex){
        if (mPlayerManager != null) {
            mPlayerManager.setPlayList(list,playIndex);
            isPlayListSet=true;
            mCurrentTrack = list.get(playIndex);
            mCurrentIndex=playIndex;
        }else{
            LogUtil.d(TAG,"mPlayerManager IS NULL");
        }
    }

    @Override
    public void play() {
        if (isPlayListSet) {
            mPlayerManager.play();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void pause() {
        if (mPlayerManager != null) {
            mPlayerManager.pause();
        }
    }

    @Override
    public void playPre() {
        if (mPlayerManager != null) {
            mPlayerManager.playPre();
        }
    }

    @Override
    public void playNext() {
        if (mPlayerManager != null) {
            mPlayerManager.playNext();
        }
    }

    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {
        if (mPlayerManager != null) {
            mCurrentPlayMode=mode;
            mPlayerManager.setPlayMode(mode);
            //通知ui更新播放模式
            for (IPlayCallback iPlayCallback : mIPlayCallbacks) {
                iPlayCallback.onPlayModeChange(mode);
            }
            //保存到SP里
            SharedPreferences.Editor edit = mPlayModSP.edit();
            edit.putInt(PLAY_MODE_SP_KEY,getIntByPlayMode(mode));
            edit.commit();
        }
    }

    private int getIntByPlayMode(XmPlayListControl.PlayMode mode){
        switch (mode){
            case PLAY_MODEL_LIST_LOOP:
                return PLAY_MODEL_LIST_LOOP_INT;
            case PLAY_MODEL_SINGLE_LOOP:
                return PLAY_MODEL_SINGLE_LOOP_INT;
            case PLAY_MODEL_RANDOM:
                return PLAY_MODEL_RANDOM_INT;
            case PLAY_MODEL_LIST:
                return PLAY_MODEL_LIST_INT;
        }
        return PLAY_MODEL_LIST_INT;
    }
    private XmPlayListControl.PlayMode  getModeByInt(int index){
        switch (index){
            case PLAY_MODEL_LIST_LOOP_INT:
                return PLAY_MODEL_LIST_LOOP;
            case PLAY_MODEL_SINGLE_LOOP_INT:
                return PLAY_MODEL_SINGLE_LOOP;
            case PLAY_MODEL_RANDOM_INT:
                return PLAY_MODEL_RANDOM;
            case PLAY_MODEL_LIST_INT:
                return PLAY_MODEL_LIST;
        }
        return PLAY_MODEL_LIST;
    }
    @Override
    public void getPlayList() {
        if (mPlayerManager != null) {
            List<Track> playList = mPlayerManager.getPlayList();
            for (IPlayCallback iPlayCallback : mIPlayCallbacks) {
                iPlayCallback.onListLoaded(playList);
            }
        }

    }

    @Override
    public void playByIndex(int index) {
        //切换播放器到底Index个位置
        if (mPlayerManager != null) {
            mPlayerManager.play(index);
        }
    }

    @Override
    public void seekTo(int progress) {
        mPlayerManager.seekTo(progress);
    }

    @Override
    public boolean isPlay() {
        return  mPlayerManager.isPlaying();
    }

    @Override
    public void registerViewCallback(IPlayCallback iPlayCallback) {
        iPlayCallback.onTrackUpdate(mCurrentTrack,mCurrentIndex);
        //从SP里面拿模式数据
        int modeIndex = mPlayModSP.getInt(PLAY_MODE_SP_KEY, PLAY_MODEL_LIST_INT);
        mCurrentPlayMode=getModeByInt(modeIndex);
        iPlayCallback.onPlayModeChange(mCurrentPlayMode);
        if (!mIPlayCallbacks.contains(iPlayCallback)) {
            mIPlayCallbacks.add(iPlayCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(IPlayCallback iPlayCallback) {
        mIPlayCallbacks.remove(iPlayCallback);

    }




    //--------------------------------------关于广告相关的回调  START-----------------------------//
    @Override
    public void onStartGetAdsInfo() {
        Log.d(TAG,"onStartGetAdsInfo");
    }

    @Override
    public void onGetAdsInfo(AdvertisList advertisList) {
        Log.d(TAG,"onGetAdsInfo");
    }

    @Override
    public void onAdsStartBuffering() {
        Log.d(TAG,"onAdsStartBuffering");
    }

    @Override
    public void onAdsStopBuffering() {
        Log.d(TAG,"onAdsStopBuffering");
    }

    @Override
    public void onStartPlayAds(Advertis advertis, int i) {
        Log.d(TAG,"onStartPlayAds");
    }

    @Override
    public void onCompletePlayAds() {
        Log.d(TAG,"onCompletePlayAds");
    }

    @Override
    public void onError(int what, int extra) {
        Log.d(TAG,"what -->"+what+" intra -->"+extra);
    }
    //--------------------------------------关于广告相关的回调  END-----------------------------//


    //---------------------------------------播放器回调的接口 START-----------------------//
    @Override
    public void onPlayStart() {
        LogUtil.d(TAG,"onPlayStart");
        for (IPlayCallback iPlayCallback : mIPlayCallbacks) {
            iPlayCallback.onPlayStart();
        }
    }

    @Override
    public void onPlayPause() {
        LogUtil.d(TAG,"onPlayPause");
        for (IPlayCallback iPlayCallback : mIPlayCallbacks) {
            iPlayCallback.onPlayPause();
        }
    }

    @Override
    public void onPlayStop() {
        LogUtil.d(TAG,"onPlayStop");
        for (IPlayCallback iPlayCallback : mIPlayCallbacks) {
            iPlayCallback.onPlayStop();
        }
    }


    @Override
    public void onSoundPlayComplete() {
        LogUtil.d(TAG,"onSoundPlayComplete");
    }

    @Override
    public void onSoundPrepared() {
        LogUtil.d(TAG,"onSoundPrepared");
        mPlayerManager.setPlayMode(mCurrentPlayMode);
        if (mPlayerManager.getPlayerStatus()== PlayerConstants.STATE_PREPARED) {
            mPlayerManager.play();
        }
    }

    @Override
    //curModel表示当前播放的内容
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel){
        LogUtil.d(TAG,"onSoundSwitch");
        //显示当前播放的歌曲信息
        mCurrentIndex=mPlayerManager.getCurrentIndex();
        if (curModel instanceof Track) {
            Track currentTrack= (Track) curModel;
            mCurrentTrack=currentTrack;
            //LogUtil.d(TAG,"title --> "+currentTrack.getTrackTitle());
            //更新UI
            for (IPlayCallback iPlayCallback : mIPlayCallbacks) {
                iPlayCallback.onTrackUpdate(mCurrentTrack,mCurrentIndex);
            }
        }
    }

    @Override
    public void onBufferingStart() {
        LogUtil.d(TAG,"onBufferingStart");
    }

    @Override
    public void onBufferingStop() {
        LogUtil.d(TAG,"onBufferingStop");
    }

    @Override
    public void onBufferProgress(int progress) {

    }

    @Override
    public void onPlayProgress(int currPos, int duration) {
        for (IPlayCallback iPlayCallback : mIPlayCallbacks) {
            iPlayCallback.onProgressChange(currPos,duration);
        }
    }

    @Override
    public boolean onError(XmPlayerException e) {
        LogUtil.d(TAG,"onError--->"+e);
        return false;
    }
    //---------------------------------------播放器回调的接口 START-----------------------//


}
