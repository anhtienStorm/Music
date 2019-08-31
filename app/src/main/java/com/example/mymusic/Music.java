package com.example.mymusic;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.util.ArrayList;

public class Music {
    Context context;


    public Music(Context context){
        this.context = context;
    }

    public ArrayList<Song> getListSong(){
        ArrayList<Song> listSong = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            musicCursor = contentResolver.query(musicUri, null, null, null,null, null);
        }

        if (musicCursor != null && musicCursor.moveToFirst()){
            int i = 0;
            int indexTitleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int indexDataColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int indexArtistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int indexAlbumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            do {
                i++;
                String title = musicCursor.getString(indexTitleColumn);
                String data = musicCursor.getString(indexDataColumn);
                String artist = musicCursor.getString(indexArtistColumn);
                String album = musicCursor.getString(indexAlbumColumn);
                Song song = new Song(i, title, data, artist, album);
                listSong.add(song);
            } while (musicCursor.moveToNext());
            musicCursor.close();
        }
        return listSong;
    }
}
