package com.example.mymusic;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
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
    public static ArrayList<String> songArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite,container,false);

        songArrayList = new ArrayList();
        getSongList();
        for (int i = 0; i < songArrayList.size(); i++) {
            Log.d("anhtien", songArrayList.get(i));
            Toast.makeText(getActivity(), songArrayList.get(i), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    public void getSongList()
    {
        /**
         * All the audio files can be accessed using the below initialised musicUri.
         * And there is a cursor to iterate over each and every column.
         */
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            musicCursor = contentResolver.query(musicUri, null, null, null, null, null);
        }

        // If cursor is not null
        if(musicCursor != null && musicCursor.moveToFirst())
        {
            //get Columns
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int dataColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);


            // Store the title, id and artist name in Song Array list.
            do
            {
                String thisTitle = musicCursor.getString(titleColumn);
                // Add the info to our array.
                songArrayList.add(thisTitle);
            }
            while (musicCursor.moveToNext());

            // For best practices, close the cursor after use.
            musicCursor.close();
        }
    }

}
