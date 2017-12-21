package com.example.maqso.assigment03;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.support.v4.app.NotificationCompat;

import org.greenrobot.eventbus.EventBus;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, MainActivity.class);
        EventBus.getDefault().post(new OnReceived(context, intent));
        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("MY_WIRELESS_SETTING", context.MODE_PRIVATE).edit();

        if (intent.getAction().equals("android.intent.action.AIRPLANE_MODE")) {
            Boolean state = intent.getBooleanExtra("state", false);
            editor.putBoolean("AIR_PLANE_STATE", state);
            if (state){
                notifyGenerator(context,"Notification","AirPlan Mode on",0,R.drawable.ic_airplanemode);
            }else{
                notifyGenerator(context,"Notification","AirPlan Mode Off",0,R.drawable.ic_airplanemode);
            }

        } else if (intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {

            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager.isWifiEnabled()) {
                editor.putBoolean("WIFI_STATUS", true);
                notifyGenerator(context,"Notification","Wifi On",1,R.drawable.ic_wifi);
            } else {
                editor.putBoolean("WIFI_STATUS", false);
                notifyGenerator(context,"Notification","Wifi off",1,R.drawable.ic_wifi);
            }
        }
        editor.apply();
    }

    public void notifyGenerator(Context context, String title, String message, int id, int icon) {

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "Channel1")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(icon)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification.build());
    }
}
