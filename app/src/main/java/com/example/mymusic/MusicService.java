package com.example.mymusic;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.LiveData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class MusicService extends Service {
    private MediaPlayer mediaPlayer = null;
    private final Binder mBinder = new MusicServiceBinder();
    private ArrayList<Song> listSong;
    private Music music;
    private int position;
    static int x = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        music = new Music(getApplicationContext());
        listSong = music.getListSong();
    }

    @Override
    public IBinder onBind(Intent intent) {
        x = 1;
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // method
    public boolean isMusicPlay() {
        if (mediaPlayer != null)
            return true;
        return false;
    }

    public String getNameSong() {
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
        this.position = position;
        Uri uri = Uri.parse(listSong.get(position).getDataSong());
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void nextSong() {
        if (isMusicPlay()) {
            if (position == listSong.size() - 1) {
                position = 0;
            } else {
                position += 1;
            }
            Uri uri = Uri.parse(listSong.get(position).getDataSong());
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            }
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    public void previousSong() {
        if (isMusicPlay()) {
            if (position == 0) {
                position = listSong.size() - 1;
            } else {
                position -= 1;
            }
            Uri uri = Uri.parse(listSong.get(position).getDataSong());
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            }
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    public String getTotalTime() {
        SimpleDateFormat formatTimeSong = new SimpleDateFormat("mm:ss");
        return formatTimeSong.format(mediaPlayer.getDuration());
    }

    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    public void setSeekTo(int seekProgress){
        mediaPlayer.seekTo(seekProgress);
    }

    public int getCurrentDuration(){
        return mediaPlayer.getCurrentPosition();
    }

    // class
    public class MusicServiceBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}