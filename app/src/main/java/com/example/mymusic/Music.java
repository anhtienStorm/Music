package com.example.mymusic;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.util.ArrayList;

public class Music {
    Context mContext;

    public Music(Context context) {
        this.mContext = context;
    }

    public ArrayList<Song> getListSong() {
        ArrayList<Song> listSong = new ArrayList<>();
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            musicCursor = contentResolver.query(musicUri, null, null, null, null, null);
        }

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int i = 0;
            int indexTitleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int indexDataColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int indexArtistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            do {
                String title = musicCursor.getString(indexTitleColumn);
                String data = musicCursor.getString(indexDataColumn);
                String artist = musicCursor.getString(indexArtistColumn);
                Bitmap bmImage = getAlbumArt(data);
                if (bmImage == null) {
                    bmImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_default_song);
                }
                Song song = new Song(i, title, data, artist, bmImage);
                listSong.add(song);
                i++;
            } while (musicCursor.moveToNext());
            musicCursor.close();
        }
        return listSong;
    }

    public Bitmap getAlbumArt(String path) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        byte[] data = mediaMetadataRetriever.getEmbeddedPicture();
        if (data != null)
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        else
            return null;
    }

}
