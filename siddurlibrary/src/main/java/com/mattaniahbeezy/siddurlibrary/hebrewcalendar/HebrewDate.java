package com.mattaniahbeezy.siddurlibrary.hebrewcalendar;

import net.sourceforge.zmanim.hebrewcalendar.HebrewDateFormatter;
import net.sourceforge.zmanim.hebrewcalendar.JewishCalendar;
import net.sourceforge.zmanim.hebrewcalendar.JewishDate;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import firebaseconnector.database.UserPrefs;

/**
 * Created by Beezy Works Studios on 6/1/2017.
 */

public class HebrewDate {
    private static final HebrewDate instance = new HebrewDate();
    private static final HebrewDate tomorrowInstance = new HebrewDate(true);
    private JewishCalendar jewishCalendar;
    private JewishCalendar tomorrowJewishCalendar;
    private HebrewDateFormatter hebrewDateFormatter;

    private HashMap<String, Boolean> cachedEvaluations = new HashMap<>();

    private HebrewDate() {
        jewishCalendar = new JewishCalendar();
    }

    private HebrewDate(boolean tomorrow) {
        jewishCalendar = new JewishCalendar();
        tomorrowJewishCalendar = new JewishCalendar();
        tomorrowJewishCalendar.forward();
        if (tomorrow) {
            jewishCalendar.forward();
            tomorrowJewishCalendar.forward();
        }
    }

    public static HebrewDate getInstance() {
        return instance;
    }

    public static HebrewDate getTimeSensitiveInstance() {
        Date sunset = ZmanimCalculator.getInstance().getZmanTime(Zman.SHKIA);
        if (sunset == null || sunset.after(new Date())) {
            return instance;
        }
        return tomorrowInstance;
    }

