package com.mattaniahbeezy.siddurlibrary.hebrewcalendar;

import net.sourceforge.zmanim.hebrewcalendar.HebrewDateFormatter;
import net.sourceforge.zmanim.hebrewcalendar.JewishCalendar;
import net.sourceforge.zmanim.hebrewcalendar.JewishDate;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Beezy Works Studios on 6/1/2017.
 */

public class HebrewDate {
    private static final HebrewDate instance = new HebrewDate();
    private static final HebrewDate tomorrowInstance = new HebrewDate(true);
    private JewishCalendar jewishCalendar;
    private HebrewDateFormatter hebrewDateFormatter;

    private HashMap<String, Boolean> cachedEvaluations = new HashMap<>();

    private HebrewDate() {
        jewishCalendar = new JewishCalendar();
    }

    private HebrewDate(boolean tomorrow) {
        jewishCalendar = new JewishCalendar();
        if (tomorrow) {
            jewishCalendar.forward();
        }
    }

    public static HebrewDate getInstance() {
        return instance;
    }
    public static HebrewDate getTimeSensitiveInstance(){
        Date sunset = ZmanimCalculator.getInstance().getZmanTime(Zman.SHKIA);
        if(sunset==null || sunset.after(new Date())) {
            return instance;
        }
        return tomorrowInstance;
    }

    private HebrewDateFormatter getHebrewDateFormatter() {
        if (hebrewDateFormatter == null) {
            hebrewDateFormatter = new HebrewDateFormatter();
            hebrewDateFormatter.setUseGershGershayim(false);
            hebrewDateFormatter.setHebrewFormat(true);
        }
        return hebrewDateFormatter;
    }

    public boolean isErevPesach() {
        String erevPesachKey = "ErevPesach";
        if (!cachedEvaluations.containsKey(erevPesachKey)) {
            cachedEvaluations.put(erevPesachKey, jewishCalendar.getJewishMonth() == JewishDate.NISSAN && jewishCalendar.getJewishDayOfMonth() == 14);
        }
        return cachedEvaluations.get(erevPesachKey);
    }

    public boolean isCandleLight() {
        String candleLightKey = "CandleLight";
        if (!cachedEvaluations.containsKey(candleLightKey)) {
            cachedEvaluations.put(candleLightKey, jewishCalendar.isErevYomTov() || jewishCalendar.getDayOfWeek() == 6);
        }
        return cachedEvaluations.get(candleLightKey);
    }

    public boolean isFastDay() {
        String fastDayKey = "FastDay";
        if (!cachedEvaluations.containsKey(fastDayKey)) {
            cachedEvaluations.put(fastDayKey, jewishCalendar.isTaanis());
        }
        return cachedEvaluations.get(fastDayKey);
    }

    public int getHebrewDayOfMonth() {
        return jewishCalendar.getJewishDayOfMonth();
    }

    public String formatNumberToGematria(int number) {
        return getHebrewDateFormatter().formatHebrewNumber(number);
    }

    public String getHebrewMonthName() {
        return getHebrewDateFormatter().formatMonth(jewishCalendar);
    }
}
