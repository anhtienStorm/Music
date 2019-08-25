package com.example.mymusic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SongViewModel extends AndroidViewModel {
    private SongRepository songRepository;
    private LiveData<List<Song>> listSong;

    public SongViewModel(@NonNull Application application) {
        super(application);
        songRepository = new SongRepository(application);
        listSong = songRepository.getListSong();
    }

    public LiveData<List<Song>> getListSong(){
        return listSong;
    }

    public void insert(Song song){
        songRepository.insert(song);
    }

    public void deleteAll(){
        songRepository.deleteAll();
    }

    public void delete(Song song){
        songRepository.delete(song);
    }

    public void update(Song song){
        songRepository.update(song);
    }
}
