package com.finzy.weathernow.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.location.Location;
import com.finzy.weathernow.api.repo.ForecastRepo;
import com.finzy.weathernow.api.response.ForecastRes;
import com.finzy.weathernow.models.PrefLocation;

public class ForecastViewModel extends AndroidViewModel {

    private LiveData<ForecastRes> forecastLiveDats;

    public ForecastViewModel(Application application, Context context, PrefLocation prefLocation) {
        super(application);
        forecastLiveDats = ForecastRepo.getInstance(context)
                .getForecastedWeather(prefLocation);
    }

    public LiveData<ForecastRes> getForecaseResponseObservable() {
        return forecastLiveDats;
    }
}