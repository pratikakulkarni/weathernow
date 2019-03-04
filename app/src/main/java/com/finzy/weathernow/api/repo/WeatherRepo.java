package com.finzy.weathernow.api.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import com.finzy.weathernow.BuildConfig;
import com.finzy.weathernow.api.APIServices;
import com.finzy.weathernow.api.RestClient;
import com.finzy.weathernow.api.response.WeatherRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Objects;

public class WeatherRepo {

    APIServices apiServices;

    private static WeatherRepo INSTANCE = null;

    protected LocationManager locationManager;


    public static WeatherRepo getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new WeatherRepo(context);
        }
        return INSTANCE;
    }

    public WeatherRepo(Context context) {
        apiServices = RestClient.getInstance(context).get();
        locationManager = (LocationManager) Objects.requireNonNull(context).getSystemService(Context.LOCATION_SERVICE);

    }

    public LiveData<WeatherRes> getCurrentWeather(Location location) {

        final MutableLiveData<WeatherRes> data = new MutableLiveData<>();

        Call<WeatherRes> weatherResCall = null;

        if (location == null) {
            weatherResCall = apiServices.getCurrentWeather(BuildConfig.apiKey, "metric", 12.9716, 77.5946);
        } else {
            weatherResCall = apiServices.getCurrentWeather(BuildConfig.apiKey, "metric", location.getLatitude(), location.getLongitude());
        }


        weatherResCall.enqueue(new Callback<WeatherRes>() {
            @Override
            public void onResponse(Call<WeatherRes> call, Response<WeatherRes> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherRes> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}