    public static HebrewDate getTomorrowInstance() {
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
            cachedEvaluations.put(candleLightKey, jewishCalendar.isErevYomTov() || isFriday());
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

    public boolean isChanuka() {
        String chanukaKey = "Chanuka";
        if (!cachedEvaluations.containsKey(chanukaKey)) {
            cachedEvaluations.put(chanukaKey, jewishCalendar.isChanukah());
        }
        return cachedEvaluations.get(chanukaKey);
    }

    public boolean isPesach() {
        String pesachKey = "isPesach";
        if (!cachedEvaluations.containsKey(pesachKey)) {
            cachedEvaluations.put(pesachKey, jewishCalendar.getYomTovIndex() == JewishCalendar.PESACH || isCholHamoedPesach());
        }
        return cachedEvaluations.get(pesachKey);
    }

    public boolean isPesachOne() {
        String pesachOneKey = "PesachOne";
        if (!cachedEvaluations.containsKey(pesachOneKey)) {
            cachedEvaluations.put(pesachOneKey, jewishCalendar.getJewishMonth() == JewishDate.NISSAN && jewishCalendar.getJewishDayOfMonth() == 15);
        }
        return cachedEvaluations.get(pesachOneKey);
    }

    public boolean isPesachTwo() {
        String pesachTwoKey = "PesachTwo";
        if (!cachedEvaluations.containsKey(pesachTwoKey)) {
            cachedEvaluations.put(pesachTwoKey, jewishCalendar.getJewishMonth() == JewishDate.NISSAN && jewishCalendar.getJewishDayOfMonth() == 16);
        }
        return cachedEvaluations.get(pesachTwoKey);
    }

    public boolean isPesachThree() {
        String pesachThreeKey = "PesachThree";
        if (!cachedEvaluations.containsKey(pesachThreeKey)) {
            cachedEvaluations.put(pesachThreeKey, jewishCalendar.getJewishMonth() == JewishDate.NISSAN && jewishCalendar.getJewishDayOfMonth() == 17);
        }
        return cachedEvaluations.get(pesachThreeKey);
    }

    public boolean isPesachFour() {
        String pesachFourKey = "PesachFour";
        if (!cachedEvaluations.containsKey(pesachFourKey)) {
            cachedEvaluations.put(pesachFourKey, jewishCalendar.getJewishMonth() == JewishDate.NISSAN && jewishCalendar.getJewishDayOfMonth() == 18);
        }
        return cachedEvaluations.get(pesachFourKey);
    }

    public boolean isPesachFive() {
        String pesachFiveKey = "PesachFive";
        if (!cachedEvaluations.containsKey(pesachFiveKey)) {
            cachedEvaluations.put(pesachFiveKey, jewishCalendar.getJewishMonth() == JewishDate.NISSAN && jewishCalendar.getJewishDayOfMonth() == 19);
        }
        return cachedEvaluations.get(pesachFiveKey);
    }

    public boolean isPesachSix() {
        String pesachSixKey = "PesachSix";
        if (!cachedEvaluations.containsKey(pesachSixKey)) {
            cachedEvaluations.put(pesachSixKey, jewishCalendar.getJewishMonth() == JewishDate.NISSAN && jewishCalendar.getJewishDayOfMonth() == 20);
        }
        return cachedEvaluations.get(pesachSixKey);
    }

    public boolean isPesachSeven() {
        String pesachSevenKey = "PesachSeven";
        if (!cachedEvaluations.containsKey(pesachSevenKey)) {
            cachedEvaluations.put(pesachSevenKey, jewishCalendar.getJewishMonth() == JewishDate.NISSAN && jewishCalendar.getJewishDayOfMonth() == 21);
        }
        return cachedEvaluations.get(pesachSevenKey);
    }

    public boolean isPesachEight() {
        String pesachEightKey = "PesachEight";
        if (!cachedEvaluations.containsKey(pesachEightKey)) {
            cachedEvaluations.put(pesachEightKey, jewishCalendar.getJewishMonth() == JewishDate.NISSAN && jewishCalendar.getJewishDayOfMonth() == 22);
        }
        return cachedEvaluations.get(pesachEightKey);
    }

    public boolean isCholHamoedPesach() {
        String cholPesachKey = "cholPesach";
        if (!cachedEvaluations.containsKey(cholPesachKey)) {
            cachedEvaluations.put(cholPesachKey, jewishCalendar.getYomTovIndex() == JewishCalendar.CHOL_HAMOED_PESACH);
        }
        return cachedEvaluations.get(cholPesachKey);
    }

    public boolean isShavuos() {
        String shavuosKey = "Shavuos";
        if (!cachedEvaluations.containsKey(shavuosKey)) {
            cachedEvaluations.put(shavuosKey, jewishCalendar.getYomTovIndex() == JewishCalendar.SHAVUOS);
        }
        return cachedEvaluations.get(shavuosKey);
    }

    public boolean isRoshHashana() {
        String roshHashanaKey = "RoshHashana";
        if (!cachedEvaluations.containsKey(roshHashanaKey)) {
            cachedEvaluations.put(roshHashanaKey, jewishCalendar.getYomTovIndex() == JewishCalendar.ROSH_HASHANA);
        }
        return cachedEvaluations.get(roshHashanaKey);
    }

    public boolean isYomKippur() {
        String yomKippurKey = "YomKippur";
        if (!cachedEvaluations.containsKey(yomKippurKey)) {
            cachedEvaluations.put(yomKippurKey, jewishCalendar.getYomTovIndex() == JewishCalendar.YOM_KIPPUR);
        }
        return cachedEvaluations.get(yomKippurKey);
    }

    public boolean isSuccos() {
        String succosKey = "Succos";
        if (!cachedEvaluations.containsKey(succosKey)) {
            cachedEvaluations.put(succosKey, jewishCalendar.getYomTovIndex() == JewishCalendar.SUCCOS || isCholHamoedSuccos());
        }
        return cachedEvaluations.get(succosKey);
    }

    public boolean isCholHamoedSuccos() {
        String cholSuccosKey = "succosCholHamoed";
        if (!cachedEvaluations.containsKey(cholSuccosKey)) {
            cachedEvaluations.put(cholSuccosKey, jewishCalendar.getYomTovIndex() == JewishCalendar.CHOL_HAMOED_SUCCOS);
        }
        return cachedEvaluations.get(cholSuccosKey);
    }

    public boolean isShminiAtzeres() {
        String shminiAtzeresKey = "ShminiAtzeres";
        if (!cachedEvaluations.containsKey(shminiAtzeresKey)) {
            cachedEvaluations.put(shminiAtzeresKey, jewishCalendar.getYomTovIndex() == JewishCalendar.SHEMINI_ATZERES);
        }
        return cachedEvaluations.get(shminiAtzeresKey);
    }

    public boolean isSimchasTorah() {
        String simchasTorahKey = "SimchasTorah";
        if (!cachedEvaluations.containsKey(simchasTorahKey)) {
            cachedEvaluations.put(simchasTorahKey, jewishCalendar.getYomTovIndex() == JewishCalendar.SIMCHAS_TORAH);
        }
        return cachedEvaluations.get(simchasTorahKey);
    }

    public boolean isRoshChodesh() {
        String roshChodeshKey = "RoshChodesh";
        if (!cachedEvaluations.containsKey(roshChodeshKey)) {
            cachedEvaluations.put(roshChodeshKey, jewishCalendar.isRoshChodesh());
        }
        return cachedEvaluations.get(roshChodeshKey);
    }

    public boolean isShabbos() {
        String shabbosKey = "Shabbos";
        if (!cachedEvaluations.containsKey(shabbosKey)) {
            cachedEvaluations.put(shabbosKey, jewishCalendar.getDayOfWeek() == 7);
        }
        return cachedEvaluations.get(shabbosKey);
    }

    public boolean isYaalehVyavo() {
        String yaleVyavoKey = "YaleVyavo";
        if (!cachedEvaluations.containsKey(yaleVyavoKey)) {
            cachedEvaluations.put(yaleVyavoKey, isRoshChodesh() || isPesach() || isShavuos() || isRoshHashana() || isYomKippur() || isSuccos() || isShminiAtzeres() || isSimchasTorah());
        }
        return cachedEvaluations.get(yaleVyavoKey);
    }

    public boolean isMussaf() {
        String isMussafKey = "Mussaf";
        if (!cachedEvaluations.containsKey(isMussafKey)) {
            cachedEvaluations.put(isMussafKey, isYaalehVyavo() || isShabbos());
        }
        return cachedEvaluations.get(isMussafKey);
    }

    public boolean isTishaBAv() {
        String isTishaBavKey = "TishaBav";
        if (!cachedEvaluations.containsKey(isTishaBavKey)) {
            cachedEvaluations.put(isTishaBavKey, jewishCalendar.getYomTovIndex() == JewishCalendar.TISHA_BEAV);
        }
        return cachedEvaluations.get(isTishaBavKey);
    }

    public boolean isSummer() {
        String isSummerKey = "IsSummer";
        if (!cachedEvaluations.containsKey(isSummerKey)) {
            cachedEvaluations.put(isSummerKey, isHebrewDateSummer(jewishCalendar));
        }
        return cachedEvaluations.get(isSummerKey);
    }

    public boolean isSummerNew() {
        String isSummerNewKey = "IsSummerNew";
        if (!cachedEvaluations.containsKey(isSummerNewKey)) {
            cachedEvaluations.put(isSummerNewKey, isSummer() != isHebrewDateSummer(getThirtyDaysAgo()));
        }
        return cachedEvaluations.get(isSummerNewKey);
    }

    private boolean isHebrewDateSummer(JewishCalendar jDate) {
        switch (jDate.getJewishMonth()) {
            // winter months
            case JewishCalendar.CHESHVAN:
            case JewishCalendar.KISLEV:
            case JewishCalendar.TEVES:
            case JewishCalendar.SHEVAT:
            case JewishCalendar.ADAR:
            case JewishCalendar.ADAR_II:
                return false;
            // summer months
            case JewishCalendar.IYAR:
            case JewishCalendar.SIVAN:
            case JewishCalendar.TAMMUZ:
            case JewishCalendar.AV:
            case JewishCalendar.ELUL:
                return true;
            // Border Months
            case JewishCalendar.TISHREI:
                return jDate.getJewishDayOfMonth() < 22;
            case JewishCalendar.NISSAN:
                return jDate.getJewishDayOfMonth() > 14;
        }
        return false;
    }

    public boolean isRain() {
        String isRainKey = "IsRain";
        if (!cachedEvaluations.containsKey(isRainKey)) {
            cachedEvaluations.put(isRainKey, isJewishDateRain(jewishCalendar));
        }
        return cachedEvaluations.get(isRainKey);
    }

    public boolean isRainNew() {
        String isRainNewKey = "IsRainNew";
        if (!cachedEvaluations.containsKey(isRainNewKey)) {
            cachedEvaluations.put(isRainNewKey, isRain() != isJewishDateRain(getThirtyDaysAgo()));
        }
        return cachedEvaluations.get(isRainNewKey);
    }

    private boolean isJewishDateRain(JewishCalendar jDate) {
        return UserPrefs.getInstance().isInIsrael() ? isIsraelRain(jDate) : isChulRain(jDate);
    }

    private boolean isIsraelRain(JewishCalendar jDate) {
        switch (jDate.getJewishMonth()) {
            // Winter months
            case JewishCalendar.KISLEV:
            case JewishCalendar.TEVES:
            case JewishCalendar.SHEVAT:
            case JewishCalendar.ADAR:
            case JewishCalendar.ADAR_II:
                return true;
            // Summer months
            case JewishCalendar.IYAR:
            case JewishDate.SIVAN:
            case JewishCalendar.TAMMUZ:
            case JewishCalendar.AV:
            case JewishCalendar.ELUL:
            case JewishCalendar.TISHREI:
                return false;
            // Border months
            case JewishCalendar.NISSAN:
                return jDate.getJewishDayOfMonth() < 15;
            case JewishCalendar.CHESHVAN:
                return jDate.getJewishDayOfMonth() > 7;
        }
        return false;
    }

    private boolean isChulRain(JewishCalendar jDate) {
        //TODO: this
        return false;
    }

    private JewishCalendar getThirtyDaysAgo() {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_YEAR, -30);
        return new JewishCalendar(today);
    }

