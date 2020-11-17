package com.example.fm_design.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.fm_design.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by Lee on 2020/11/5.
 * Practice make perfect
 */
public class PlayerTrackPagerAdapter extends PagerAdapter {

    private List<Track> mData=new ArrayList<>();
    @Override
    public int getCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView= LayoutInflater.from(container.getContext()).inflate(R.layout.item_track_pager,container,false);
        container.addView(itemView);
        //设置数据
        //找到控件
        ImageView item =itemView.findViewById(R.id.track_pager_item);
        //设置数据
        Track track=mData.get(position);
        String coverUrlLarge=track.getCoverUrlLarge();
        Glide.with(container.getContext()).load(coverUrlLarge).into(item);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    public void setData(List<Track> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }
}
