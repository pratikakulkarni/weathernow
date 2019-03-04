package com.finzy.weathernow.viewmodel.factory;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import com.finzy.weathernow.viewmodel.ForecastViewModel;
import com.finzy.weathernow.viewmodel.WeatherViewModel;

public class ForecastFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private Context context;


    public ForecastFactory(Application application, Context context) {
        mApplication = application;
        this.context = context;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ForecastViewModel(mApplication, context);
    }
}