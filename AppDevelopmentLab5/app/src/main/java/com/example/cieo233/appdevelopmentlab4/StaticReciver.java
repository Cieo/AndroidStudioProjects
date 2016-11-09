package com.example.cieo233.appdevelopmentlab4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Objects;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.cieo233.appdevelopmentlab4.mWidgetProvider.updateAppWidget;

/**
 * Created by Cieo233 on 10/15/2016.
 */

public class StaticReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager am = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = am.getAppWidgetIds(new ComponentName(context, mWidgetProvider.class));
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, am, appWidgetId, intent, false);
        }

    }
}
