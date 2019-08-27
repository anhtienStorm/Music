package com.example.mymusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private Binder mBinder = new MusicServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        String stringSong = intent.getStringExtra("stringSong");
        Uri uri = Uri.parse(stringSong);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        return mBinder;
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        String stringSong = intent.getStringExtra("stringSong");
//        Uri uri = Uri.parse(stringSong);
//        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
//        mediaPlayer.setLooping(true);
//        mediaPlayer.start();
//        return START_STICKY;
//    }

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
    public boolean isPlaying(){
        if (mediaPlayer.isPlaying())
            return true;
        else
            return false;
    }

    public void play(){
        mediaPlayer.start();
    }

    public void pause(){
        mediaPlayer.pause();
    }

    public void stop(){
        mediaPlayer.stop();
    }

    // class
    public class MusicServiceBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }
}