    public boolean isTachanun() {
        String tachanunkey = "tachanun";
        if (!cachedEvaluations.containsKey(tachanunkey)) {
            boolean tachanun = true;
            if (jewishCalendar.getJewishMonth() == JewishDate.NISSAN)
                tachanun = false;
            if (isHallel() || isYaalehVyavo() || isTishaBAv())
                tachanun = false;

            if (jewishCalendar.getJewishMonth() == JewishDate.TISHREI && jewishCalendar.getJewishDayOfMonth() > 8)
                tachanun = false;

            if (jewishCalendar.getJewishMonth() == JewishDate.SIVAN && jewishCalendar.getJewishDayOfMonth() < 13)
                tachanun = false;

            int yomTovIndex = jewishCalendar.getYomTovIndex();
            int[] noTachanunHolidays = new int[]{
                    JewishCalendar.TU_BEAV,
                    JewishCalendar.EREV_ROSH_HASHANA,
                    JewishCalendar.EREV_YOM_KIPPUR,
                    JewishCalendar.PURIM,
                    JewishCalendar.SHUSHAN_PURIM
            };
            for (int index : noTachanunHolidays) {
                if (yomTovIndex == index)
                    tachanun = false;
            }
            cachedEvaluations.put(tachanunkey, tachanun);
        }
        return cachedEvaluations.get(tachanunkey);
    }

