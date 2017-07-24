package com.mattaniahbeezy.handysiddur.notification;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.mattaniahbeezy.handysiddur.R;

/**
 * Created by Beezy Works Studios on 6/9/2017.
 */

public class ShabbosNotificationService extends Service {
    private static final int shabbosAlarmId = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(shabbosAlarmId);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.cloud_ladder)
                        .setContentTitle("Shabbos wakeup");
        notificationManager.notify(shabbosAlarmId, mBuilder.build());
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
