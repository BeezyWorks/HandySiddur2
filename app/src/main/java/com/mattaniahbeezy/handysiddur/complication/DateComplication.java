package com.mattaniahbeezy.handysiddur.complication;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.wearable.complications.ComplicationData;
import android.support.wearable.complications.ComplicationManager;
import android.support.wearable.complications.ComplicationProviderService;
import android.support.wearable.complications.ComplicationText;
import android.util.Log;

import com.mattaniahbeezy.handysiddur.activities.ZmanimActivity;
import com.mattaniahbeezy.siddurlibrary.hebrewcalendar.HebrewDate;

/**
 * Created by Beezy Works Studios on 6/5/2017.
 */

public class DateComplication extends ComplicationProviderService {
    private static final String TAG = "DateComplication";

    @Override
    public void onComplicationActivated(int complicationId, int type, ComplicationManager manager) {
        super.onComplicationActivated(complicationId, type, manager);
        onComplicationUpdate(complicationId, type, manager);
    }

    @Override
    public void onComplicationUpdate(int complicationId, int dataType, ComplicationManager complicationManager) {
        // TODO: change to better activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(getApplicationContext(), ZmanimActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        String title = HebrewDate.getTimeSensitiveInstance().getHebrewMonthName();
        String text = HebrewDate.getInstance().formatNumberToGematria(HebrewDate.getTimeSensitiveInstance().getHebrewDayOfMonth());

        ComplicationData returnData = null;
        switch (dataType) {
            case ComplicationData.TYPE_LONG_TEXT:
                returnData = new ComplicationData.Builder(ComplicationData.TYPE_LONG_TEXT)
                        .setLongTitle(ComplicationText.plainText(title))
                        .setLongText(ComplicationText.plainText(text))
                        .setTapAction(pendingIntent)
                        .build();
                break;
            case ComplicationData.TYPE_SHORT_TEXT:
                returnData = new ComplicationData.Builder(ComplicationData.TYPE_SHORT_TEXT)
                        .setShortTitle(ComplicationText.plainText(title))
                        .setShortText(ComplicationText.plainText(text))
                        .setTapAction(pendingIntent)
                        .build();
        }

        assert returnData != null;

        complicationManager.updateComplicationData(complicationId, returnData);
        Log.d(TAG, "Updated");
    }
}
