package com.example.mymusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder> {

    private ArrayList<Song> mListSong;
    private Context mContext;
    private ISongListAdapter listenner;
    //private Music music;

    public SongListAdapter(ArrayList<Song> mListSong, Context mContext) {
        this.mListSong = mListSong;
        this.mContext = mContext;
        //music = new Music(mContext);
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SongViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recyclerview_items, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = mListSong.get(position);
        Bitmap imgSong = song.getBmImageSong();
        if (imgSong != null){
            holder.imgSong.setImageBitmap(song.getBmImageSong());
        }
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
        private ImageView imgSong;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong = itemView.findViewById(R.id.imgItemSong);
            tvTitleSong = itemView.findViewById(R.id.tvItemNameSong);
            tvArtist = itemView.findViewById(R.id.tvItemArtist);
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
