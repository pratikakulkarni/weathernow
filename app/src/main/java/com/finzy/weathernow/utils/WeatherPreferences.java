package com.finzy.weathernow.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.finzy.weathernow.api.response.WeatherRes;
import com.google.gson.Gson;

public class WeatherPreferences {

    private static final String PREFS_NAME = "com.finzy.weathernow.api.response.WeatherRes";
    private static final String PREF_PREFIX_KEY = "weather_";

    public static void saveLocationPref(Context context, WeatherRes weatherRes, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        Gson gson = new Gson();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, gson.toJson(weatherRes));
        prefs.apply();
    }

    public static WeatherRes loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String weather = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);

        if (weather == null) {
            return null;
        }
        Gson gson = new Gson();

        return gson.fromJson(weather, WeatherRes.class);
    }

    public static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

}
