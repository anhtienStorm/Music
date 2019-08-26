package com.example.mymusic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    private SongViewModel songViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container,false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        final SongListAdapter adapter = new SongListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        songViewModel = ViewModelProviders.of(this).get(SongViewModel.class);
        songViewModel.getListSong().observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                adapter.setSongs(songs);
            }
        });

        adapter.setOnClickListenner(new SongListAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int index = songViewModel.getListSong().getValue().get(position).getId();
                Intent it = new Intent(getActivity(), PlaySong.class).putExtra("id", index+"");
                startActivity(it);
            }
        });

        return view;
    }
}
