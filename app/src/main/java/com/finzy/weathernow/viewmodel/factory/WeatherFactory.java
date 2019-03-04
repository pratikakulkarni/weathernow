package com.finzy.weathernow.viewmodel.factory;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.location.Location;
import com.finzy.weathernow.viewmodel.WeatherViewModel;

public class WeatherFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private Context context;
    private Location location;


    public WeatherFactory(Application application, Context context, Location location) {
        this.mApplication = application;
        this.context = context;
        this.location = location;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new WeatherViewModel(mApplication, context, location);
    }
}