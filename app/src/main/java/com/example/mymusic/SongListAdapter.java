package com.example.mymusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder> implements Filterable {

    private ArrayList<Song> mListSong;
    private ArrayList<Song> mListFullSong;
    private Context mContext;
    private ISongListAdapter listenner;

    public SongListAdapter(ArrayList<Song> mListSong, Context mContext) {
        this.mListSong = mListSong;
        mListFullSong =  new ArrayList<>(mListSong);
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
        Bitmap imgSong = song.getBmImageSong();
        if (imgSong != null){
            holder.imgSong.setImageBitmap(imgSong);
        }
        holder.tvTitleSong.setText(song.getNameSong());
        holder.tvArtist.setText(song.getSinger());
    }

    @Override
    public int getItemCount() {
        return mListSong.size();
    }

    public void setOnClickListenner(ISongListAdapter listenner){
        this.listenner = listenner;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Song> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filterList.addAll(mListFullSong);
            } else {
                String filterPattern = unAccent(charSequence.toString().toLowerCase().trim());

                for (Song song : mListFullSong){
                    if (unAccent(song.getNameSong().toLowerCase()).contains(filterPattern)){
                        filterList.add(song);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mListSong.clear();
            mListSong.addAll((Collection<? extends Song>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "d");
    }

    //interface
    interface ISongListAdapter {
        void onItemClick(int position);
    }

    // class
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

}
