package com.mattaniahbeezy.handysiddur.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Beezy Works Studios on 6/2/2017.
 */

public class DateFormatter {
    private static final DateFormatter ourInstance = new DateFormatter();
    private SimpleDateFormat simpleDateFormat;

   public static DateFormatter getInstance() {
        return ourInstance;
    }

    private DateFormatter() {
     setUseTwentyFourHour(false);
    }

    public void setUseTwentyFourHour(boolean useTwentyFourHour){
        String twentyFourTemplate = "H:mm";
        String twelveHourTemplate = "h:mm";
        simpleDateFormat = new SimpleDateFormat(useTwentyFourHour ? twentyFourTemplate : twelveHourTemplate, Locale.US);
    }

    public String formatTime(Date date){
        return simpleDateFormat.format(date);
    }
}
