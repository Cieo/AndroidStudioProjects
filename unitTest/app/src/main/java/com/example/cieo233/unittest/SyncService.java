package com.example.cieo233.unittest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Cieo233 on 12/15/2016.
 */

public class SyncService extends Service {
    Handler syncReminderHandler;
    Handler syncChannelHandler;
    Timer timer;

    @Override
    public void onCreate() {
        Log.e("SeriveCreated", "同步服务创建成功");
        syncReminderHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case StateCode.OK:
                        sendBroadcast(new Intent("Cieo.SyncReminderComplete"));
                        Log.e("ServiceTest", "ServiceSyncReminder成功");
                        break;
                    case StateCode.TOKEN_INVALID:
                        Log.e("ServiceTest", "SyncReminder失败");
                        break;
                }
                return false;
            }
        });
        syncChannelHandler = new Handler(new Handler.Callback() {
            int count = 0;

            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case StateCode.OK:
                        count += 1;
                        if (count == 3) {
                            sendBroadcast(new Intent("Cieo.SyncChannelComplete"));
                            Log.e("ServiceTest", "ServiceSyncChannel成功");
                            count = 0;
                        }
                        break;
                    case StateCode.TOKEN_INVALID:
                        Log.e("ServiceTest", "SyncChannel失败");
                        break;
                }
                return false;
            }
        });
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("ServiceUP", "同步服务启动成功");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                syncChannel();
                syncReminder();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 1000 * 600);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        Log.e("ServiceDown", "同步服务关闭成功，定时器取消");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    void syncChannel() {
        CodoAPI.getChannels(syncChannelHandler);
    }

    void syncReminder() {
        CodoAPI.getReminders(syncReminderHandler);
    }

}
