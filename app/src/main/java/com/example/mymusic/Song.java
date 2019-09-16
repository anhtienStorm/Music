package com.example.mymusic;

import android.graphics.Bitmap;

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
//    private Bitmap bmImageSong;
    private String albumID;

//    public Song(int id, String nameSong, String dataSong, String singer, Bitmap bmImageSong) {
//        this.id = id;
//        this.nameSong = nameSong;
//        this.dataSong = dataSong;
//        this.singer = singer;
//        this.bmImageSong = bmImageSong;
//    }


    public Song(int id, String nameSong, String dataSong, String singer, String albumID) {
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

//    public Bitmap getBmImageSong() {
//        return bmImageSong;
//    }


    public String getAlbumID() {
        return albumID;
    }

    public void setId(int id) {
        this.id = id;
    }
}
