package com.example.mymusic;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class SongRepository {
    private SongDao songDao;
    private LiveData<List<Song>> listSong;

    SongRepository(Application application){
        SongRoomDatabase db = SongRoomDatabase.getDatabase(application);
        this.songDao = db.songDao();
        listSong = songDao.getListSong();
    }


    // method
    public LiveData<List<Song>> getListSong(){
        return listSong;
    }

    public void insert(Song song){
        new insertAsyncTask(songDao).execute(song);
    }

    public void deleteAll(){
        new deleteAllAsyncTask(songDao).execute();
    }

    public void delete(Song song){
        new deleteAsyncTask(songDao).execute(song);
    }

    public void update(Song song){
        new updateAsyncTask(songDao).execute(song);
    }

    // AsynTask
    private static class insertAsyncTask extends AsyncTask<Song, Void, Void> {

        private SongDao songDao;

        insertAsyncTask(SongDao dao){
            songDao = dao;
        }

        @Override
        protected Void doInBackground(Song... songs) {
            songDao.insert(songs[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Song, Void, Void> {

        private SongDao songDao;

        deleteAsyncTask(SongDao dao){
            songDao = dao;
        }

        @Override
        protected Void doInBackground(Song... songs) {
            songDao.delete(songs[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private SongDao songDao;

        deleteAllAsyncTask(SongDao dao){
            songDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            songDao.deleteAll();
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Song, Void, Void> {

        private SongDao songDao;

        updateAsyncTask(SongDao dao){
            songDao = dao;
        }

        @Override
        protected Void doInBackground(Song... songs) {
            songDao.update(songs[0]);
            return null;
        }
    }

//    private static class getAnySongAsyncTask extends AsyncTask<Void, Void, Void>{
//
//        private SongDao songDao;
//
//        getAnySongAsyncTask(SongDao dao){
//            songDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            songDao.getAnySong();
//            return null;
//        }
//    }
}
