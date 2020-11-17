package com.example.fm_design.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fm_design.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Lee on 2020/11/3.
 * Practice make perfect
 */
public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.InnerHolder> {

    private static final String TAG = "DetailListAdapter";
    private  List<Track> mDetailData= new ArrayList<>();
    ///格式化时间
    private SimpleDateFormat mUpdateDateFormat=new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mDurationFormat=new SimpleDateFormat("mm:ss");
    private ItemClickListener mItemClickListener =null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_detail, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, final int position) {
        final View itemView=holder.itemView;
        //顺序Id
        TextView orderView=itemView.findViewById(R.id.order_title);
        //标题
        TextView  titleTv=itemView.findViewById(R.id.detail_item_title);
        //播放次数
        TextView playCountTv=itemView.findViewById(R.id.detail_item_play_count);
        //播放时长
        TextView durationTv=itemView.findViewById(R.id.detail_item_duration);
        //更新日期
        TextView updateDateTv=itemView.findViewById(R.id.detail_item_update_time);

        //设置数据
        Track track=mDetailData.get(position);
        orderView.setText(position+1+"");
        titleTv.setText(track.getTrackTitle());
        playCountTv.setText(track.getPlayCount()+"");
        //将输出的时间格式化成：分钟/秒钟
        int i = track.getDuration() * 1000;
        String durationTimeText=mDurationFormat.format(i);
        durationTv.setText(durationTimeText);
        //将输出的时间格式化成：年/月/日
        String updateTimeText= mUpdateDateFormat.format(track.getUpdatedAt());
        updateDateTv.setText(updateTimeText);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(mDetailData,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDetailData.size();
    }

    public void setData(List<Track> tracks) {
        //清除原来的数据
        mDetailData.clear();
        //添加新的数据
        mDetailData.addAll(tracks);
        //更新UI
        notifyDataSetChanged();
    }


    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setItemClickListener(ItemClickListener listener){
        this.mItemClickListener=listener;
    }
    public interface ItemClickListener{
        void onItemClick(List<Track> detailData, int position);
    }
}
