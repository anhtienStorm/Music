package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class PlaySong extends AppCompatActivity {

    Button btPlay, btNext, btPrevious, btLoop, btShuffle;
    TextView tvNameSong, tvTotalTime, tvTimeSong;
    ImageView imgSong;
    SeekBar seekBar;
    MusicService musicService;
    boolean checkService = false;
    Animation animation;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicServiceBinder musicServiceBinder = (MusicService.MusicServiceBinder) iBinder;
            musicService = musicServiceBinder.getService();
            update();
            musicService.onChangeStatus(new MusicService.IListenner() {
                @Override
                public void onSelect() {
                    update();
                }
            });
            checkService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            checkService = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent it = new Intent(this, MusicService.class);
        bindService(it, serviceConnection, 0);
        if (checkService){
            update();
            musicService.onChangeStatus(new MusicService.IListenner() {
                @Override
                public void onSelect() {
                    update();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        initView();
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.disk_rotate);

        btPlay.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService.isMusicPlay()) {
                    if (musicService.isPlaying()) {
                        musicService.pause();
                        imgSong.clearAnimation();
                    } else if (!musicService.isPlaying()) {
                        musicService.play();
                        imgSong.startAnimation(animation);
                    }
                }
            }
        });

        btNext.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService.isMusicPlay()) {
                    musicService.nextSong();
                }
            }
        });

        btPrevious.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService.isMusicPlay()) {
                    musicService.previousSong();
                }
            }
        });

        btLoop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService.loopSong();
            }
        });

        btShuffle.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService.shuffleSong();
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

    }

    public void initView() {
        btPlay = findViewById(R.id.btPlay);
        btNext = findViewById(R.id.btNext);
        btPrevious = findViewById(R.id.btPrevious);
        btLoop = findViewById(R.id.btLoop);
        btShuffle = findViewById(R.id.btShuffle);
        tvNameSong = findViewById(R.id.playSong_nameSong);
        tvTimeSong = findViewById(R.id.tvTimeSong);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        imgSong = findViewById(R.id.imgSong);
        seekBar = findViewById(R.id.seekBarSong);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    public void updateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat formatTimeSong = new SimpleDateFormat("mm:ss");
                tvTimeSong.setText(formatTimeSong.format(musicService.getCurrentDuration()));
                seekBar.setProgress(musicService.getCurrentDuration());
                handler.postDelayed(this, 100);
            }
        }, 100);
    }

    public void update(){
        if (musicService.isMusicPlay()) {
            tvNameSong.setText(musicService.getNameSong());
            tvTotalTime.setText(musicService.getTotalTime());
            seekBar.setMax(musicService.getDuration());
            updateTimeSong();
            if (musicService.isPlaying()) {
                btPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
            } else {
                btPlay.setBackgroundResource(R.drawable.ic_play_black_24dp);
            }
        }
        int loop = musicService.getStatusLoop();
        int shuffle = musicService.getShuffle();
        if (loop==0){
            btLoop.setBackgroundResource(R.drawable.ic_repeat_black_24dp);
        } else if (loop==1){
            btLoop.setBackgroundResource(R.drawable.ic_repeat_violet_24dp);
        } else {
            btLoop.setBackgroundResource(R.drawable.ic_repeat_one_violet_24dp);
        }
        if (shuffle==0){
            btShuffle.setBackgroundResource(R.drawable.ic_shuffle_black_24dp);
        } else {
            btShuffle.setBackgroundResource(R.drawable.ic_shuffle_violet_24dp);
        }
    }
}