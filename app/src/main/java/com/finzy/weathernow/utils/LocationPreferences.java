package com.finzy.weathernow.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.finzy.weathernow.models.PrefLocation;

public class LocationPreferences {

    private static final String PREFS_NAME = "com.finzy.weathernow.model.PrefLocation";
    private static final String PREF_PREFIX_KEY = "_appwidget";
    public static final String LAT = "LAT";
    public static final String LON = "LON";

    // Write the prefix to the SharedPreferences object for this widget
    public static void saveLocationPref(Context context, double lat, double lon) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(LAT + PREF_PREFIX_KEY, Double.toString(lat));
        prefs.putString(LON + PREF_PREFIX_KEY, Double.toString(lon));
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public static PrefLocation loadTitlePref(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String lat = prefs.getString(LAT + PREF_PREFIX_KEY, null);
        String lon = prefs.getString(LON + PREF_PREFIX_KEY, null);

        /*Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        PrefLocation targetLocation = realm.createObject(PrefLocation.class);
        if (lat != null && lon != null) {
            targetLocation.setLatitide(Double.parseDouble(lat));
            targetLocation.setLongitude(Double.parseDouble(lon));
        } else {
            targetLocation.setLatitide(12.9716);
            targetLocation.setLongitude(77.5946);
        }
        realm.commitTransaction();*/

        if (lat == null || lon == null) {
            return null;
        }
        PrefLocation targetLocation = new PrefLocation(Double.parseDouble(lat), Double.parseDouble(lon));

        return targetLocation;
    }

    public static void deleteTitlePref(Context context) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(LAT + PREF_PREFIX_KEY);
        prefs.remove(LON + PREF_PREFIX_KEY);
        prefs.apply();
    }

}
