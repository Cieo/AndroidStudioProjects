package com.example.cieo233.appdevelopmentlab4;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Objects;

/**
 * Implementation of App Widget functionality.
 */
public class mWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Intent intent, boolean isInit) {


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_main);
        if (isInit){
            views.setTextViewText(R.id.widget_text, "Widget");
            views.setImageViewResource(R.id.widget_image,R.mipmap.apple);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,new Intent(context,MainActivity.class),0);
            views.setOnClickPendingIntent(R.id.widget_image,pendingIntent);
        }
        else {
            views.setImageViewResource(R.id.widget_image,intent.getIntExtra("resource",0));
            views.setTextViewText(R.id.widget_text, intent.getStringExtra("fruitText"));
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, null, true);
        }
    }
}

