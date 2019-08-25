package com.example.mymusic;

import android.Manifest;
import android.content.Context;
import android.content.ReceiverCallNotAllowedException;
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

import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SongViewModel songViewModel;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectFragment = new FragmentHome();
                    break;
                case R.id.navigation_favorite:
                    selectFragment = new FragmentFavorite();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermission();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentHome()).commit();
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    //xin cap quyen runtime
    public void initPermission(){
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
}
