package com.example.mymusic;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentHome extends Fragment implements SongListAdapter.ISongListAdapter {

    MusicService mMusicService;
    Music mMusic;
    ArrayList<Song> mListSong;
    //IHomeFragment listenner;
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicServiceBinder musicServiceBinder = (MusicService.MusicServiceBinder) iBinder;
            mMusicService = musicServiceBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mMusic = new Music(getActivity());
        mListSong = mMusic.getListSong();
        connectService();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        SongListAdapter adapter = new SongListAdapter(mListSong, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnClickListenner(this);
        return view;
    }

    @Override
    public void onItemClick(int position) {
        mMusicService.playSong(mListSong,position);
    }

    public void createService(){
        Intent it = new Intent(getActivity(), MusicService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().startForegroundService(it);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unbindService(mServiceConnection);
    }

    public void connectService() {
        Intent it = new Intent(getActivity(), MusicService.class);
        getActivity().bindService(it, mServiceConnection, 0);
    }

//    void setOnCreateService(IHomeFragment listenner){
//        this.listenner = listenner;
//    }

    //interface
//    interface IHomeFragment{
//        void onCreateService();
//    }
}