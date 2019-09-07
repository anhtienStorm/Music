package com.example.mymusic;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FragmentFavorite extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite,container,false);
        //getImageMusic();
        return view;
    }

    public Bitmap getAlbumArt(String path){
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        byte[] data = mediaMetadataRetriever.getEmbeddedPicture();
        if (data!=null)
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        else
            return null;
    }

    public void getImageMusic(){
        ArrayList<Song> listSong = new ArrayList<>();
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            String[] projection = {MediaStore.Images.Media.DATA};
            musicCursor = contentResolver.query(musicUri, projection, null, null,null, null);
        }

        if (musicCursor != null && musicCursor.moveToFirst()){
            int i = 0;
            int indexTitleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int indexDataColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int indexArtistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int indexAlbumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int indexImageColumn = musicCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                i++;
                String image = musicCursor.getString(indexImageColumn);
                Log.d("anhtien", image);
                String title = musicCursor.getString(indexTitleColumn);
                Log.d("anhtien", title);
                String data = musicCursor.getString(indexDataColumn);
                String artist = musicCursor.getString(indexArtistColumn);
                String album = musicCursor.getString(indexAlbumColumn);
                //Song song = new Song(i, title, data, artist, album);
                //mListSong.add(song);
            } while (musicCursor.moveToNext());
            musicCursor.close();
        }
    }

}
