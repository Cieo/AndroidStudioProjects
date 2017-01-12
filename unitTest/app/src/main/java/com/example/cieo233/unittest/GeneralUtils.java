package com.example.cieo233.unittest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Cieo233 on 12/10/2016.
 */

public class GeneralUtils {
    public static void showToast(Context context, String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }

    public static boolean isNetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if (ni.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void showNotification(Context context, String content){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context).setContentTitle("Codo").setContentText(content).setSmallIcon(R.mipmap.ic_event_note_white_48dp);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,new Intent(context,MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        manager.notify(0,builder.build());
    }
}
