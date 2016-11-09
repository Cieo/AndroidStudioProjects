package com.example.cieo233.appdevelopmentlab6;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private Button playOrPause, stop, quit;
    private SeekBar progressBar;
    private Chronometer currentPosition, duration;
    private ImageView cover;
    private TextView status;
    private MediaPlayer mediaPlayer;
    private MainService.mBinder mBinder;
    private boolean isFirst, isChanging;
    private ServiceConnection connection;
    private SimpleDateFormat formatter;
    private final String DEBUG233 = "DEBUG233";
    private ObjectAnimator discAnimator;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!isFirst & !isChanging) {
//              update the progress bar is we start playing
//              and not dragging the progress bar
                progressBar.setProgress(msg.what);
                currentPosition.setText(formatter.format(msg.what));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        startService();
        setResponse();
        setAnimator();
    }

    void init() {
        playOrPause = (Button) findViewById(R.id.playOrPause);
        stop = (Button) findViewById(R.id.stop);
        quit = (Button) findViewById(R.id.quit);
        progressBar = (SeekBar) findViewById(R.id.progressBar);
        currentPosition = (Chronometer) findViewById(R.id.currentPostion);
        duration = (Chronometer) findViewById(R.id.duration);
        cover = (ImageView) findViewById(R.id.cover);
        status = (TextView) findViewById(R.id.status);
        isFirst = true;
        isChanging = false;
        formatter = new SimpleDateFormat("mm:ss");
        status.setVisibility(View.INVISIBLE);
//      set the detials of connection , get the instance of mediaplayer
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mBinder = (MainService.mBinder) iBinder;
                mediaPlayer = mBinder.getMediaPlayer();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
    }

    //  start the service
    void startService() {
        Intent bindIntent = new Intent(this, MainService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }

    //  set the onclicklistener
    void setResponse() {
        playOrPause.setOnClickListener(this);
        stop.setOnClickListener(this);
        quit.setOnClickListener(this);
        progressBar.setOnSeekBarChangeListener(this);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                status.setVisibility(View.INVISIBLE);
                isFirst = true;
                mediaPlayer.reset();
                playOrPause.setText("PLAY");
                progressBar.setProgress(0);
                currentPosition.setText(formatter.format(0));
                duration.setText(formatter.format(0));
                discAnimator.end();
            }
        });
    }

    //  set the disc anime
    void setAnimator() {
        discAnimator = ObjectAnimator.ofFloat(cover, "rotation", 0, 360);
        discAnimator.setDuration(20000);
        discAnimator.setInterpolator(new LinearInterpolator());
        discAnimator.setRepeatCount(ValueAnimator.INFINITE);
        discAnimator.setRepeatMode(ValueAnimator.RESTART);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playOrPause:
//              if this is the first time to play the music
//              init mediaplayer , start updating the progressbar
                if (isFirst) {
                    isFirst = false;
                    try {
                        mediaPlayer.setDataSource("/storage/emulated/0/netease/cloudmusic/Music/阿桑 - 一直很安静.mp3");
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        status.setVisibility(View.VISIBLE);
                        status.setText("Playing");
                        progressBar.setMax(mediaPlayer.getDuration());
                        progressBar.setProgress(0);
                        currentPosition.setText(formatter.format(0));
                        duration.setText(formatter.format(mediaPlayer.getDuration()));
                        playOrPause.setText("PAUSE");
                        discAnimator.start();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (!isFirst) {
                                    if (!isChanging) {
                                        handler.sendEmptyMessage(mediaPlayer.getCurrentPosition());
                                    }
                                }
                            }
                        }).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
//                  else , just start or pause the player
                    if (mediaPlayer.isPlaying()) {
                        status.setText("Pause");
                        playOrPause.setText("Play");
                        mediaPlayer.pause();
                        discAnimator.pause();
                    } else {
                        status.setText("Playing");
                        playOrPause.setText("Pause");
                        mediaPlayer.start();
                        discAnimator.resume();
                    }
                }
                break;
            case R.id.stop:
//              clear the mediaplayer is we press stop
                status.setVisibility(View.INVISIBLE);
                isFirst = true;
                mediaPlayer.reset();
                playOrPause.setText("PLAY");
                progressBar.setProgress(0);
                currentPosition.setText(formatter.format(0));
                duration.setText(formatter.format(0));
                discAnimator.end();
                break;
            case R.id.quit:
//              quit the activity if we press quit
                isFirst = true;
                mediaPlayer.stop();
                mediaPlayer.release();
                unbindService(connection);
                this.finish();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    //  add a lock to the progress bar , stop updating when we a dragging it
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (!isFirst) {
            isChanging = true;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (!isFirst) {
            mediaPlayer.seekTo(progressBar.getProgress());
            isChanging = false;
        }
        if (isFirst) {
            progressBar.setProgress(0);
        }
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }
}
