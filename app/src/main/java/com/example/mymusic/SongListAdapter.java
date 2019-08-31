package com.example.mymusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder> {

    private ArrayList<Song> mListSong;
    private Context mContext;
    private ISongListAdapter listenner;

    public SongListAdapter(ArrayList<Song> mListSong, Context mContext) {
        this.mListSong = mListSong;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SongViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recyclerview_items, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = mListSong.get(position);
        holder.tvTitleSong.setText(song.getNameSong());
        holder.tvArtist.setText(song.getSinger());
    }

    @Override
    public int getItemCount() {
        return mListSong.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitleSong;
        private TextView tvArtist;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitleSong = itemView.findViewById(R.id.tvNameSong);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listenner.onItemClick(getAdapterPosition());
        }
    }

    public void setOnClickListenner(ISongListAdapter listenner){
        this.listenner = listenner;
    }

    interface ISongListAdapter {
        void onItemClick(int position);
    }
}
