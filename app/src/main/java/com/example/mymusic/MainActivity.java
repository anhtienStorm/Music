package com.example.mymusic;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Fragment homeFragment, favoriteFragment;
    Button btPlay, btNext, btPrevious;
    TextView tvNameSong, tvArtist;
    ImageView imgSong;
    MusicService musicService;
    boolean checkService = false;
    Animation animation;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicServiceBinder musicServiceBinder = (MusicService.MusicServiceBinder) iBinder;
            musicService = musicServiceBinder.getService();
            update();
            checkService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            checkService = false;
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                    break;
                case R.id.navigation_favorite:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, favoriteFragment).commit();
                    break;
            }
            return true;
        }
    };

    void createFragment() {
        homeFragment = new FragmentHome();
        favoriteFragment = new FragmentFavorite();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, String.valueOf(isMyServiceRunning(MusicService.class)), Toast.LENGTH_SHORT).show();
        if (isMyServiceRunning(MusicService.class)){
            Intent it = new Intent(MainActivity.this, MusicService.class);
            bindService(it, serviceConnection, 0);
        } else {
            connectService();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkService){
            update();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermission();   // xin cap quyen runtime
        createFragment();   // khoi tao cac fragment
        animation = AnimationUtils.loadAnimation(this, R.anim.disk_rotate);
        initView();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
                    update();
                }
            }
        });

        btNext.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService.isMusicPlay()) {
                    musicService.nextSong();
                }
                update();
            }
        });

        btPrevious.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService.isMusicPlay()) {
                    musicService.previousSong();
                }
                update();
            }
        });


    }

    //xin cap quyen runtime
    public void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(MainActivity.this, "Permission isn't granted ", Toast.LENGTH_SHORT).show();
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    Toast.makeText(MainActivity.this, "Permisson don't granted and dont show dialog again ", Toast.LENGTH_SHORT).show();
                }
                //Register permission
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Quyền đọc file: được phép", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(MainActivity.this, "Quyền đọc file: không được phép", Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void initView() {
        btPlay = findViewById(R.id.btMainPlay);
        btNext = findViewById(R.id.btMainNext);
        btPrevious = findViewById(R.id.btMainPrevious);
        tvNameSong = findViewById(R.id.nameSong);
        tvArtist = findViewById(R.id.artist);
        imgSong = findViewById(R.id.imgSong);
    }

    // chuyen den giao dien phat nhac
    public void startPlaySong(View view) {
        Intent it = new Intent(this, PlaySong.class);
        startActivity(it);

    }

    public void connectService() {
        Intent it = new Intent(MainActivity.this, MusicService.class);
        bindService(it, serviceConnection, BIND_AUTO_CREATE);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 0){
//            update();
//        }
//    }

    public void update(){
        if (musicService.isMusicPlay()) {
            tvNameSong.setText(musicService.getNameSong());
            tvArtist.setText(musicService.getArtist());
            if (musicService.isPlaying()) {
                btPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
            } else {
                btPlay.setBackgroundResource(R.drawable.ic_play_black_24dp);
            }
        }
    }
}