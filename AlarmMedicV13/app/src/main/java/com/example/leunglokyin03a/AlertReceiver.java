package com.example.leunglokyin03a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import static com.example.leunglokyin03a.store.idalarm;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    //notification AlertReceiver
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(store.idalarm, nb.build());
    }
}
