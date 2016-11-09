package com.example.cieo233.appdevelopmentlab4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
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

import java.util.Objects;

import static com.example.cieo233.appdevelopmentlab4.mWidgetProvider.updateAppWidget;

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

                    intent.putExtra("fruitText",text);
                    intent.putExtra("resource",R.mipmap.dynamic);
                    AppWidgetManager am = AppWidgetManager.getInstance(context);
                    int[] appWidgetIds = am.getAppWidgetIds(new ComponentName(context, mWidgetProvider.class));
                    for (int appWidgetId : appWidgetIds) {
                        updateAppWidget(context, am, appWidgetId, intent, false);
                    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegistered){
            unregisterReceiver(dynamicReceiver);
        }
    }
}
