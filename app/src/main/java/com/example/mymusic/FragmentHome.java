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

public class FragmentHome extends Fragment implements SongListAdapter.ISongListAdapter {

    MusicService musicService;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicServiceBinder musicServiceBinder = (MusicService.MusicServiceBinder) iBinder;
            musicService = musicServiceBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Intent it = new Intent(getActivity(), MusicService.class);
        getActivity().bindService(it, serviceConnection, 0);

        Music music = new Music(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        SongListAdapter adapter = new SongListAdapter(music.getListSong(), getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnClickListenner(this);
        return view;
    }

    @Override
    public void onItemClick(int position) {
        musicService.playSong(position);
        if (musicService.isMusicPlay()){
            TextView tvNameSong = getActivity().findViewById(R.id.nameSong);
            Button btPlay = getActivity().findViewById(R.id.btMainPlay);
            tvNameSong.setText(musicService.getNameSong());
            btPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        }
    }
}