    public boolean isHallel() {
        String hallelKey = "Hallel";
        if (!cachedEvaluations.containsKey(hallelKey)) {
            cachedEvaluations.put(hallelKey, isPesach() || isShavuos() || isSuccos() || isShminiAtzeres() || isSimchasTorah() || isRoshChodesh() || isChanuka() || isYomHaAtzmaut() || isYomYerushaleim());
        }
        return cachedEvaluations.get(hallelKey);
    }

    public boolean isYomTov() {
        String yomTovKey = "IsYomTov";
        if (!cachedEvaluations.containsKey(yomTovKey)) {
            cachedEvaluations.put(yomTovKey, isSuccosOne() ||
                    (isSuccosTwo() && !UserPrefs.getInstance().isInIsrael()) ||
                    isSimchasTorah() ||
                    isShminiAtzeres() ||
                    isPesachOne() ||
                    (isPesachTwo() && !UserPrefs.getInstance().isInIsrael()) ||
                    isPesachSeven() ||
                    isPesachEight() ||
                    isShavuos() ||
                    isRoshHashana() ||
                    isYomKippur());
        }
        return cachedEvaluations.get(yomTovKey);
    }

    public boolean isOmer() {
        String omerKey = "IsOmer";
        if (!cachedEvaluations.containsKey(omerKey)) {
            cachedEvaluations.put(omerKey, jewishCalendar.getDayOfOmer() > -1);
        }
        return cachedEvaluations.get(omerKey);
    }

