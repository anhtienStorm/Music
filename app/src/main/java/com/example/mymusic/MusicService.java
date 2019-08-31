package com.example.mymusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;


public class MusicService extends Service {
    private MediaPlayer mediaPlayer = null;
    private final Binder mBinder = new MusicServiceBinder();
    private ArrayList<Song> listSong;
    private Music music;
    private int position;

    @Override
    public void onCreate() {
        super.onCreate();
        music = new Music(getApplicationContext());
        listSong = music.getListSong();
    }

    @Override
    public IBinder onBind(Intent intent) {
//        position = intent.getIntExtra("position",0);
//        Uri uri = Uri.parse(listSong.get(position).getDataSong());
//        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
//        mediaPlayer.setLooping(true);
//        mediaPlayer.start();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    // method

    public boolean isMusicPlay(){
        if (mediaPlayer!=null)
            return true;
        return false;
    }

    public String getNameSong(){
        return listSong.get(position).getNameSong();
    }

    public boolean isPlaying() {
        if (mediaPlayer.isPlaying())
            return true;
        else
            return false;
    }

    public void play() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void playSong(int position) {
        Uri uri = Uri.parse(listSong.get(position).getDataSong());
        if (mediaPlayer!=null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void nextSong() {

    }

    // class
    public class MusicServiceBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
