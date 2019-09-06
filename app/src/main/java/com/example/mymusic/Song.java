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
    private int albumID;

    public Song(int id, String nameSong, String dataSong, String singer, int albumID) {
        this.id = id;
        this.nameSong = nameSong;
        this.dataSong = dataSong;
        this.singer = singer;
        this.albumID = albumID;
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

    public int getAlbumID() {
        return albumID;
    }

    public void setId(int id) {
        this.id = id;
    }
}
