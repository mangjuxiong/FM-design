package com.example.fm_design.presenters;

import com.example.fm_design.interfaces.IAlbumDetailPresenter;
import com.example.fm_design.interfaces.IAlbumDetailViewCallback;
import com.example.fm_design.utils.Constants;
import com.example.fm_design.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lee on 2020/11/2.
 * Practice make perfect
 */
public class AlbumDetailPresenter implements IAlbumDetailPresenter {
    private static final String TAG = "AlbumDetailPresenter";
    private Album mTargetAlbum =null;
    private List<IAlbumDetailViewCallback> mCallbacks=new ArrayList<>();
    private AlbumDetailPresenter(){};

    private static AlbumDetailPresenter sInstance=null;

    /**
     * 获取单例对象
     * @return
     */
    public static synchronized AlbumDetailPresenter getInstance(){
        if (sInstance == null) {
            sInstance=new AlbumDetailPresenter();
        }
        return sInstance;
    }


    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getAlbumDetail(int AlbumId, int Page) {
        //根据id和页码获取数据
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.ALBUM_ID, AlbumId+"");
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.PAGE, Page+"");
        map.put(DTransferConstants.PAGE_SIZE, Constants.COUNT_DETAIL +"");
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
                @Override
                public void onSuccess(TrackList trackList) {
                    LogUtil.d(TAG,"thread -->"+Thread.currentThread().getName());
                    if (trackList != null) {
                        List<Track> tracks = trackList.getTracks();
                        LogUtil.d(TAG,"Tracks size -->"+tracks.size());
                        handlerAlbumDetailResult(tracks);
                    }
                }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG,"errorCode -->"+i);
                LogUtil.d(TAG,"errorMsg -->"+s);
                handlerError(i,s);
            }
        });
    }

    private void handlerError(int i, String s) {
        for (IAlbumDetailViewCallback callback : mCallbacks) {
            callback.onNetworkError(i,s);
        }
    }

    private void handlerAlbumDetailResult(List<Track> tracks) {
        for (IAlbumDetailViewCallback mCallback : mCallbacks) {
            mCallback.onDetailListLoaded(tracks);
        }
    }


    @Override
    public void registerViewCallback(IAlbumDetailViewCallback detailViewCallback) {
        if (!mCallbacks.contains(detailViewCallback)) {
            mCallbacks.add(detailViewCallback);
            if (mTargetAlbum != null) {
                detailViewCallback.onAlbumLoaded(mTargetAlbum);
            }
        }
    }

    @Override
    public void unRegisterViewCallback(IAlbumDetailViewCallback detailViewCallback) {
        mCallbacks.remove(detailViewCallback);
    }

    public  void  setTargetAlbum(Album targetAlbum){
        this.mTargetAlbum=targetAlbum;
    }
}
