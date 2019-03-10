package com.finzy.weathernow.repo;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.finzy.weathernow.R;

public class MainActivityRepo {

    private static MainActivityRepo INSTANCE = null;

    public static MainActivityRepo getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MainActivityRepo(context);
        }
        return INSTANCE;
    }

    private MainActivityRepo(Context context) {

    }

    public LiveData<NavController> setUpNavigation(Activity activity, Bundle bundle) {
        final MutableLiveData<NavController> navControllerMutableLiveData = new MutableLiveData<>();

        NavController navController = Navigation.findNavController(activity, R.id.fragment_navigation_host);
        navController.popBackStack(R.id.homeFragment, true);
        navController.navigate(R.id.homeFragment, bundle);

        navControllerMutableLiveData.setValue(navController);

        return navControllerMutableLiveData;
    }
}