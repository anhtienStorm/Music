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
        getImageMusic();
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
            musicCursor = contentResolver.query(musicUri, null, null, null,null, null);
        }

        if (musicCursor != null && musicCursor.moveToFirst()){
            int indexDataColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                String data = musicCursor.getString(indexDataColumn);
                Log.d("anhtien", String.valueOf(getAlbumArt(data)));
            } while (musicCursor.moveToNext());
            musicCursor.close();
        }
    }

}
