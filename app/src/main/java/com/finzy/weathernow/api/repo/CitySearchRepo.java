package com.finzy.weathernow.api.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import com.finzy.weathernow.BuildConfig;
import com.finzy.weathernow.api.APIServices;
import com.finzy.weathernow.api.RestClient;
import com.finzy.weathernow.api.response.CityInfoRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitySearchRepo {

    private APIServices apiServices;
    private static CitySearchRepo INSTANCE = null;
    private Call<CityInfoRes> weatherResCall = null;

    public static CitySearchRepo getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CitySearchRepo(context);
        }
        return INSTANCE;
    }

    private CitySearchRepo(Context context) {
        apiServices = RestClient.getInstance(context, false).get();
    }

    public LiveData<CityInfoRes> findCity(String query) {
        final MutableLiveData<CityInfoRes> data = new MutableLiveData<>();

        if (weatherResCall != null) {
            weatherResCall.cancel();
        }

        weatherResCall = apiServices.findCities(BuildConfig.apiKey, query, "like", "population", "metric");
        weatherResCall.enqueue(new Callback<CityInfoRes>() {
            @Override
            public void onResponse(Call<CityInfoRes> call, Response<CityInfoRes> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<CityInfoRes> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}