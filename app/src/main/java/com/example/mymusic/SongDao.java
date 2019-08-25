package com.example.mymusic;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SongDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Song song);

    @Delete
    void delete(Song song);

    @Query("DELETE FROM song_table")
    void deleteAll();

    @Update
    void update(Song song);

    @Query("SELECT * FROM song_table ORDER BY song_name ASC")
    LiveData<List<Song>> getListSong();

    @Query("SELECT * FROM song_table LIMIT 1")
    Song[] getAnySong();
}
