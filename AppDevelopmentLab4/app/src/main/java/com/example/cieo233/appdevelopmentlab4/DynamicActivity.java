package com.example.cieo233.appdevelopmentlab4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Cieo233 on 10/14/2016.
 */

public class DynamicActivity extends AppCompatActivity {

    private final String ACTION = "com.cieo233.lab4.DYNAMICACTION";
    private Button btnSend, btnRegisterBroadcast;
    private EditText dynamicInput;
    private boolean isRegistered;
    private BroadcastReceiver dynamicReceiver;
    private IntentFilter dynamicFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        init();
        setResponse();
    }

    void init() {
        btnRegisterBroadcast = (Button) findViewById(R.id.btnRegisterBroadcast);
        btnSend = (Button) findViewById(R.id.btnSend);
        dynamicInput = (EditText) findViewById(R.id.dynamicInput);
        isRegistered = false;
        dynamicFilter = new IntentFilter(ACTION);
        dynamicReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(ACTION)) {
                    String text = intent.getStringExtra("text");
                    showNotification(text);
                }
            }
        };
    }

    void setResponse() {
        btnRegisterBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRegistered) {
                    isRegistered = false;
                    btnRegisterBroadcast.setText("Register Broadcast");
                    unregisterReceiver(dynamicReceiver);
                } else {
                    isRegistered = true;
                    btnRegisterBroadcast.setText("Unregister Broadcast");
                    registerReceiver(dynamicReceiver, dynamicFilter);
                }
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = dynamicInput.getText().toString();
                Intent intent = new Intent();
                intent.setAction(ACTION);
                intent.putExtra("text", text);
                sendBroadcast(intent);
            }
        });
    }

    void showNotification(String text) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("动态广播")
                .setContentText(text).setTicker("动态通知")
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.dynamic, 0)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.dynamic))
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0));
        mNotificationManager.notify(1, mBuilder.build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(dynamicReceiver);
    }
}
