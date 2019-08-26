package com.example.mymusic;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "song_table")
public class Song {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "song_name")
    private String name;

    @ColumnInfo(name = "song_string")
    private String stringSong;

    public Song(@NonNull String name, String stringSong) {
        this.name = name;
        this.stringSong = stringSong;
    }

    @Ignore
    public Song(int id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setStringSong(String stringSong) {
        this.stringSong = stringSong;
    }

    public String getStringSong() {
        return stringSong;
    }
}
