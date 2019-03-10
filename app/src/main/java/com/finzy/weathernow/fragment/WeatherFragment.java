package com.finzy.weathernow.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.finzy.weathernow.R;
import com.finzy.weathernow.activity.CitySearchActivity;
import com.finzy.weathernow.api.response.WeatherRes;
import com.finzy.weathernow.models.PrefLocation;
import com.finzy.weathernow.utils.*;
import com.finzy.weathernow.viewmodel.WeatherViewModel;
import com.finzy.weathernow.viewmodel.factory.WeatherFactory;
import com.finzy.weathernow.widget.RemoteFetchService;

import java.util.Calendar;

public class WeatherFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.textView_Temp)
    TextView textView_Temp;
    @BindView(R.id.textView_TempOverall)
    TextView textView_TempOverall;
    @BindView(R.id.textView_Weather)
    TextView textView_Weather;
    @BindView(R.id.textView_Location)
    TextView textView_Location;
    @BindView(R.id.imageView_EditLocation)
    ImageView imageView_EditLocation;
    @BindView(R.id.button_viewDetails)
    Button button_viewDetails;
    @BindView(R.id.textView_Wind)
    TextView textView_Wind;
    @BindView(R.id.textView_Humidity)
    TextView textView_Humidity;
    @BindView(R.id.layout_Loading)
    FrameLayout layout_Loading;

    private PrefLocation prefLocation;
    private WeatherRes weatherRes;
    private boolean isLocationChanged = false;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isLocationChanged = getArguments().getBoolean(RemoteFetchService.LOCATION_CHANGED, false);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        prefLocation = LocationPreferences.loadTitlePref(getActivity());
        if (prefLocation == null) {
            prefLocation = new PrefLocation(12.9716, 77.5946);
            Toast.makeText(getActivity(), "Could not get current location. Displaying weather for default city.", Toast.LENGTH_LONG).show();
        }

        imageView_EditLocation.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CitySearchActivity.class);
            startActivity(intent);
        });

        Bundle bundle = new Bundle();
        bundle.putParcelable("location", prefLocation);
        button_viewDetails.setOnClickListener(v -> {
            if (!NetworkUtil.isConnectedToInternet(getActivity())) {
                NetworkUtil.noConnectivityDialog(getActivity());
            } else {
                Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_forecastFragment, bundle).onClick(v);
            }
        });

        weatherRes = WeatherPreferences.loadTitlePref(getActivity());
        long hours = -1;
        if (weatherRes != null) {
            hours = TimeUtil.getDiffInHours(weatherRes.getDt() * 1000, Calendar.getInstance().getTimeInMillis());
        }
        if (!isLocationChanged && hours != -1 && hours < Constants.UPDATE_TIME) {
//            Toast.makeText(getActivity(), "Already up to date.", Toast.LENGTH_SHORT).show();
            setCurrentWeather(weatherRes);
        } else {
            getWeatherUpdates(prefLocation);
        }

        return view;
    }

    private void getWeatherUpdates(PrefLocation prefLocation) {
        layout_Loading.setVisibility(View.VISIBLE);
        final WeatherViewModel viewModel = ViewModelProviders
                .of(this, new WeatherFactory(getActivity().getApplication(), getActivity(), prefLocation))
                .get(WeatherViewModel.class);

        viewModel.getNewsResponseObservable().observe(this, this::setCurrentWeather);
    }

    public void setCurrentWeather(WeatherRes weatherRes) {
        layout_Loading.setVisibility(View.GONE);

        if (weatherRes != null) {
            textView_Temp.setText(Double.toString(weatherRes.getMain().getTemp()) + (char) 0x00B0);
            try {
                textView_Weather.setText(weatherRes.getWeather().get(0).getDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
            textView_TempOverall.setText(Double.toString(weatherRes.getMain().getTempMax()) + (char) 0x00B0 + " / "
                    + Double.toString(weatherRes.getMain().getTempMin()) + (char) 0x00B0);
            textView_Location.setText(weatherRes.getName() + ", " + weatherRes.getSys().getCountry());
            textView_Humidity.setText(Integer.toString(weatherRes.getMain().getHumidity()) + "%");
            textView_Wind.setText(Double.toString(weatherRes.getWind().getSpeed()) + " km/h");
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
