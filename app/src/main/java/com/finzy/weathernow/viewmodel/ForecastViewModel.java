package com.finzy.weathernow.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import com.finzy.weathernow.api.repo.ForecastRepo;
import com.finzy.weathernow.api.response.ForecastRes;

public class ForecastViewModel extends AndroidViewModel {

    private LiveData<ForecastRes> forecastLiveDats;

    public ForecastViewModel(Application application, Context context) {
        super(application);
        forecastLiveDats = ForecastRepo.getInstance(context)
                .getForecastedWeather("Bengaluru,in");
    }

    public LiveData<ForecastRes> getForecaseResponseObservable() {
        return forecastLiveDats;
    }
}