    public boolean isLogBomer() {
        String logBomerKey = "LogBomer";
        if (!cachedEvaluations.containsKey(logBomerKey)) {
            cachedEvaluations.put(logBomerKey, jewishCalendar.getDayOfOmer() == 33);
        }
        return cachedEvaluations.get(logBomerKey);
    }

    public boolean isYomHaShoah() {
        String yomHashoahKey = "YomHaShoah";
        if (!cachedEvaluations.containsKey(yomHashoahKey)) {
            cachedEvaluations.put(yomHashoahKey, jewishCalendar.getYomTovIndex() == JewishCalendar.YOM_HASHOAH);
        }
        return cachedEvaluations.get(yomHashoahKey);
    }

    public boolean isYomHaZikaron() {
        String yomHaZikaronKey = "YomHazikraon";
        if (!cachedEvaluations.containsKey(yomHaZikaronKey)) {
            cachedEvaluations.put(yomHaZikaronKey, jewishCalendar.getYomTovIndex() == JewishCalendar.YOM_HAZIKARON);
        }
        return cachedEvaluations.get(yomHaZikaronKey);
    }

    public boolean isHavdala() {
        String havdalaKey = "Havdala";
        if (!cachedEvaluations.containsKey(havdalaKey)) {
            cachedEvaluations.put(havdalaKey, isYomTov() || (isShabbos() && !isErevTishaBav()) || (isTishaBAv() && isSunday()));
        }
        return cachedEvaluations.get(havdalaKey);
    }

    public boolean isTzomTaamuz(){
        String tzomTamuzKey = "TzomTaamuz";
        if (!cachedEvaluations.containsKey(tzomTamuzKey)) {
            cachedEvaluations.put(tzomTamuzKey, jewishCalendar.isTaanis() && jewishCalendar.getJewishMonth() == JewishCalendar.TAMMUZ);
        }
        return cachedEvaluations.get(tzomTamuzKey);
    }

    public boolean isTzomTeves(){
        String tzomTevesKey = "TzomTeves";
        if (!cachedEvaluations.containsKey(tzomTevesKey)) {
            cachedEvaluations.put(tzomTevesKey, jewishCalendar.isTaanis() && jewishCalendar.getJewishMonth() == JewishCalendar.TEVES);
        }
        return cachedEvaluations.get(tzomTevesKey);
    }

    public boolean isTaanisEsther(){
        String taanisEstherKey = "TaanisEsther";
        if (!cachedEvaluations.containsKey(taanisEstherKey)) {
            int month = jewishCalendar.getJewishMonth();
            cachedEvaluations.put(taanisEstherKey, jewishCalendar.isTaanis() && (month==JewishCalendar.ADAR || month==jewishCalendar.ADAR_II));
        }
        return cachedEvaluations.get(taanisEstherKey);
    }

    public boolean isNineDays(){
        String nineDaysKey = "NineDays";
        if (!cachedEvaluations.containsKey(nineDaysKey)) {
            int month = jewishCalendar.getJewishMonth();
            cachedEvaluations.put(nineDaysKey, jewishCalendar.getJewishMonth()==JewishDate.AV && jewishCalendar.getJewishDayOfMonth()<10);
        }
        return cachedEvaluations.get(nineDaysKey);
    }

    public boolean isTuBav(){
        String tuBavKey = "TuBav";
        if (!cachedEvaluations.containsKey(tuBavKey)) {
            cachedEvaluations.put(tuBavKey, jewishCalendar.getYomTovIndex() == JewishCalendar.TU_BEAV);
        }
        return cachedEvaluations.get(tuBavKey);
    }

    public boolean isTzomGedaliah(){
        String tzomGedaliahKey = "TzomGedaliah";
        if (!cachedEvaluations.containsKey(tzomGedaliahKey)) {
            cachedEvaluations.put(tzomGedaliahKey, jewishCalendar.getYomTovIndex() == JewishCalendar.FAST_OF_GEDALYAH);
        }
        return cachedEvaluations.get(tzomGedaliahKey);
    }

