package com.example.mymusic;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Fragment mFragmentSelected, homeFragment, favoriteFragment, recentFragment;
    Button btPlay, btNext, btPrevious;
    TextView tvNameSong, tvArtist;
    ImageView imgMainSong;
    MusicService mMusicService;
    boolean mCheckService = false;
    Animation mAnimation;
    SharedPreferences mSharedPreferences;
    String sharePrefFile = "SongSharedPreferences";
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mFragmentSelected = homeFragment;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragmentSelected).commit();
                    break;
                case R.id.navigation_favorite:
                    mFragmentSelected = favoriteFragment;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragmentSelected).commit();
                    break;
                case R.id.navigation_recent:
                    mFragmentSelected = recentFragment;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragmentSelected).commit();
                    break;
            }
            return true;
        }
    };

    void createFragment() {
        homeFragment = new FragmentHome();
        favoriteFragment = new FragmentFavorite();
        recentFragment = new FragmentRecent();
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
        if (isMyServiceRunning(MusicService.class)) {
            connectService();
        } else {
            startService();
            connectService();
        }
        mSharedPreferences = getSharedPreferences(sharePrefFile,MODE_PRIVATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermission();   // xin cap quyen runtime
        createFragment();   // khoi tao cac fragment
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.disk_rotate);
        initView();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_home);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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

    }

    @Override
    protected void onResume() {
        super.onResume();
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
        tvNameSong = findViewById(R.id.tvMainNameSong);
        tvArtist = findViewById(R.id.tvMainArtist);
        imgMainSong = findViewById(R.id.imgMainSong);
    }

    // chuyen den giao dien phat nhac
    public void startPlaySong(View view) {
        Intent it = new Intent(this, PlaySong.class);
        startActivity(it);

    }

    public void startService() {
        Intent it = new Intent(MainActivity.this, MusicService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startService(it);
        }
    }

    public void connectService() {
        Intent it = new Intent(MainActivity.this, MusicService.class);
        bindService(it, mServiceConnection, 0);
    }

    public void update() {
        if (mMusicService.isMusicPlay()) {
//            imgMainSong.setImageBitmap(mMusicService.getBitmapImage());
            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(mMusicService.getAlbumID()));
            Glide.with(this).load(uri).error(R.drawable.icon_default_song).into(imgMainSong);

            tvNameSong.setText(mMusicService.getNameSong());
            tvArtist.setText(mMusicService.getArtist());
            if (mMusicService.isPlaying()) {
                btPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
            } else {
                btPlay.setBackgroundResource(R.drawable.ic_play_black_24dp);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }
}