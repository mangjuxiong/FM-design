package com.example.fm_design;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fm_design.adapters.DetailListAdapter;
import com.example.fm_design.base.BaseActivity;
import com.example.fm_design.interfaces.IAlbumDetailViewCallback;
import com.example.fm_design.presenters.AlbumDetailPresenter;
import com.example.fm_design.presenters.PlayerPresenter;
import com.example.fm_design.utils.LogUtil;
import com.example.fm_design.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback, UILoader.OnRetryClickListener, DetailListAdapter.ItemClickListener {

    private static final String TAG = "DetailActivity";
    private int mCurrentPage=1;
    private ImageView mLargeCover;
    private ImageView mSmallCover;
    private TextView mAlbumTitle;
    private TextView mAlbumAuthor;
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private RecyclerView mDetailList;
    private DetailListAdapter mDetailListAdapter;
    private FrameLayout mDetailListContainer;
    private UILoader mUiLoader;
    private long mCurrentId=-1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //将顶部栏隐藏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initView();
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewCallback(this);
    }


    private void initView() {

        mDetailListContainer = this.findViewById(R.id.detail_list_container);
        if (mUiLoader == null) {
            mUiLoader = new UILoader(this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView(container);
                }
            };
            mDetailListContainer.removeAllViews();
            mDetailListContainer.addView(mUiLoader);
            mUiLoader.setOnRetryClickListener(DetailActivity.this);
        }
        mLargeCover = this.findViewById(R.id.iv_large_cover);
        mSmallCover = this.findViewById(R.id.iv_small_cover);
        mAlbumTitle = this.findViewById(R.id.tv_album_title);
        mAlbumAuthor = this.findViewById(R.id.tv_album_author);


    }

    private View createSuccessView(ViewGroup container) {
        View detailListView= LayoutInflater.from(this).inflate(R.layout.item_detail_list,container,false);
        mDetailList = detailListView.findViewById(R.id.album_detail_list);
        //recycler view的使用步骤
        //1.设置布局管理器
        LinearLayoutManager LayoutManager=new LinearLayoutManager(this);
        mDetailList.setLayoutManager(LayoutManager);
        //2.设置适配器
        mDetailListAdapter = new DetailListAdapter();
        mDetailList.setAdapter(mDetailListAdapter);
        //设置间距
        mDetailList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom= UIUtil.dip2px(view.getContext(),1);
                outRect.top= UIUtil.dip2px(view.getContext(),1);
                outRect.left= UIUtil.dip2px(view.getContext(),2);
                outRect.right= UIUtil.dip2px(view.getContext(),2);

            }
        });
        mDetailListAdapter.setItemClickListener(this);
        return detailListView;
    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        //根据数据判断结果去显示ui
        if (tracks.size()==0 ||tracks == null) {
            if (mUiLoader != null) {
                mUiLoader.upDataStatus(UILoader.UIStatus.EMPTY);
            }
        }
        if (mUiLoader != null) {
        mUiLoader.upDataStatus(UILoader.UIStatus.SUCCESS);
    }
        mDetailListAdapter.setData(tracks);
}

    @Override
    public void onNetworkError(int i, String s) {
        //请求发生错误，显示网络错误状态页面
        mUiLoader.upDataStatus(UILoader.UIStatus.NETWORK_ERROR);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onAlbumLoaded(Album album) {

        long  id=album.getId();
        LogUtil.d(TAG,"album "+id);
        mCurrentId=id;
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.getAlbumDetail((int)id ,mCurrentPage);
        }
        //拿数据，显示UILoading状态
        if (mUiLoader != null) {
            mUiLoader.upDataStatus(UILoader.UIStatus.LOADING);
        }
        if (mAlbumTitle != null) {
            mAlbumTitle.setText(album.getAlbumTitle());
        }
        if (mAlbumAuthor != null) {
            mAlbumAuthor.setText(album.getAnnouncer().getNickname());
        }
        if (mLargeCover != null) {
            Glide.with(this)
                    .load(album.getCoverUrlLarge())
                    //毛玻璃效果
                    .apply(bitmapTransform(new BlurTransformation(25)))
                    .into(mLargeCover);
        }
        if (mSmallCover != null) {
            Glide.with(this).load(album.getCoverUrlSmall()).into(mSmallCover);
        }
    }

    @Override
    public void onRetryClick() {
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.getAlbumDetail((int)mCurrentId ,mCurrentPage);
            LogUtil.d(TAG,"mCurrentId "+mCurrentId);
        }
    }

    @Override
    public void onItemClick(List<Track> detailData, int position) {
        PlayerPresenter playerPresenter = PlayerPresenter.getPlayerPresenter();
        playerPresenter.setPlayList(detailData,position);
        Intent intent=new Intent(DetailActivity.this,PlayerActivity.class);
        startActivity(intent);
    }
}