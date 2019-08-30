package com.example.mymusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;


public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private final Binder mBinder = new MusicServiceBinder();
    private String stringSong;
    private SongRepository songRepository;
    private LiveData<List<Song>> listSong;

    @Override
    public void onCreate() {
        super.onCreate();
        SongRoomDatabase database =
        songRepository = new SongRepository(new SongRoomDatabase();
        listSong = songRepository.getListSong();
        Log.d("anhtien", String.valueOf(listSong.getValue().size()));
    }

    @Override
    public IBinder onBind(Intent intent) {
        stringSong = intent.getStringExtra("stringSong");
        Uri uri = Uri.parse(stringSong);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
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

    public void changSong(String stringSong) {
        Uri uri = Uri.parse(stringSong);
        mediaPlayer.stop();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void nextSong() {

    }

    public Song getSongPlay(){
        for (int i = 0; i < listSong.getValue().size(); i++) {
            if (listSong.getValue().get(i).getStringSong().equals(stringSong)){
                return listSong.getValue().get(i);
            }
        }
        return null;
    }

    // class
    public class MusicServiceBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