    public boolean isErevYomKippur(){
        String erevYomKippurKey = "ErevYomKippur";
        if (!cachedEvaluations.containsKey(erevYomKippurKey)) {
            cachedEvaluations.put(erevYomKippurKey, jewishCalendar.getYomTovIndex() == JewishCalendar.EREV_YOM_KIPPUR);
        }
        return cachedEvaluations.get(erevYomKippurKey);
    }

    public boolean isLDovid(){
        String ldovidKey = "Ldovid";
        if (!cachedEvaluations.containsKey(ldovidKey)) {
            int month = jewishCalendar.getJewishMonth();
            cachedEvaluations.put(ldovidKey, month==JewishCalendar.ELUL || (month == JewishCalendar.TISHREI && jewishCalendar.getJewishDayOfMonth()<23));
        }
        return cachedEvaluations.get(ldovidKey);
    }

    public boolean isErevRoshHashana(){
        String erevRoshHashanaKey = "ErevRoshHashana";
        if (!cachedEvaluations.containsKey(erevRoshHashanaKey)) {
            cachedEvaluations.put(erevRoshHashanaKey, jewishCalendar.getYomTovIndex()==JewishCalendar.EREV_ROSH_HASHANA);
        }
        return cachedEvaluations.get(erevRoshHashanaKey);
    }

    public boolean isEseresYemeiTeshuva(){
        String eseresYemeiTeshuva = "EseresYemeiTeshuva";
        if (!cachedEvaluations.containsKey(eseresYemeiTeshuva)) {
            int month = jewishCalendar.getJewishMonth();
            cachedEvaluations.put(eseresYemeiTeshuva, month == JewishCalendar.TISHREI && jewishCalendar.getJewishDayOfMonth()<11);
        }
        return cachedEvaluations.get(eseresYemeiTeshuva);
    }

    public boolean isErevTishaBav() {
        String erevTishaBav = "ErevTishaBav";
        if (!cachedEvaluations.containsKey(erevTishaBav)) {
            cachedEvaluations.put(erevTishaBav, tomorrowJewishCalendar.getYomTovIndex() == JewishCalendar.TISHA_BEAV);
        }
        return cachedEvaluations.get(erevTishaBav);
    }

    public boolean isSuccosOne() {
        String succosOneKey = "SuccosOne";
        if (!cachedEvaluations.containsKey(succosOneKey)) {
            cachedEvaluations.put(succosOneKey, jewishCalendar.getJewishMonth() == JewishDate.TISHREI && jewishCalendar.getJewishDayOfMonth() == 15);
        }
        return cachedEvaluations.get(succosOneKey);
    }

    public boolean isSuccosTwo() {
        String succosTwoKey = "SuccosTwo";
        if (!cachedEvaluations.containsKey(succosTwoKey)) {
            cachedEvaluations.put(succosTwoKey, jewishCalendar.getJewishMonth() == JewishDate.TISHREI && jewishCalendar.getJewishDayOfMonth() == 16);
        }
        return cachedEvaluations.get(succosTwoKey);
    }

    public boolean isSuccosThree() {
        String succosThreeKey = "SuccosThree";
        if (!cachedEvaluations.containsKey(succosThreeKey)) {
            cachedEvaluations.put(succosThreeKey, jewishCalendar.getJewishMonth() == JewishDate.TISHREI && jewishCalendar.getJewishDayOfMonth() == 17);
        }
        return cachedEvaluations.get(succosThreeKey);
    }

    public boolean isSuccosFour() {
        String SuccousFourKey = "SuccosFour";
        if (!cachedEvaluations.containsKey(SuccousFourKey)) {
            cachedEvaluations.put(SuccousFourKey, jewishCalendar.getJewishMonth() == JewishDate.TISHREI && jewishCalendar.getJewishDayOfMonth() == 18);
        }
        return cachedEvaluations.get(SuccousFourKey);
    }

    public boolean isSuccosFive() {
        String succosFiveKey = "SuccosFive";
        if (!cachedEvaluations.containsKey(succosFiveKey)) {
            cachedEvaluations.put(succosFiveKey, jewishCalendar.getJewishMonth() == JewishDate.TISHREI && jewishCalendar.getJewishDayOfMonth() == 19);
        }
        return cachedEvaluations.get(succosFiveKey);
    }

