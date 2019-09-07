package com.example.mymusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

public class FragmentHome extends Fragment implements SongListAdapter.ISongListAdapter {

    MusicService musicService;
    Music music;
    ArrayList<Song> listSong;
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
        music = new Music(getActivity());
        listSong = music.getListSong();
        connectService();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        SongListAdapter adapter = new SongListAdapter(listSong, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnClickListenner(this);
        return view;
    }

    @Override
    public void onItemClick(int position) {
        musicService.playSong(listSong,position);
    }

//    public void createService(){
//        Intent it = new Intent(getActivity(), MusicService.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            getActivity().startForegroundService(it);
//        }
//    }

    public void connectService() {
        Intent it = new Intent(getActivity(), MusicService.class);
        getActivity().bindService(it, serviceConnection, 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unbindService(serviceConnection);
    }
}