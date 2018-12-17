package com.qin.aidl_keeplive.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qin.aidl_keeplive.R;
import com.qin.aidl_keeplive.bean.Music;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Create by qindl
 * on 2018/12/17
 */
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private ArrayList<Music> mMusicList;

    private Context mContext;

    public MusicAdapter(Context context) {
        mContext = context;
        mMusicList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MusicViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder musicViewHolder, int i) {
        musicViewHolder.bindData(mMusicList, i);
    }

    @Override
    public int getItemCount() {
        return mMusicList.size();
    }

    class MusicViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mAuthor;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_name);
            mAuthor = itemView.findViewById(R.id.tv_author);

        }

        public void bindData(List<Music> musicList, int i) {
            mName.setText(musicList.get(i).getMusicName());
            mAuthor.setText(musicList.get(i).getMusicAuthor());
        }
    }

    public void setDataList(List<Music> list) {
        int size = mMusicList.size();
        mMusicList.addAll(list);
        notifyDataSetChanged();
    }

    public void setData(Music music){
        mMusicList.add(music);
        notifyDataSetChanged();
    }
}
