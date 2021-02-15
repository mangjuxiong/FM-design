package com.example.fm_design.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fm_design.R;
import com.example.fm_design.adapters.PlayListAdapter;
import com.example.fm_design.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class SobPopWindow extends PopupWindow {

    private final View mPopView;
    private TextView mPlayListCloseBtn;
    private RecyclerView mPlayListRv;
    private PlayListAdapter mAdapter;

    public SobPopWindow(){
        //设置宽高
        super();
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        //加载view
        mPopView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.pop_play_list,null);
        //设置内容
        setContentView(mPopView);
        //设置进出场动画
        setAnimationStyle(R.style.pop_animation);
        initView();
        initEvent();
    }

    private void initView() {
        mPlayListCloseBtn = mPopView.findViewById(R.id.play_list_close_btn);
        mPlayListRv = mPopView.findViewById(R.id.play_list_rv);
        mAdapter = new PlayListAdapter();
        mPlayListRv.setLayoutManager(new LinearLayoutManager(BaseApplication.getAppContext()));
        mPlayListRv.setAdapter(mAdapter);
    }

    private void initEvent() {
        mPlayListCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void setListData(List<Track> list){
        if (mAdapter != null) {
            mAdapter.setData(list);
        }
    }
}
