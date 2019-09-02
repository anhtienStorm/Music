package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class PlaySong extends AppCompatActivity {

    Button btPlay, btNext, btPrevious, btLoop, btShuffle;
    TextView tvTotalTime, tvTimeSong;
    SeekBar seekBar;
    MusicService musicService;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicServiceBinder musicServiceBinder = (MusicService.MusicServiceBinder) iBinder;
            musicService = musicServiceBinder.getService();
            if (musicService.isMusicPlay()) {
                tvTotalTime.setText(musicService.getTotalTime());
                seekBar.setMax(musicService.getDuration());
                updateTimeSong();
                if (musicService.isPlaying()) {
                    btPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                }
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
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
                Intent replyIntent = new Intent();
                if (musicService.isMusicPlay()) {
                    if (musicService.isPlaying()) {
                        musicService.pause();
                        btPlay.setBackgroundResource(R.drawable.ic_play_black_24dp);
                    } else if (!musicService.isPlaying()) {
                        musicService.play();
                        btPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                    }
                    replyIntent.putExtra("statusPlay", musicService.isPlaying());
                    replyIntent.putExtra("currentSong", musicService.getNameSong());
                    setResult(RESULT_OK, replyIntent);
                } else {
                    setResult(RESULT_CANCELED, replyIntent);
                    Toast.makeText(PlaySong.this, "Vui lòng chọn bài hát để phát !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btNext.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (musicService.isMusicPlay()){
                    musicService.nextSong();
                    if (musicService.isPlaying()){
                        btPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                    }
                    tvTotalTime.setText(musicService.getTotalTime());
                    seekBar.setMax(musicService.getDuration());
                    updateTimeSong();
                    replyIntent.putExtra("statusPlay", musicService.isPlaying());
                    replyIntent.putExtra("currentSong", musicService.getNameSong());
                    setResult(RESULT_OK, replyIntent);
                }
            }
        });

        btPrevious.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (musicService.isMusicPlay()){
                    musicService.previousSong();
                    if (musicService.isPlaying()){
                        btPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                    }
                    tvTotalTime.setText(musicService.getTotalTime());
                    seekBar.setMax(musicService.getDuration());
                    updateTimeSong();
                    replyIntent.putExtra("statusPlay", musicService.isPlaying());
                    replyIntent.putExtra("currentSong", musicService.getNameSong());
                    setResult(RESULT_OK, replyIntent);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicService.setSeekTo(seekBar.getProgress());
            }
        });

//        if (musicService.isMusicPlay()){
//            replyIntent.putExtra("statusPlay", musicService.isPlaying());
//            setResult(RESULT_OK, replyIntent);
//        } else {
//            setResult(RESULT_CANCELED, replyIntent);
//            Toast.makeText(PlaySong.this, "Vui lòng chọn bài hát để phát !", Toast.LENGTH_SHORT).show();
//        }
    }

    public void initView() {
        btPlay = findViewById(R.id.btPlay);
        btNext = findViewById(R.id.btNext);
        btPrevious = findViewById(R.id.btPrevious);
        btLoop = findViewById(R.id.btLoop);
        btShuffle = findViewById(R.id.btShuffle);
        tvTimeSong =findViewById(R.id.tvTimeSong);
        tvTotalTime =findViewById(R.id.tvTotalTime);
        seekBar = findViewById(R.id.seekBarSong);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    public void updateTimeSong(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat formatTimeSong = new SimpleDateFormat("mm:ss");
                tvTimeSong.setText(formatTimeSong.format(musicService.getCurrentDuration()));
                seekBar.setProgress(musicService.getCurrentDuration());
                handler.postDelayed(this, 500);
            }
        }, 100);
    }
}