    public boolean isSuccosSix() {
        String succosSixKey = "SuccosSix";
        if (!cachedEvaluations.containsKey(succosSixKey)) {
            cachedEvaluations.put(succosSixKey, jewishCalendar.getJewishMonth() == JewishDate.TISHREI && jewishCalendar.getJewishDayOfMonth() == 20);
        }
        return cachedEvaluations.get(succosSixKey);
    }

    public boolean isHoshanaRaba() {
        String hoshanaRabbaKey = "HoshanaRaba";
        if (!cachedEvaluations.containsKey(hoshanaRabbaKey)) {
            cachedEvaluations.put(hoshanaRabbaKey, jewishCalendar.getYomTovIndex() == JewishCalendar.HOSHANA_RABBA);
        }
        return cachedEvaluations.get(hoshanaRabbaKey);
    }

    public boolean isFullHallel() {
        String fullHallelKey = "FullHallel";
        if (!cachedEvaluations.containsKey(fullHallelKey)) {
            cachedEvaluations.put(fullHallelKey, isPesachOne() || (isPesachTwo() && !UserPrefs.getInstance().isInIsrael()) || isShavuos() || isSuccos() || isShminiAtzeres() || isSimchasTorah() || isChanuka() || isYomHaAtzmaut() || isYomYerushaleim());
        }
        return cachedEvaluations.get(fullHallelKey);
    }

    public boolean isErevChanuka() {
        String erevChanukaKey = "ErevChaunka";
        if (!cachedEvaluations.containsKey(erevChanukaKey)) {
            cachedEvaluations.put(erevChanukaKey, getHebrewDayOfMonth() == 24 && jewishCalendar.getJewishMonth() == JewishDate.KISLEV);
        }
        return cachedEvaluations.get(erevChanukaKey);
    }

    public boolean isMinchaTachanun() {
        String minchaTahanunKey = "MinchaTachanun";
        if (!cachedEvaluations.containsKey(minchaTahanunKey)) {
            boolean minchaTachanun = isTachanun();
            if (jewishCalendar.isErevRoshChodesh() || jewishCalendar.isErevYomTov() || isErevChanuka() || isFriday()) {
                minchaTachanun = false;
            }
            int tomorrowYomTovIndex = tomorrowJewishCalendar.getYomTovIndex();
            int[] erevNoTachanunHolidays = new int[]{
                    JewishCalendar.TU_BEAV,
                    JewishCalendar.TU_BESHVAT,
                    JewishCalendar.TISHA_BEAV,
                    JewishCalendar.PURIM,
                    JewishCalendar.SHUSHAN_PURIM

            };
            for (int index : erevNoTachanunHolidays) {
                if (tomorrowYomTovIndex == index) {
                    minchaTachanun = false;
                }
            }
            cachedEvaluations.put(minchaTahanunKey, minchaTachanun);
        }
        return cachedEvaluations.get(minchaTahanunKey);
    }

    public boolean isLongTachanun() {
        String longTachanunKey = "longTachanun";
        if (!cachedEvaluations.containsKey(longTachanunKey)) {
            cachedEvaluations.put(longTachanunKey, isMonday() || isThursday());
        }
        return cachedEvaluations.get(longTachanunKey);
    }

    public boolean isLamnateach() {
        String lamnatzeachKey = "Lamnatzeach";
        if (!cachedEvaluations.containsKey(lamnatzeachKey)) {
            cachedEvaluations.put(lamnatzeachKey, !isPurim() && !isHallel() && !isMussaf() && !isErevPesach() && !isTishaBAv() && jewishCalendar.getYomTovIndex() != JewishCalendar.EREV_YOM_KIPPUR);
        }
        return cachedEvaluations.get(lamnatzeachKey);
    }

    public boolean isKriasHatorah() {
        String kriasHatorahKey = "KriasHatorah";
        if (!cachedEvaluations.containsKey(kriasHatorahKey)) {
            cachedEvaluations.put(kriasHatorahKey, isLongTachanun() || isMussaf() || isFastDay() || isPurim() || isChanuka());
        }
        return cachedEvaluations.get(kriasHatorahKey);
    }

