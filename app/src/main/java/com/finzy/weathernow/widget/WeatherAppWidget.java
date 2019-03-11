package com.finzy.weathernow.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import com.finzy.weathernow.R;
import com.finzy.weathernow.activity.CitySearchActivity;
import com.finzy.weathernow.activity.MainActivity;
import com.finzy.weathernow.api.response.WeatherRes;
import com.finzy.weathernow.utils.TimeUtil;
import com.finzy.weathernow.utils.WeatherPreferences;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link CitySearchActivity CitySearchActivity}
 */
public class WeatherAppWidget extends AppWidgetProvider {

    public final static String LOG_TAG = WeatherAppWidget.class.getCanonicalName();

    public static void updateAppWidget(Context context, int appWidgetId) {

        // Build the intent to call the service
        Intent intent = new Intent(context.getApplicationContext(),
                RemoteFetchService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // Update the widgets via the service
        context.startService(intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            populateWidget(context, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            WeatherPreferences.deleteCurretnWeather(context);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * Method which sends broadcast to WidgetProvider so that widget is notified
     * to do necessary action and here action == WidgetProvider.DATA_FETCHED
     */
    private static void populateWidget(Context context, int widgetId) {
        WeatherRes weatherRes = WeatherPreferences.loadCurretnWeather(context);

        Log.d(LOG_TAG, "populateWidget :: started");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        RemoteViews remoteViews = new RemoteViews(context
                .getApplicationContext().getPackageName(),
                R.layout.weather_app_widget);

        Log.d(LOG_TAG, "populateWidget -> weatherRes :: isNull" + weatherRes);

        if (weatherRes != null) {
            remoteViews.setTextViewText(R.id.textView_cityName, weatherRes.getName() + ", " + weatherRes.getSys().getCountry());
            remoteViews.setTextViewText(R.id.textView_maxTemp, weatherRes.getMain().getTempMax() + "\u00ba");
            remoteViews.setTextViewText(R.id.textView_minTemp, weatherRes.getMain().getTempMax() + "\u00ba");
            remoteViews.setTextViewText(R.id.textView_Temp, weatherRes.getMain().getTemp() + "\u00ba");
            remoteViews.setTextViewText(R.id.textView_WindSpeed, "Wind: " + weatherRes.getWind().getSpeed() + "km/h");
            remoteViews.setTextViewText(R.id.textView_updateAt, "" + TimeUtil.getDateWithServerTimeStamp(weatherRes.getDt()));
            try {
                remoteViews.setTextViewText(R.id.textView_Weather, weatherRes.getWeather().get(0).getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        remoteViews.setOnClickPendingIntent(R.id.imageView_Season, getPendingService(context, widgetId));
        remoteViews.setOnClickPendingIntent(R.id.textView_EditCity, getPendingActivityForCity(context, widgetId));
        remoteViews.setOnClickPendingIntent(R.id.imageView_Info, getPendingActivityForInfo(context, widgetId));

        appWidgetManager.updateAppWidget(widgetId, remoteViews);
    }

    public static PendingIntent getPendingActivityForCity(Context context, int widgetId) {
        Intent clickIntent = new Intent(context.getApplicationContext(),
                CitySearchActivity.class);
        clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                widgetId);

        return PendingIntent.getActivity(
                context.getApplicationContext(), 0, clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getPendingActivityForInfo(Context context, int widgetId) {
        Intent clickIntent = new Intent(context.getApplicationContext(),
                MainActivity.class);
        clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                widgetId);

        return PendingIntent.getActivity(
                context.getApplicationContext(), 0, clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getPendingService(Context context, int widgetId) {
        Intent clickIntent = new Intent(context.getApplicationContext(),
                RemoteFetchService.class);
        clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                widgetId);

        return PendingIntent.getService(
                context.getApplicationContext(), 0, clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}