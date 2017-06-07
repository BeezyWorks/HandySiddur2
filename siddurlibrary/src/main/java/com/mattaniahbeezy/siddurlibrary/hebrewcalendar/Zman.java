package com.mattaniahbeezy.siddurlibrary.hebrewcalendar;

import android.support.annotation.Nullable;

/**
 * Created by Beezy Works Studios on 6/1/2017.
 */

public enum Zman {
    ALOS,
    MISHYAKIR,
    HANEITZ,
    SHMA_MGA,
    SHMA_GRA,
    ACHILA_CHOMETZ,
    BIUR_CHOMETZ,
    TEFILA_MGA,
    TEFILA_GRA,
    CHATZOS_HAYOM,
    MINCHA_GEDOLA,
    MINCHA_KETANA,
    PLAG_HAMINCHA,
    CANDLE_LIGHT,
    SHKIA,
    SIYUM_HATZOM,
    TZEIS,
    TZEIS_RT,
    CHATZOS_HALAILA;

    public String hebrewName() {
        switch (this) {
            case ALOS:
                return "עלות השחר";
            case MISHYAKIR:
                return "משיכיר";
            case HANEITZ:
                return "הנץ החמה";
            case SHMA_MGA:
                return "שמע מג''א";
            case SHMA_GRA:
                return "שמע גר''א";
            case ACHILA_CHOMETZ:
                return "אכילת חמץ";
            case BIUR_CHOMETZ:
                return "ביור חמץ";
            case TEFILA_MGA:
                return "תפילה מג''א";
            case TEFILA_GRA:
                return "תפילה גר''א";
            case CHATZOS_HAYOM:
                return "חצות היום";
            case MINCHA_GEDOLA:
                return "מנחה גדולה";
            case MINCHA_KETANA:
                return "מנחה קטנה";
            case PLAG_HAMINCHA:
                return "פלג המנחה";
            case CANDLE_LIGHT:
                return "הדלקת נרות";
            case SHKIA:
                return "שקיעת החמה";
            case SIYUM_HATZOM:
                return "סיום הצום";
            case TZEIS:
                return "צאת הככבים";
            case TZEIS_RT:
                return "צאת ר''ת";
            case CHATZOS_HALAILA:
                return "חצות הלילה";
        }
        return "I am error";
    }

    public boolean shouldShow(@Nullable HebrewDate hebrewDate) {
        if (hebrewDate == null) {
            hebrewDate = HebrewDate.getInstance();
        }
        switch (this) {
            case ACHILA_CHOMETZ:
            case BIUR_CHOMETZ:
                return hebrewDate.isErevPesach();
            case CANDLE_LIGHT:
                return hebrewDate.isCandleLight();
            case SIYUM_HATZOM:
                return hebrewDate.isFastDay();
        }
        return true;
    }

    public boolean isSpecial(){
        switch (this){
            case SIYUM_HATZOM:
            case CANDLE_LIGHT:
            case ACHILA_CHOMETZ:
            case BIUR_CHOMETZ:
                return true;
        }
        return false;
    }
}
