package com.example.mymusic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MusicService extends Service {
    public static final String CHANNEL_ID = "MusicServiceChannel";
    private MediaPlayer mediaPlayer = null;
    private final Binder mBinder = new MusicServiceBinder();
    private ArrayList<Song> listSong;
    private Music music;
    private int position;
    IListenner listenner;

    @Override
    public void onCreate() {
        super.onCreate();
        music = new Music(getApplicationContext());
        listSong = music.getListSong();
        Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();

        if (isMusicPlay()){
            switch (intent.getAction()) {
                case "Previous":
                    previousSong();
                    break;
                case "Play":
                    if (isPlaying()){
                        pause();
                    } else {
                        play();
                    }
                    break;
            }
        }
        showNotification();
        return START_NOT_STICKY;
    }

    public void showNotification(){
        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        Intent previousIntent = new Intent(this,MusicService.class);
        previousIntent.setAction("Previous");
        PendingIntent previousPendingIntent = PendingIntent.getActivity(this,0,previousIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playIntent = new Intent(this,MusicService.class);
        playIntent.setAction("Play");
        PendingIntent playPendingIntent = PendingIntent.getActivity(this,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeImage = BitmapFactory.decodeResource(getResources(),R.drawable.icon_disk);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel musicServiceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            musicServiceChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(musicServiceChannel);
        }

        Notification notification = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_disk)
                .setContentTitle(isMusicPlay()?getNameSong():"Name Song")
                .setContentText(isMusicPlay()?getArtist():"Artist")
                .setLargeIcon(largeImage)
                .addAction(R.drawable.ic_skip_previous_black_24dp, "previous",previousPendingIntent)
                .addAction(isMusicPlay()?isPlaying()?R.drawable.ic_pause_black_24dp:R.drawable.ic_play_black_24dp:R.drawable.ic_play_black_24dp, "play", playPendingIntent)
                .addAction(R.drawable.ic_skip_next_black_24dp, "next", null)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0,1,2))
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // method
    public boolean isMusicPlay() {
        if (mediaPlayer != null)
            return true;
        return false;
    }

    public String getNameSong() {
        return listSong.get(position).getNameSong();
    }

    public String getArtist() {
        return listSong.get(position).getSinger();
    }

    public boolean isPlaying() {
        if (mediaPlayer.isPlaying())
            return true;
        else
            return false;
    }

    public void play() {
        mediaPlayer.start();
        showNotification();
        listenner.onItemClick();
    }

    public void pause() {
        mediaPlayer.pause();
        showNotification();
        listenner.onItemClick();
    }

    public void playSong(int position) {
        this.position = position;
        Uri uri = Uri.parse(listSong.get(position).getDataSong());
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        showNotification();
        listenner.onItemClick();
    }

    public void nextSong() {
        if (isMusicPlay()) {
            if (position == listSong.size() - 1) {
                position = 0;
            } else {
                position += 1;
            }
            Uri uri = Uri.parse(listSong.get(position).getDataSong());
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            }
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            showNotification();
            listenner.onItemClick();
        }
    }

    public void previousSong() {
        if (isMusicPlay()) {
            if (position == 0) {
                position = listSong.size() - 1;
            } else {
                position -= 1;
            }
            Uri uri = Uri.parse(listSong.get(position).getDataSong());
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            }
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            showNotification();
            listenner.onItemClick();
        }
    }

    public String getTotalTime() {
        SimpleDateFormat formatTimeSong = new SimpleDateFormat("mm:ss");
        return formatTimeSong.format(mediaPlayer.getDuration());
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public void setSeekTo(int seekProgress) {
        mediaPlayer.seekTo(seekProgress);
    }

    public int getCurrentDuration() {
        return mediaPlayer.getCurrentPosition();
    }

    // class
    public class MusicServiceBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    void onChangeStatus(IListenner listenner){
        this.listenner = listenner;
    }

    interface IListenner{
        void onItemClick();
    }
}