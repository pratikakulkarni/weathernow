package com.finzy.weathernow.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import com.finzy.weathernow.repo.CitySearchRepo;
import com.finzy.weathernow.api.response.CityInfoRes;

public class CitySearchViewModel extends AndroidViewModel {

    private LiveData<CityInfoRes> cityInfoResLiveData;
    private Context context;
//    private String query;

    public CitySearchViewModel(Application application, Context context/*, String query*/) {
        super(application);
        this.context = context;
//        this.query = query;
    }

    public LiveData<CityInfoRes> getCities(String query) {
        cityInfoResLiveData = CitySearchRepo.getInstance(context)
                .findCity(query);

        return cityInfoResLiveData;
    }
}