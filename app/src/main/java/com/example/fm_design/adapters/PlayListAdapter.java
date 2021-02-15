package com.example.fm_design.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fm_design.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {
    private  List<Track> mData=new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.play_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Track track=mData.get(position);
        holder.mTrackTitleTv.setText(track.getTrackTitle());
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }else{
            return 0;
        }
    }

    public  void setData(List<Track> list){
        if (list != null) {
            mData.clear();
            mData.addAll(list);
            notifyDataSetChanged();
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTrackTitleTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTrackTitleTv = itemView.findViewById(R.id.track_title_tv);
        }
    }
}
