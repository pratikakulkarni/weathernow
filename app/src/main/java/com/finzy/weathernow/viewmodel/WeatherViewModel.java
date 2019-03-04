package com.finzy.weathernow.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.location.Location;
import com.finzy.weathernow.api.repo.WeatherRepo;
import com.finzy.weathernow.api.response.WeatherRes;

public class WeatherViewModel extends AndroidViewModel {

    private LiveData<WeatherRes> currentWeatherLiveData;

    public WeatherViewModel(Application application, Context context, Location location) {
        super(application);
        currentWeatherLiveData = WeatherRepo.getInstance(context)
                .getCurrentWeather(location);
    }

    public LiveData<WeatherRes> getNewsResponseObservable() {
        return currentWeatherLiveData;
    }
}