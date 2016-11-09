package com.example.cieo233.appdevelopmentlab6;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Cieo233 on 10/27/2016.
 */

public class MainService extends Service {
    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new mBinder(mediaPlayer);
    }

    class mBinder extends Binder {
        private MediaPlayer mediaPlayer;

        public mBinder(MediaPlayer mediaPlayer) {
            this.mediaPlayer = mediaPlayer;
        }

        public MediaPlayer getMediaPlayer() {
            return mediaPlayer;
        }
    }
}
