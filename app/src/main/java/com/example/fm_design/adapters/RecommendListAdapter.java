package com.example.fm_design.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fm_design.R;
import com.example.fm_design.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class RecommendListAdapter  extends RecyclerView.Adapter<RecommendListAdapter.InnerHolder> {

    private static final String TAG = "RecommendListAdapter";
    private  List<Album> mData=new ArrayList<>();
    private OnRecommendItemClickListener mItemClickListener =null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //设置View
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend,parent,false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final InnerHolder holder, final int position) {
        //设置数据
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    int clickPosition=(int) v.getTag();
                    mItemClickListener.onItemClick(clickPosition,mData.get(clickPosition));
                }
                LogUtil.d(TAG,"you click the "+v.getTag());
            }
        });
        holder.setData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        //返回要显示的个数
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public void setData(List<Album> albumList) {
        if (mData != null) {
            mData.clear();
            mData.addAll(albumList);
        }
        //更新ui
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Album album) {
            //在这里找控件并设置数据
            //专辑封面
            ImageView albumCoverTv=itemView.findViewById(R.id.album_cover);
            //专辑标题
            TextView albumTitleTv=itemView.findViewById(R.id.album_title_tv);
            //专辑内容简介
            TextView albumDesTV=itemView.findViewById(R.id.album_description_tv);
            //播放量
            TextView albumPlayCountTv=itemView.findViewById(R.id.album_play_count);
            //专辑集数
            TextView albumContentCountTV=itemView.findViewById(R.id.album_content_size);

            albumTitleTv.setText(album.getAlbumTitle());
            albumDesTV.setText(album.getAlbumIntro());
            albumPlayCountTv.setText(album.getPlayCount()+"");
            albumContentCountTV.setText(album.getIncludeTrackCount()+"");

            Glide.with(itemView.getContext()).load(album.getCoverUrlLarge()).into(albumCoverTv);
        }
    }

    public void setOnRecommendItemClickListener(OnRecommendItemClickListener listener){
        this.mItemClickListener= listener;
    }

    public  interface  OnRecommendItemClickListener{
        void onItemClick(int position,Album album);
    }
}
