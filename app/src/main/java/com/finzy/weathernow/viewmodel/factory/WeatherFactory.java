package com.finzy.weathernow.viewmodel.factory;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import com.finzy.weathernow.models.PrefLocation;
import com.finzy.weathernow.viewmodel.WeatherViewModel;

public class WeatherFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private Context context;
    private PrefLocation prefLocation;


    public WeatherFactory(Application application, Context context, PrefLocation prefLocation) {
        this.mApplication = application;
        this.context = context;
        this.prefLocation = prefLocation;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new WeatherViewModel(mApplication, context, prefLocation);
    }
}