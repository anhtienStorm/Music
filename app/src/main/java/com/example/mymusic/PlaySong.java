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
        int index = Integer.parseInt(it.getStringExtra("id"));
        Uri uri = Uri.parse(SongRoomDatabase.listFileSong.get(index).toString());
        mp = MediaPlayer.create(getApplicationContext(), uri);
        mp.start();
    }
}
