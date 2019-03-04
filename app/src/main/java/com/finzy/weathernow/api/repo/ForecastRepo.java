package com.finzy.weathernow.api.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import com.finzy.weathernow.BuildConfig;
import com.finzy.weathernow.api.APIServices;
import com.finzy.weathernow.api.RestClient;
import com.finzy.weathernow.api.response.ForecastRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastRepo {

    APIServices apiServices;

    private static ForecastRepo INSTANCE = null;


    public static ForecastRepo getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ForecastRepo(context);
        }
        return INSTANCE;
    }

    public ForecastRepo(Context context) {
        apiServices = RestClient.getInstance(context).get();
    }

    public LiveData<ForecastRes> getForecastedWeather(String location) {
        final MutableLiveData<ForecastRes> data = new MutableLiveData<>();
        apiServices.getForecastedWeather(BuildConfig.apiKey, "metric", location)
                .enqueue(new Callback<ForecastRes>() {
                    @Override
                    public void onResponse(Call<ForecastRes> call, Response<ForecastRes> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ForecastRes> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }
}