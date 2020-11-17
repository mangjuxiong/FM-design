package com.example.fm_design.presenters;

import com.example.fm_design.interfaces.IRecommendPresenter;
import com.example.fm_design.interfaces.IRecommendViewCallback;
import com.example.fm_design.utils.Constants;
import com.example.fm_design.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Created by Lee on 2020/11/1.
 * Practice make perfect
 */
public class RecommendPresenter implements IRecommendPresenter {

    private static final String TAG = "RecommendPresenter";
    private  List<IRecommendViewCallback> mCallbacks=new ArrayList<>();

    private RecommendPresenter(){}

    private static RecommendPresenter sInstance=null;

    /**
     * 获取单例对象
     * @return
     */
    public static synchronized RecommendPresenter getInstance(){
        if (sInstance == null) {
            sInstance=new RecommendPresenter();
        }
        return sInstance;
    }

    @Override
    public void getRecommendList() {
        updateLoading();
        //封装参数
        Map<String, String> map = new HashMap<>();
        //设置一页数据返回多少条专辑
        map.put(DTransferConstants.LIKE_COUNT,String.valueOf(Constants.COUNT_RECOMMEND));
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    handlerRecommendResult(albumList);
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG,"error code - >"+i);
                LogUtil.d(TAG,"error Message - >"+s);
                handlerError();
            }
        });

    }

    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    private void updateLoading(){
        //等待中启动
        for (IRecommendViewCallback callback: mCallbacks) {
            callback.onLoading();
        }
    }

    private void handlerRecommendResult(List<Album> albumList) {
        //通知UI更新
        if (albumList != null) {
            //要测试内容为空时的显示课取消注释下面
            //albumList.clear();
            //取得的内容为空
            if (albumList.size()==0) {
                for (IRecommendViewCallback callback: mCallbacks) {
                    callback.onEmpty();
                }
            }else {
                //取得到内容（正常情况）
                for (IRecommendViewCallback callback: mCallbacks) {
                    callback.onRecommendListLoaded(albumList);
                }
            }
        }
    }

    private void handlerError() {
        //页面出现错误时调用
        if (mCallbacks != null) {
            for (IRecommendViewCallback callback: mCallbacks) {
                callback.onNetworkError();
            }
        }
    }


    @Override
    public void registerViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks !=null &&!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unRegisterViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null) {
            mCallbacks.remove(callback);
        }
    }
}
