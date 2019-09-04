package com.example.mymusic;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class NotificationApp extends Application {
    public static final String CHANNEL_ID = "MusicServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

    }

    public void createNotificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel musicServiceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(musicServiceChannel);
        }

    }
}
