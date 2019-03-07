package com.finzy.weathernow.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LifecycleService;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import com.finzy.weathernow.R;
import com.finzy.weathernow.api.repo.WeatherRepo;
import com.finzy.weathernow.api.response.WeatherRes;
import com.finzy.weathernow.models.PrefLocation;
import com.finzy.weathernow.utils.LocationPreferences;
import com.finzy.weathernow.utils.WeatherPreferences;

import java.util.Random;

public class RemoteFetchService extends LifecycleService {

    public static final String BROADCAST_ACTION = "DataFetched_StatusReceived";
    private static String LOG_TAG = RemoteFetchService.class.getCanonicalName();


    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

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
        }

        callWeatherAPI(weatherRes -> {
            Log.d(LOG_TAG, "callWeatherAPI :: started");
            if (weatherRes != null) {
                WeatherPreferences.saveLocationPref(RemoteFetchService.this, weatherRes, appWidgetId);
            }

            populateWidget(weatherRes, appWidgetId, this);
        });

        return super.onStartCommand(myIntent, flags, startId);
    }

    public void callWeatherAPI(Observer<WeatherRes> observer) {
        PrefLocation locationMap = LocationPreferences.loadTitlePref(this);

        WeatherRepo.getInstance(this).getCurrentWeather(locationMap).observe(this, observer);
    }

    /**
     * Method which sends broadcast to WidgetProvider so that widget is notified
     * to do necessary action and here action == WidgetProvider.DATA_FETCHED
     */
    private void populateWidget(WeatherRes weatherRes, int widgetId, Context context) {
        Log.d(LOG_TAG, "populateWidget :: started");

        // Register an onClickListener
        Intent clickIntent = new Intent(this.getApplicationContext(),
                WeatherAppWidget.class);

        clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                widgetId);
        int number = (new Random().nextInt(100));

        RemoteViews remoteViews = new RemoteViews(this
                .getApplicationContext().getPackageName(),
                R.layout.weather_app_widget);
        Log.w("WidgetExample", String.valueOf(number));
        // Set the text
        Log.d(LOG_TAG, "weatherRes :: isNull" + weatherRes);

        if (weatherRes != null) {
            Log.d(LOG_TAG, "weatherRes :: " + weatherRes.getMain());

            remoteViews.setTextViewText(R.id.textView_cityName, weatherRes.getName());
            remoteViews.setTextViewText(R.id.textView_maxTemp, weatherRes.getMain().getTempMax() + "\u00ba");
            remoteViews.setTextViewText(R.id.textView_minTemp, weatherRes.getMain().getTempMax() + "\u00ba");
            remoteViews.setTextViewText(R.id.textView_Temp, weatherRes.getMain().getTemp() + "\u00ba");
            remoteViews.setTextViewText(R.id.textView_WindSpeed, "Wind: " + weatherRes.getWind().getSpeed() + "km/hr");
            try {
                remoteViews.setTextViewText(R.id.textView_Weather, weatherRes.getWeather().get(0).getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 0, clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.imageView_Season, pendingIntent);
        appWidgetManager.updateAppWidget(widgetId, remoteViews);

        Intent widgetUpdateIntent = new Intent();
        widgetUpdateIntent.setAction(BROADCAST_ACTION);
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                widgetId);
        sendBroadcast(widgetUpdateIntent);

        this.stopSelf();
    }
}