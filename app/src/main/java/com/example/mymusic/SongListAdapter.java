package com.example.mymusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder> {

    private LayoutInflater inflater;
    private List<Song> listSong;

    SongListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_items, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        if (listSong != null) {
            Song song = listSong.get(position);
            holder.tvNameSong.setText(song.getName());
        } else {
            holder.tvNameSong.setText("No Song !!!");
        }
    }

    @Override
    public int getItemCount() {
        if (listSong != null)
            return listSong.size();
        else return 0;
    }


    // method
    void setSongs(List<Song> list){
        listSong = list;
        notifyDataSetChanged();
    }

    // class View Holder
    class SongViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNameSong;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameSong = itemView.findViewById(R.id.tvNameSong);
        }
    }

    //set onclick

}
