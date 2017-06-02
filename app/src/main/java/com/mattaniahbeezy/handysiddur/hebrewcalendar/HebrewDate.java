package com.mattaniahbeezy.handysiddur.hebrewcalendar;

import net.sourceforge.zmanim.hebrewcalendar.JewishCalendar;
import net.sourceforge.zmanim.hebrewcalendar.JewishDate;

import java.util.HashMap;

/**
 * Created by Beezy Works Studios on 6/1/2017.
 */

public class HebrewDate {
    private static final HebrewDate instance = new HebrewDate();
    private JewishCalendar jewishCalendar;

    private HashMap<String, Boolean> cachedEvaluations = new HashMap<>();

    private HebrewDate(){
        jewishCalendar = new JewishCalendar();
    }

    public static HebrewDate getInstance(){
        return instance;
    }

    public boolean isErevPesach(){
        String erevPesachKey="ErevPesach";
        if(!cachedEvaluations.containsKey(erevPesachKey)){
            cachedEvaluations.put(erevPesachKey, jewishCalendar.getJewishMonth()== JewishDate.NISSAN && jewishCalendar.getJewishDayOfMonth()==14);
        }
        return cachedEvaluations.get(erevPesachKey);
    }

    public boolean isCandleLight(){
        String candleLightKey = "CandleLight";
        if(!cachedEvaluations.containsKey(candleLightKey)){
            cachedEvaluations.put(candleLightKey, jewishCalendar.isErevYomTov() || jewishCalendar.getDayOfWeek()==6);
        }
        return cachedEvaluations.get(candleLightKey);
    }

    public boolean isFastDay(){
        String fastDayKey = "FastDay";
        if(!cachedEvaluations.containsKey(fastDayKey)){
            cachedEvaluations.put(fastDayKey, jewishCalendar.isTaanis());
        }
        return cachedEvaluations.get(fastDayKey);
    }
}
