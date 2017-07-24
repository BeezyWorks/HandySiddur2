package com.mattaniahbeezy.handysiddur;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.CurvedChildLayoutManager;
import android.support.wearable.view.WearableRecyclerView;

import com.mattaniahbeezy.handysiddur.adapters.NavigationRecyclerAdapter;
import com.mattaniahbeezy.handysiddur.notification.ShabbosNotificationService;

import java.util.Date;

public class MainActivity extends WearableActivity implements NavigationRecyclerAdapter.ShabbosAlarmStart {

    //private static final String Tag = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WearableRecyclerView navigationRecyclerView = (WearableRecyclerView) findViewById(R.id.navigation_recycler);
        navigationRecyclerView.setLayoutManager(new CurvedChildLayoutManager(this));
        NavigationRecyclerAdapter adapter = new NavigationRecyclerAdapter();
        adapter.setShabbosAlarmStart(this);
        navigationRecyclerView.setAdapter(adapter);
        navigationRecyclerView.setCircularScrollingGestureEnabled(false);
        navigationRecyclerView.setCenterEdgeItems(true);
        setAmbientEnabled();
    }

    private void stopShabbosAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(getShabbosAlertPendingIntent());
    }

    private PendingIntent getShabbosAlertPendingIntent(){
        return PendingIntent.getService(this, 1, new Intent(this, ShabbosNotificationService.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void startShabbosAlarm() {
        startService(new Intent(this, ShabbosNotificationService.class));
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                new Date().getTime(),
                AlarmManager.INTERVAL_HALF_HOUR, getShabbosAlertPendingIntent());
    }
}
