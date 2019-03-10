package com.finzy.weathernow.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.Bundle;
import androidx.navigation.NavController;
import com.finzy.weathernow.repo.MainActivityRepo;

public class MainActivityViewModel extends AndroidViewModel {

    private LiveData<NavController> navControllerLiveData;
    private Context context;
//    private String query;

    public MainActivityViewModel(Application application, Context context) {
        super(application);
        this.context = context;
//        this.query = query;
    }

    public LiveData<NavController> getNavControllerLiveData(Activity activity, Bundle bundle) {
        navControllerLiveData = MainActivityRepo.getInstance(context)
                .setUpNavigation(activity, bundle);

        return navControllerLiveData;
    }
}