    public boolean isPurim() {
        String purimKey = "Purim";
        if (!cachedEvaluations.containsKey(purimKey)) {
            boolean purim = jewishCalendar.getYomTovIndex() == JewishCalendar.PURIM;
            boolean shushanPurim = jewishCalendar.getYomTovIndex() == JewishCalendar.SHUSHAN_PURIM;
            boolean isTodayPurim = UserPrefs.getInstance().isInJerusalem() && shushanPurim ||
                    !UserPrefs.getInstance().isInJerusalem() && purim;
            cachedEvaluations.put(purimKey, isTodayPurim);
        }
        return cachedEvaluations.get(purimKey);
    }

    public boolean isHebrewLeapYear(){
        String hebrewLeapYearKey = "HebrewLeapYear";
        if (!cachedEvaluations.containsKey(hebrewLeapYearKey)) {
            cachedEvaluations.put(hebrewLeapYearKey, jewishCalendar.isJewishLeapYear());
        }
        return cachedEvaluations.get(hebrewLeapYearKey);
    }

    public boolean isPurimKatan() {
        String purimKatanKey = "PurimKatan";
        if (!cachedEvaluations.containsKey(purimKatanKey)) {
            cachedEvaluations.put(purimKatanKey, jewishCalendar.getYomTovIndex() == JewishCalendar.PURIM_KATAN);
        }
        return cachedEvaluations.get(purimKatanKey);
    }

    public boolean isYomHaAtzmaut() {
        String yomHaAtzmautKey = "YomHatzmaut";
        if (!cachedEvaluations.containsKey(yomHaAtzmautKey)) {
            cachedEvaluations.put(yomHaAtzmautKey, jewishCalendar.getYomTovIndex() == JewishCalendar.YOM_HAATZMAUT);
        }
        return cachedEvaluations.get(yomHaAtzmautKey);
    }

    public boolean isYomYerushaleim() {
        String yomYerushaleimKey = "YomYerushaleim";
        if (!cachedEvaluations.containsKey(yomYerushaleimKey)) {
            cachedEvaluations.put(yomYerushaleimKey, jewishCalendar.getYomTovIndex() == JewishCalendar.YOM_YERUSHALAYIM);
        }
        return cachedEvaluations.get(yomYerushaleimKey);
    }

    public boolean isSunday() {
        String sundayKey = "Sunday";
        if (!cachedEvaluations.containsKey(sundayKey)) {
            cachedEvaluations.put(sundayKey, jewishCalendar.getDayOfWeek() == 1);
        }
        return cachedEvaluations.get(sundayKey);
    }

    public boolean isMonday() {
        String mondayKey = "Monday";
        if (!cachedEvaluations.containsKey(mondayKey)) {
            cachedEvaluations.put(mondayKey, jewishCalendar.getDayOfWeek() == 2);
        }
        return cachedEvaluations.get(mondayKey);
    }

    public boolean isTuesday() {
        String tuesdayKey = "Tuesday";
        if (!cachedEvaluations.containsKey(tuesdayKey)) {
            cachedEvaluations.put(tuesdayKey, jewishCalendar.getDayOfWeek() == 3);
        }
        return cachedEvaluations.get(tuesdayKey);
    }

    public boolean isWednesday() {
        String wendesdayKey = "Wendesday";
        if (!cachedEvaluations.containsKey(wendesdayKey)) {
            cachedEvaluations.put(wendesdayKey, jewishCalendar.getDayOfWeek() == 4);
        }
        return cachedEvaluations.get(wendesdayKey);
    }

    public boolean isThursday() {
        String thursdayKey = "Thursday";
        if (!cachedEvaluations.containsKey(thursdayKey)) {
            cachedEvaluations.put(thursdayKey, jewishCalendar.getDayOfWeek() == 5);
        }
        return cachedEvaluations.get(thursdayKey);
    }

    public boolean isFriday() {
        String fridayKey = "Friday";
        if (!cachedEvaluations.containsKey(fridayKey)) {
            cachedEvaluations.put(fridayKey, jewishCalendar.getDayOfWeek() == 6);
        }
        return cachedEvaluations.get(fridayKey);
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
