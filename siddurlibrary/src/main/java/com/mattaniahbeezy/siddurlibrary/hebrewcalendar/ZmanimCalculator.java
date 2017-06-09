package com.mattaniahbeezy.siddurlibrary.hebrewcalendar;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.sourceforge.zmanim.ComplexZmanimCalendar;
import net.sourceforge.zmanim.util.GeoLocation;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by Beezy Works Studios on 6/1/2017.
 */

public class ZmanimCalculator {
    private ComplexZmanimCalendar complexZmanimCalendar;
    private static final ZmanimCalculator ourInstance = new ZmanimCalculator();
    private HashMap<Zman, Date> todayZmanim = new HashMap<>();

    public static ZmanimCalculator getInstance() {
        return ourInstance;
    }

    private ZmanimCalculator() {
        complexZmanimCalendar = new ComplexZmanimCalendar();
    }

    public void setLocation(@NonNull Location location) {
        GeoLocation geoLocation = new GeoLocation("ignore", location.getLatitude(), location.getLongitude(), location.getAltitude(), TimeZone.getDefault());
        complexZmanimCalendar.setGeoLocation(geoLocation);
        generateZmanimList();
    }

    public Zman getUpcomingZman() {
        Date now = new Date();
        for (Zman checkZman : Zman.values()) {
            if (!checkZman.shouldShow(null)) {
                continue;
            }
            Date zmanTime = getZmanTime(checkZman);
            if (zmanTime == null) {
                continue;
            }
            if (zmanTime.after(now)) {
                return checkZman;
            }
        }
        return Zman.CHATZOS_HALAILA;
    }

    public long getShaaZmanisGra(){
        return complexZmanimCalendar.getShaahZmanisGra();
    }

    private void generateZmanimList() {
        for (Zman zman : Zman.values()) {
            generateZmanTime(zman);
        }
    }

    private void generateZmanTime(Zman zman) {
        switch (zman) {
            case ALOS:
                todayZmanim.put(zman, complexZmanimCalendar.getAlosHashachar());
                break;
            case MISHYAKIR:
                todayZmanim.put(zman, complexZmanimCalendar.getMisheyakir11Degrees());
                break;
            case HANEITZ:
                todayZmanim.put(zman, complexZmanimCalendar.getSunrise());
                break;
            case SHMA_MGA:
                todayZmanim.put(zman, complexZmanimCalendar.getSofZmanShmaMGA());
                break;
            case SHMA_GRA:
                todayZmanim.put(zman, complexZmanimCalendar.getSofZmanShmaGRA());
                break;
            case ACHILA_CHOMETZ:
                todayZmanim.put(zman, complexZmanimCalendar.getSofZmanShmaGRA());
                break;
            case BIUR_CHOMETZ:
                todayZmanim.put(zman, complexZmanimCalendar.getSofZmanTfilaGRA());
                break;
            case TEFILA_MGA:
                todayZmanim.put(zman, complexZmanimCalendar.getSofZmanTfilaMGA());
                break;
            case TEFILA_GRA:
                todayZmanim.put(zman, complexZmanimCalendar.getSofZmanTfilaGRA());
                break;
            case CHATZOS_HAYOM:
                todayZmanim.put(zman, complexZmanimCalendar.getChatzos());
                break;
            case MINCHA_GEDOLA:
                todayZmanim.put(zman, complexZmanimCalendar.getMinchaGedola());
                break;
            case MINCHA_KETANA:
                todayZmanim.put(zman, complexZmanimCalendar.getMinchaKetana());
                break;
            case PLAG_HAMINCHA:
                todayZmanim.put(zman, complexZmanimCalendar.getPlagHamincha());
                break;
            case CANDLE_LIGHT:
                todayZmanim.put(zman, complexZmanimCalendar.getCandleLighting());
                break;
            case SHKIA:
                todayZmanim.put(zman, complexZmanimCalendar.getSunset());
                break;
            case SIYUM_HATZOM:
                todayZmanim.put(zman, complexZmanimCalendar.getTzaisGeonim7Point083Degrees());
                break;
            case TZEIS:
                todayZmanim.put(zman, complexZmanimCalendar.getTzais());
                break;
            case TZEIS_RT:
                todayZmanim.put(zman, complexZmanimCalendar.getTzais72Zmanis());
                break;
            case CHATZOS_HALAILA:
                Calendar chatzosNightTime = Calendar.getInstance();
                chatzosNightTime.setTime(complexZmanimCalendar.getChatzos());
                chatzosNightTime.add(Calendar.HOUR_OF_DAY, 12);
                todayZmanim.put(zman, chatzosNightTime.getTime());
                break;
        }
    }

    @Nullable
    public Date getZmanTime(Zman zman) {
        if (!todayZmanim.containsKey(zman)) {
            return null;
        }
        return todayZmanim.get(zman);
    }
}
