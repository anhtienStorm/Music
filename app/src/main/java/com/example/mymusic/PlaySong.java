package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
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

import java.text.SimpleDateFormat;

public class PlaySong extends AppCompatActivity {

    Button btPlay, btNext, btPrevious, btLoop, btShuffle;
    TextView tvNameSong, tvTotalTime, tvTimeSong;
    ImageView imgSong;
    SeekBar seekBar;
    MusicService mMusicService;
    boolean mCheckService = false;
    Animation mAnimation;
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicServiceBinder musicServiceBinder = (MusicService.MusicServiceBinder) iBinder;
            mMusicService = musicServiceBinder.getService();
            update();
            mMusicService.onChangeStatus(new MusicService.IListenner() {
                @Override
                public void onSelect() {
                    update();
                }
            });
            mCheckService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mCheckService = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent it = new Intent(this, MusicService.class);
        bindService(it, mServiceConnection, 0);
        if (mCheckService){
            update();
            mMusicService.onChangeStatus(new MusicService.IListenner() {
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
        mAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.disk_rotate);

        btPlay.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMusicService.isMusicPlay()) {
                    if (mMusicService.isPlaying()) {
                        mMusicService.pause();
                    } else if (!mMusicService.isPlaying()) {
                        mMusicService.play();
                    }
                }
            }
        });

        btNext.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMusicService.isMusicPlay()) {
                    mMusicService.nextSong();
                }
            }
        });

        btPrevious.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMusicService.isMusicPlay()) {
                    mMusicService.previousSong();
                }
            }
        });

        btLoop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMusicService.loopSong();
            }
        });

        btShuffle.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMusicService.shuffleSong();
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
                mMusicService.setSeekTo(seekBar.getProgress());
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    public void startService(){
        Intent it = new Intent(this, MusicService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(it);
        }
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

    public void updateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat formatTimeSong = new SimpleDateFormat("mm:ss");
                tvTimeSong.setText(formatTimeSong.format(mMusicService.getCurrentDuration()));
                seekBar.setProgress(mMusicService.getCurrentDuration());
                handler.postDelayed(this, 100);
            }
        }, 100);
    }

    public void update(){
        if (mMusicService.isMusicPlay()) {
            tvNameSong.setText(mMusicService.getNameSong());
            tvTotalTime.setText(mMusicService.getTotalTime());
            seekBar.setMax(mMusicService.getDuration());
            updateTimeSong();
            if (mMusicService.isPlaying()) {
                btPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
            } else {
                btPlay.setBackgroundResource(R.drawable.ic_play_black_24dp);
            }
        }
        int loop = mMusicService.getmStatusLoop();
        int shuffle = mMusicService.getmShuffle();
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