package com.example.simplemusicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.LocalMusicViewHolder> {
    Context context;
    List<localMusicBean>mDatas;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void OnItemClick(View view,int position);
    }
    public LocalMusicAdapter(Context context, List<localMusicBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public LocalMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_local_music,parent,false);
        LocalMusicViewHolder holder = new LocalMusicViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull LocalMusicViewHolder holder, @SuppressLint("RecyclerView") int position) {
        localMusicBean musicBean = mDatas.get(position);
        holder.idTv.setText(musicBean.getId());
        holder.singerTv.setText(musicBean.getSinger());
        holder.albumTv.setText(musicBean.getAlbum());
        holder.songTv.setText(musicBean.getSong());
        holder.timeTv.setText(musicBean.getDuration());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class LocalMusicViewHolder extends RecyclerView.ViewHolder{
        TextView idTv,songTv,singerTv,albumTv,timeTv;
        public LocalMusicViewHolder(@NonNull View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.item_local_music_num);
            albumTv = itemView.findViewById(R.id.item_local_music_album);
            timeTv = itemView.findViewById(R.id.item_local_music_durtion);
            singerTv = itemView.findViewById(R.id.item_local_music_singer);
            songTv = itemView.findViewById(R.id.item_local_music_song);
        }
    }
}
