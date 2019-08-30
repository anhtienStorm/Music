package com.example.mymusic;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SongListAdapter.ClickListener {

    Fragment homeFragment, favoriteFragment;
    Button btPlay , btNext;
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                    break;
                case R.id.navigation_favorite:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,favoriteFragment).commit();
                    break;
            }
            return true;
        }
    };

    void createFragment(){
        homeFragment = new FragmentHome();
        favoriteFragment = new FragmentFavorite();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermission();   // xin cap quyen runtime
        createFragment();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        btPlay = findViewById(R.id.btMainPlay);
        btNext = findViewById(R.id.btMainNext);
        btPlay.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService.isPlaying()) {
                    musicService.pause();
                    btPlay.setBackgroundResource(R.drawable.ic_play_black_24dp);
                } else if (!musicService.isPlaying()){
                    musicService.play();
                    btPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                }
            }
        });

        btNext.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    public void startPlaySong(View view) {
        Intent it = new Intent(this, PlaySong.class);
        startActivity(it);
    }

    public void connectService(View view){
        Intent it = new Intent(MainActivity.this, MusicService.class);
        bindService(it, serviceConnection, 0);

    }

    @Override
    public void onClick(View view, int position) {
        Intent it = new Intent(MainActivity.this, MusicService.class);
        bindService(it, serviceConnection, 0);
    }
}
