package com.example.fm_design;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.fm_design.adapters.PlayerTrackPagerAdapter;
import com.example.fm_design.interfaces.IPlayCallback;
import com.example.fm_design.presenters.PlayerPresenter;
import com.example.fm_design.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP;

public class PlayerActivity extends AppCompatActivity implements IPlayCallback, ViewPager.OnPageChangeListener {

    private static final String TAG = "PlayerActivity";
    private ImageView mMControlBtn;
    private PlayerPresenter mPlayerPresenter;

    private SimpleDateFormat mMinFormat= new SimpleDateFormat("mm:ss");
    private SimpleDateFormat mHourFormat= new SimpleDateFormat("hh:mm:ss");
    private TextView mTotalDuration;
    private TextView mCurrentPosition;
    private SeekBar mDurationBar;
    private int mCurrentProgress=0;
    private  boolean mIsUserTouchProgressBar= false;
    private ImageView mPlayPre;
    private ImageView mPlayNext;
    private TextView mTrackTitleTv;
    private String mTrackTitleText;
    private ViewPager mTrackPagerView;
    private PlayerTrackPagerAdapter mTrackPagerAdapter;
    private boolean mIsUserSlidePager=false;
    private ImageView mPlayModeSwitchBtn;


    private XmPlayListControl.PlayMode mCurrentMode= PLAY_MODEL_LIST;
    private static  Map<XmPlayListControl.PlayMode,XmPlayListControl.PlayMode> sPlayModeRule=new HashMap<>();
    //歌曲播放模式
    //PLAY_MODEL_LIST        列表播放
    //PLAY_MODEL_LIST_LOOP   列表循环
    //PLAY_MODEL_SINGLE_LOOP 单曲循环播放
    //PLAY_MODEL_RANDOM      随机播放
    static{
        sPlayModeRule.put(PLAY_MODEL_LIST,PLAY_MODEL_LIST_LOOP);
        sPlayModeRule.put(PLAY_MODEL_LIST_LOOP,PLAY_MODEL_RANDOM);
        sPlayModeRule.put(PLAY_MODEL_RANDOM,PLAY_MODEL_SINGLE_LOOP);
        sPlayModeRule.put(PLAY_MODEL_SINGLE_LOOP,PLAY_MODEL_LIST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallback(this);
        //在界面初始化数据之后才去获取数据
        mPlayerPresenter.getPlayList();
        initEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unRegisterViewCallback(this);
            mPlayerPresenter=null;
        }
    }



    /**
     *设置控件的事件（要做的事）
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {
        //暂停播放按钮
    mMControlBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPlayerPresenter.isPlay()) {
                mPlayerPresenter.pause();
            }else{
                mPlayerPresenter.play();
            }
        }
    });

        //进度条
    mDurationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
            if (isFromUser) {
                mCurrentProgress=progress;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mIsUserTouchProgressBar=true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mIsUserTouchProgressBar=false;
            //手离开时更新进度条
            mPlayerPresenter.seekTo(mCurrentProgress);
        }
    });
    //上一首歌曲
        mPlayPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerPresenter.playPre();
            }
        });
    //下一首歌曲
    mPlayNext.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPlayerPresenter.playNext();
        }
    });
    //歌曲图片切换
    mTrackPagerView.addOnPageChangeListener(this);

    mTrackPagerView.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            int action=motionEvent.getAction();
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    mIsUserSlidePager=true;
                    break;
            }
            return false;
        }
    });
    //切换播放模式
        mPlayModeSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //根据当前mode来获取下一个mode
                XmPlayListControl.PlayMode playMode=sPlayModeRule.get(mCurrentMode);
                //修改播放模式
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.switchPlayMode(playMode);
                }
            }
        });
    }

    /**
     * 根据当前的状态，更新播放模式的图标3
     * PLAY_MODEL_LIST
     * PLAY_MODEL_LIST_LOOP
     * PLAY_MODEL_SINGLE_LOOP
     * PLAY_MODEL_RANDOM
     */
    private void updatePlayModeBtnImg() {
        int resId=R.drawable.selector_list_order;
        switch (mCurrentMode){
            case PLAY_MODEL_LIST:
                resId=R.drawable.selector_list_order;
                break;
            case PLAY_MODEL_LIST_LOOP:
                resId=R.drawable.selector_list_loop;
                break;
            case PLAY_MODEL_SINGLE_LOOP:
                resId=R.drawable.selector_single_loop;
                break;
            case PLAY_MODEL_RANDOM:
                resId=R.drawable.selector_random;
                break;
        }
        mPlayModeSwitchBtn.setImageResource(resId);
    }

