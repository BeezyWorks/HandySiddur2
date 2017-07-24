package com.mattaniahbeezy.handysiddur.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mattaniahbeezy.handysiddur.R;

/**
 * Created by Beezy Works Studios on 6/1/2017.
 */


public class LocationPermissionUtility implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private final static String Tag = "LocationUtil";
    private GoogleApiClient googleApiClient;
    private Context context;
    private LocationHandled locationHandled;

    public static final int SAVED = 0;
    public static final int RETRIEVED = 1;

    public LocationPermissionUtility(Context context, LocationHandled locationHandled) {
        this.context = context;
        this.locationHandled = locationHandled;
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void connect() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS) {
                googleApiClient.connect();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       disconnect();
                    }
                }, 1*1000*10);
            }
        } else {
            if (context instanceof Activity) {
                Log.d(Tag, "Requesting Permission");
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
            disconnect();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(10);

        //noinspection MissingPermission
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        Resources res = context.getResources();
        editor.putString(res.getString(R.string.longitude_key), String.valueOf(location.getLongitude()));
        editor.putString(res.getString(R.string.latitude_key), String.valueOf(location.getLatitude()));
        editor.putString(res.getString(R.string.elevation_key), String.valueOf(location.getAltitude()));
        editor.apply();
        locationHandled.locationAvailable(location, RETRIEVED);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        locationHandled.locationAvailable(getSavedLocation(context), SAVED);
        disconnect();
    }

    public static boolean hasSavedLocation(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.contains(context.getResources().getString(R.string.elevation_key));
    }

    public static Location getSavedLocation(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        double longitude = Double.valueOf(sharedPreferences.getString(res.getString(R.string.longitude_key), "35.235806"));
        double latitude = Double.valueOf(sharedPreferences.getString(res.getString(R.string.latitude_key), "31.777972"));
        double elevation = Double.valueOf(sharedPreferences.getString(res.getString(R.string.elevation_key), "0"));
        if (elevation <= 0d)
            elevation = 1d;
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAltitude(elevation);
        return location;
    }

    public interface LocationHandled {
        void locationAvailable(Location location, int locationSource);
    }

    public void disconnect() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
        googleApiClient.disconnect();
    }
}