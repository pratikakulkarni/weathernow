package com.finzy.weathernow.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import com.finzy.weathernow.repo.WeatherRepo;
import com.finzy.weathernow.api.response.WeatherRes;
import com.finzy.weathernow.models.PrefLocation;

public class WeatherViewModel extends AndroidViewModel {

    private LiveData<WeatherRes> currentWeatherLiveData;

    public WeatherViewModel(Application application, Context context, PrefLocation prefLocation) {
        super(application);
        currentWeatherLiveData = WeatherRepo.getInstance(context)
                .getCurrentWeather(prefLocation);
    }

    public LiveData<WeatherRes> getNewsResponseObservable() {
        return currentWeatherLiveData;
    }
}