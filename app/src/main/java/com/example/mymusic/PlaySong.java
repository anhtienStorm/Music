package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

public class PlaySong extends AppCompatActivity {

    Button btPlay, btNext, btPrevious, btLoop, btShuffle;
    MusicService musicService;
    boolean isMusicService = false;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicServiceBinder musicServiceBinder = (MusicService.MusicServiceBinder) iBinder;
            musicService = musicServiceBinder.getService();
            isMusicService = true;
            if (musicService.isPlaying()) {
                btPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isMusicService = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent it = new Intent(this, MusicService.class);
        bindService(it, serviceConnection, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        initView();

        btPlay.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService.isPlaying()) {
                    musicService.pause();
                    btPlay.setBackgroundResource(R.drawable.ic_play_black_24dp);
                } else if (!musicService.isPlaying()) {
                    musicService.play();
                    btPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                }
            }
        });
    }

    public void initView() {
        btPlay = findViewById(R.id.btPlay);
        btNext = findViewById(R.id.btNext);
        btPrevious = findViewById(R.id.btPrevious);
        btLoop = findViewById(R.id.btLoop);
        btShuffle = findViewById(R.id.btShuffle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
