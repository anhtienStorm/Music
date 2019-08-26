package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

public class PlaySong extends AppCompatActivity {

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        Intent it = getIntent();
        String stringSong = it.getStringExtra("stringSong");
        Uri uri = Uri.parse(stringSong);
        mp = MediaPlayer.create(getApplicationContext(), uri);
        mp.start();
    }
}
