package com.finzy.weathernow.widget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LifecycleService;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.finzy.weathernow.R;
import com.finzy.weathernow.repo.WeatherRepo;
import com.finzy.weathernow.api.response.WeatherRes;
import com.finzy.weathernow.models.PrefLocation;
import com.finzy.weathernow.utils.*;

import java.util.Calendar;

public class RemoteFetchService extends LifecycleService {

    public static final String BROADCAST_ACTION = "DataFetched_StatusReceived";
    public static final String LOCATION_CHANGED = "LOCATION_CHANGED";
    private static String LOG_TAG = RemoteFetchService.class.getCanonicalName();


    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private boolean isLocationChanged = false;

    /*
     * Retrieve appwidget id from intent it is needed to update widget later
     * initialize our AQuery class
     */
    @Override
    public int onStartCommand(Intent myIntent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand :: started");
        if (myIntent.getExtras() != null) {
            appWidgetId = myIntent.getExtras().getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            isLocationChanged = myIntent.getExtras().getBoolean(
                    LOCATION_CHANGED, false);
        }
        callWeatherAPI();
        return super.onStartCommand(myIntent, flags, startId);
    }

    public void callWeatherAPI() {
        WeatherRes tempWeather = WeatherPreferences.loadTitlePref(this);
        long hours = -1;
        if (tempWeather != null) {
            hours = TimeUtil.getDiffInHours(tempWeather.getDt() * 1000, Calendar.getInstance().getTimeInMillis());
        }
        if (!isLocationChanged && hours != -1 && hours < Constants.UPDATE_TIME) {
            Toast.makeText(this, R.string.already_up_to_date, Toast.LENGTH_SHORT).show();
        } else {
            if (!NetworkUtil.isConnectedToInternet(this)) {
                Toast.makeText(this, R.string.data_connectivity_message, Toast.LENGTH_SHORT).show();
                this.stopSelf();
                return;
            }
            PrefLocation locationMap = LocationPreferences.loadTitlePref(this);
            WeatherRepo.getInstance(this).getCurrentWeather(locationMap).observe(this, weatherRes -> {
                if (weatherRes != null) {
                    updateData(weatherRes);
                }
            });
        }
    }

    public void updateData(WeatherRes weatherRes) {
        Log.d(LOG_TAG, "callWeatherAPI :: started");
        if (weatherRes != null) {
            WeatherPreferences.saveLocationPref(RemoteFetchService.this, weatherRes);
        }

        Intent customBroadcast = new Intent();
        customBroadcast.setAction(BROADCAST_ACTION);
        customBroadcast.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                appWidgetId);
        sendBroadcast(customBroadcast);

        Intent intent = new Intent(this, WeatherAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), WeatherAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

        this.stopSelf();
    }
}