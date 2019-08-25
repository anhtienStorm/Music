package com.example.mymusic;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Database(entities = {Song.class}, version = 1, exportSchema = false)
public abstract class SongRoomDatabase extends RoomDatabase {

    public abstract SongDao songDao();
    private static SongRoomDatabase INSTANCE;

    public static SongRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (SongRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),SongRoomDatabase.class, "song_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void>{

        private final SongDao songDao;
        List<File> listSong;
        String[] songsName;

        PopulateDbAsync (SongRoomDatabase db){
            songDao = db.songDao();

            listSong = findSongs(Environment.getExternalStorageDirectory());
            songsName = new String[listSong.size()];
            for (int i = 0; i < listSong.size(); i++) {
                songsName[i] = listSong.get(i).getName().replace(".mp3","");
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (songDao.getAnySong().length<1){
                for (int i = 0; i < songsName.length; i++) {
                    Song song = new Song(songsName[i]);
                    songDao.insert(song);
                }
            }
            return null;
        }

        // get file mp3
        public ArrayList<File> findSongs(File root){
            ArrayList<File> list = new ArrayList<File>();
            File[] files = root.listFiles();
            for (File sFile : files){
                if (sFile.isDirectory() && !sFile.isHidden()){
                    list.addAll(findSongs(sFile));
                } else {
                    if (sFile.getName().endsWith(".mp3")){
                        list.add(sFile);
                    }
                }
            }
            return list;
        }
    }


}
