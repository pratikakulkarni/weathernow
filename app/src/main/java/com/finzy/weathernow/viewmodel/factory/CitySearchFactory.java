package com.finzy.weathernow.viewmodel.factory;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.location.Location;
import com.finzy.weathernow.viewmodel.CitySearchViewModel;
import com.finzy.weathernow.viewmodel.WeatherViewModel;

public class CitySearchFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private Context context;
//    private String query;


    public CitySearchFactory(Application application, Context context/*, String query*/) {
        this.mApplication = application;
        this.context = context;
//        this.query = query;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new CitySearchViewModel(mApplication, context/*, query*/);
    }
}