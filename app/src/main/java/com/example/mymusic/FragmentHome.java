package com.example.mymusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import static android.content.Context.BIND_AUTO_CREATE;

public class FragmentHome extends Fragment {

    private SongViewModel songViewModel;
    MusicService musicService;
    boolean isMusicService = false;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicServiceBinder musicServiceBinder = (MusicService.MusicServiceBinder) iBinder;
            musicService = musicServiceBinder.getService();
            isMusicService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isMusicService = false;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

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
                if (isMusicService) {
                    musicService.changSong(songViewModel.getListSong().getValue().get(position).getStringSong());
                } else {
                    Intent it = new Intent(getActivity(), MusicService.class).putExtra("stringSong", songViewModel.getListSong().getValue().get(position).getStringSong());
                    getActivity().bindService(it, serviceConnection, BIND_AUTO_CREATE);
                }
                TextView tvNamSong = getActivity().findViewById(R.id.nameSong);
                tvNamSong.setText(songViewModel.getListSong().getValue().get(position).getName());
            }
        });
        return view;
    }

}
