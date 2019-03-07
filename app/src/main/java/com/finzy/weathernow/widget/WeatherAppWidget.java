package com.finzy.weathernow.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import com.finzy.weathernow.activity.CitySearchActivity;
import com.finzy.weathernow.utils.WeatherPreferences;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link CitySearchActivity CitySearchActivity}
 */
public class WeatherAppWidget extends AppWidgetProvider {

    public final static String LOG_TAG = WeatherAppWidget.class.getCanonicalName();

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

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
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            WeatherPreferences.deleteTitlePref(context, appWidgetId);
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
}

