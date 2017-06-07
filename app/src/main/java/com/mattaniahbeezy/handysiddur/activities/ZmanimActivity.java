package com.mattaniahbeezy.handysiddur.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.complications.ProviderUpdateRequester;
import android.support.wearable.view.CurvedChildLayoutManager;
import android.support.wearable.view.WearableRecyclerView;

import com.mattaniahbeezy.handysiddur.R;
import com.mattaniahbeezy.handysiddur.adapters.ZmanRecyclerAdapter;
import com.mattaniahbeezy.handysiddur.complication.ZmanComplication;
import com.mattaniahbeezy.handysiddur.utilities.LocationPermissionUtility;
import com.mattaniahbeezy.siddurlibrary.hebrewcalendar.ZmanimCalculator;

/**
 * Created by Beezy Works Studios on 6/2/2017.
 */

public class ZmanimActivity  extends Activity implements LocationPermissionUtility.LocationHandled {

    //private static final String Tag = "ZmanimActivity";
    private LocationPermissionUtility locationUtility;
    private ZmanRecyclerAdapter zmanRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zmanim);
        WearableRecyclerView recyclerView = (WearableRecyclerView) findViewById(R.id.zmanimRecycler);
        zmanRecyclerAdapter = new ZmanRecyclerAdapter();
        recyclerView.setAdapter(zmanRecyclerAdapter);
        recyclerView.setCenterEdgeItems(false);
        recyclerView.setLayoutManager(new CurvedChildLayoutManager(this));
        recyclerView.setCircularScrollingGestureEnabled(true);
        recyclerView.setCenterEdgeItems(true);
        locationUtility = new LocationPermissionUtility(this, this);
        locationUtility.connect();
    }

    private void updateComplication() {
        ComponentName providerComponentName = new ComponentName(
                this,
                ZmanComplication.class
        );
        ProviderUpdateRequester providerUpdateRequester = new
                ProviderUpdateRequester(this, providerComponentName);
        providerUpdateRequester.requestUpdateAll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationUtility.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationUtility.connect();
    }

    @Override
    public void locationAvailable(Location location, int locationSource) {
        ZmanimCalculator.getInstance().setLocation(location);
        zmanRecyclerAdapter.notifyDataSetChanged();
        updateComplication();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            locationUtility.connect();
        }
    }
}
