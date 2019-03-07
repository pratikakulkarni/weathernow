package com.finzy.weathernow.viewmodel.factory;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.location.Location;
import com.finzy.weathernow.models.PrefLocation;
import com.finzy.weathernow.viewmodel.ForecastViewModel;
import com.finzy.weathernow.viewmodel.WeatherViewModel;

public class ForecastFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private Context context;
    private PrefLocation prefLocation;

    public ForecastFactory(Application application, Context context, PrefLocation prefLocation) {
        mApplication = application;
        this.context = context;
        this.prefLocation = prefLocation;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ForecastViewModel(mApplication, context, prefLocation);
    }
}