package com.mattaniahbeezy.handysiddur.complication;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.wearable.complications.ComplicationData;
import android.support.wearable.complications.ComplicationManager;
import android.support.wearable.complications.ComplicationProviderService;
import android.support.wearable.complications.ComplicationText;
import android.util.Log;

import com.mattaniahbeezy.handysiddur.activities.ZmanimActivity;
import com.mattaniahbeezy.handysiddur.utilities.DateFormatter;
import com.mattaniahbeezy.handysiddur.utilities.LocationPermissionUtility;
import com.mattaniahbeezy.siddurlibrary.hebrewcalendar.Zman;
import com.mattaniahbeezy.siddurlibrary.hebrewcalendar.ZmanimCalculator;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Beezy Works Studios on 6/2/2017.
 */

public class ZmanComplication extends ComplicationProviderService implements LocationPermissionUtility.LocationHandled {
    private LocationPermissionUtility locationUtil;
    private static final String Tag = "ZmanComplication";

    @Override
    public void onComplicationActivated(int complicationId, int type, ComplicationManager manager) {
        Log.d(Tag, "Activated");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(this, ZmanimActivity.class));
        }
        locationUtil = new LocationPermissionUtility(this, this);
        locationUtil.connect();
        if (LocationPermissionUtility.hasSavedLocation(this)) {
            ZmanimCalculator.getInstance().setLocation(LocationPermissionUtility.getSavedLocation(this));
        }
        onComplicationUpdate(complicationId, type, manager);
    }

    @Override
    public void onComplicationUpdate(int complicationId, int dataType, ComplicationManager complicationManager) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(getApplicationContext(), ZmanimActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar zmanCalendar = ZmanimCalculator.getInstance().getCalendar();
        Calendar now = Calendar.getInstance();
        if (zmanCalendar.get(Calendar.DAY_OF_YEAR) != now.get(Calendar.DAY_OF_YEAR)) {
            ZmanimCalculator.getInstance().setCalendar(now);
        }
        if (LocationPermissionUtility.hasSavedLocation(this)) {
            ZmanimCalculator.getInstance().setLocation(LocationPermissionUtility.getSavedLocation(this));
        }
        Zman upcomingZman = ZmanimCalculator.getInstance().getUpcomingZman();
        Date upcomingZmanTime = ZmanimCalculator.getInstance().getZmanTime(upcomingZman);
        String formattedZmanTime = upcomingZmanTime == null ?
                "Loading ..." :
                DateFormatter.getInstance().formatTime(upcomingZmanTime);
        ComplicationData returnData = null;
        switch (dataType) {
            case ComplicationData.TYPE_LONG_TEXT:
                returnData = new ComplicationData.Builder(ComplicationData.TYPE_LONG_TEXT)
                        .setLongTitle(ComplicationText.plainText(upcomingZman.hebrewName()))
                        .setLongText(ComplicationText.plainText(formattedZmanTime))
                        .setTapAction(pendingIntent)
                        .build();
                break;
            case ComplicationData.TYPE_SHORT_TEXT:
                returnData = new ComplicationData.Builder(ComplicationData.TYPE_SHORT_TEXT)
                        .setShortTitle(ComplicationText.plainText(upcomingZman.hebrewName()))
                        .setShortText(ComplicationText.plainText(formattedZmanTime))
                        .setTapAction(pendingIntent)
                        .build();
        }

        assert returnData != null;

        complicationManager.updateComplicationData(complicationId, returnData);
        Log.d(Tag, "Updated");
    }

    @Override
    public void locationAvailable(Location location, int locationSource) {
        ZmanimCalculator.getInstance().setLocation(location);
        if (locationSource == LocationPermissionUtility.RETRIEVED) {
            locationUtil.disconnect();
        }
    }

    @Override
    public void onComplicationDeactivated(int complicationId) {
        if (locationUtil != null) {
            locationUtil.disconnect();
        }
    }
}
