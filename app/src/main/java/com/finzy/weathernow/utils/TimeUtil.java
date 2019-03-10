package com.finzy.weathernow.utils;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    public static String getDateWithServerTimeStamp(long longDate) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(longDate * 1000);
        String date = DateFormat.format("hh:mm aa", cal).toString();
        return date;
    }

    public static String getFormattedDate(long longDate) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(longDate * 1000);
        String date = DateFormat.format("dd MMM yyyy, hh:mm aa", cal).toString();
        return date;
    }

    public static long getDiffInHours(long startDateLong, long endDateLong) {
        Date startDate = new Date(startDateLong);
        Date endDate = new Date(endDateLong);

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

//        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

//        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

//        long elapsedSeconds = different / secondsInMilli;

        return elapsedHours;
    }
}