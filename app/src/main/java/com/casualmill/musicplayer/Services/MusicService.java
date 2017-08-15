package com.casualmill.musicplayer.Services;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import com.casualmill.musicplayer.models.Track;

import java.util.ArrayList;

/**
 * Created by Ali on 08-14-2017.
 */

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener {

    private MediaPlayer player;
    ArrayList<Long> tracks;
    int trackPosition=0;

    private final IBinder serviceBinder = new MusicBinder();

    @Override
    public void onCreate(){
        super.onCreate();

        this.InitializeMusicPlayer();
    }

    public void InitializeMusicPlayer()
    {
        //Initializing MusicPlayer and its properties
        player=new MediaPlayer();

        // WAKELOCK permission is required to use this method
        // Keeps the phone awake partially to allow the playback
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        //Setting which type of audio should be played
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void PlayTrack(){
        player.reset();

        //get trackId to play
        long trackId=tracks.get(trackPosition);
        Uri trackUri= ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,trackId);

        try{
            player.setDataSource(getApplicationContext(),trackUri);
        }
        catch (Exception ex){
            Log.e("Music Player","Error occured while setting data source",ex);
        }
        player.prepareAsync();

    }

    public void setTracks(ArrayList<Long> tracks){
        this.tracks=tracks;
        this.trackPosition=0;
    }

    public void setTrackPosition(int trackPosition){
        this.trackPosition=1;
        this.trackPosition=trackPosition;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    public void playNext() {
        if (trackPosition < tracks.size() - 1)
        {
            trackPosition++;
        } else {
            trackPosition = 0;
        }
        PlayTrack();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

        playNext();


    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    public class MusicBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }
}
