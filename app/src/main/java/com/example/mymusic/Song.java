package com.example.mymusic;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

public class Song {
    private int id;
    private String nameSong;
    private String dataSong;
    private String singer;
    private String albumName;

    public Song(int id, String nameSong, String dataSong, String singer, String albumName) {
        this.id = id;
        this.nameSong = nameSong;
        this.dataSong = dataSong;
        this.singer = singer;
        this.albumName = albumName;
    }

    public int getId() {
        return id;
    }

    public String getNameSong() {
        return nameSong;
    }

    public String getDataSong() {
        return dataSong;
    }

    public String getSinger() {
        return singer;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setId(int id) {
        this.id = id;
    }
}