    /**
     *找到各个控件
     */
    private void initView() {
        mMControlBtn = this.findViewById(R.id.play_or_pause_btn);
        mTotalDuration = this.findViewById(R.id.track_duration);
        mCurrentPosition = this.findViewById(R.id.current_position);
        mDurationBar = this.findViewById(R.id.track_seek_bar);
        mPlayPre = this.findViewById(R.id.play_pre);
        mPlayNext = this.findViewById(R.id.play_next);
        mTrackTitleTv = this.findViewById(R.id.track_title);
        if (!TextUtils.isEmpty(mTrackTitleText)) {
            mTrackTitleTv.setText(mTrackTitleText);
        }
        mTrackPagerView = this.findViewById(R.id.track_pager_view);
        //创建适配器
        mTrackPagerAdapter = new PlayerTrackPagerAdapter();
        //配置适配器
        mTrackPagerView.setAdapter(mTrackPagerAdapter);
        mPlayModeSwitchBtn = this.findViewById(R.id.player_mode_switch_btn);
    }


    @Override
    public void onPlayStart() {
        //开始播放，将UI修改成暂停的样式
        if (mMControlBtn != null) {
            mMControlBtn.setImageResource(R.drawable.selector_pause);
        }
    }

    @Override
    public void onPlayPause() {
        if (mMControlBtn != null) {
            mMControlBtn.setImageResource(R.drawable.selector_play);
        }
    }

    @Override
    public void onPlayStop() {
        if (mMControlBtn != null) {
            mMControlBtn.setImageResource(R.drawable.selector_play);
        }
    }

    @Override
    public void onPlayError() {

    }

    @Override
    public void nextPlay(Track track) {

    }

    @Override
    public void prePlay(Track track) {

    }

    @Override
    public void onListLoaded(List<Track> list) {
        if (mTrackPagerAdapter != null) {
            mTrackPagerAdapter.setData(list);
        }
    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {
        //更新播放模式并且修改UI
        mCurrentMode=playMode;
        updatePlayModeBtnImg();
    }

    @Override
    public void onProgressChange(int currentProgress, int total) {
        mDurationBar.setMax(total);
        String totalDuration;
        String currentPosition;
        if (total>1000*60*60) {
            totalDuration =mHourFormat.format(total);
            currentPosition =mHourFormat.format(currentProgress);
        }else {
            totalDuration=mMinFormat.format(total);
            currentPosition=mMinFormat.format(currentProgress);
        }
        //更新总进度事件
        if (mTotalDuration != null) {
            mTotalDuration.setText(totalDuration);
        }
        //更新当前时间
        if (mCurrentPosition != null) {
            mCurrentPosition.setText(currentPosition);
        }
        //更新进度条
        //计算当前进度
        if (!mIsUserTouchProgressBar) {
            mDurationBar.setProgress(currentProgress);
        }
    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFished() {

    }

    @Override
    public void onTrackUpdate(Track track, int playIndex) {
        this.mTrackTitleText=track.getTrackTitle();
        if (mTrackTitleTv != null) {
            //设置当前播放歌曲的标题
            mTrackTitleTv.setText(mTrackTitleText);
        }
        //当前的节目改变以后，要修改页面的图片
        if (mTrackPagerView != null) {
            mTrackPagerView.setCurrentItem(playIndex,true);
        }
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        LogUtil.d(TAG,"position->"+position);
        //当图片被选中时候，就切换歌曲
        if (mPlayerPresenter != null &&mIsUserSlidePager) {
            mPlayerPresenter.playByIndex(position);
        }
        mIsUserSlidePager=false;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}