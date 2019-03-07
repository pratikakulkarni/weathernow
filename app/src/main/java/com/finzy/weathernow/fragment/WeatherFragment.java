package com.finzy.weathernow.fragment;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.finzy.weathernow.R;
import com.finzy.weathernow.models.PrefLocation;
import com.finzy.weathernow.viewmodel.WeatherViewModel;
import com.finzy.weathernow.viewmodel.factory.WeatherFactory;

import java.util.Objects;

public class WeatherFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    protected LocationManager locationManager;

    @BindView(R.id.textView_Temp)
    TextView textView_Temp;

    @BindView(R.id.textView_Time)
    TextView textView_Time;

    PrefLocation prefLocation;

    public WeatherFragment() {
        // Required empty public constructor
    }

    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        getWeatherUpdates(prefLocation);

        locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //PrefLocation is not enabled so showing default city as Bangalore
            getWeatherUpdates(null);
            return view;
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    PrefLocation prefLocation = new PrefLocation();
                    prefLocation.setLatitide(location.getLatitude());
                    prefLocation.setLongitude(location.getLongitude());

                    WeatherFragment.this.prefLocation = prefLocation;
                    getWeatherUpdates(prefLocation);
                    locationManager.removeUpdates(this);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }

        return view;
    }

    private void getWeatherUpdates(PrefLocation prefLocation) {
        final WeatherViewModel viewModel = ViewModelProviders
                .of(this, new WeatherFactory(getActivity().getApplication(), getActivity(), prefLocation))
                .get(WeatherViewModel.class);

        viewModel.getNewsResponseObservable().observe(this, currentWeather -> {
            if (currentWeather != null) {
                textView_Temp.setText(Double.toString(currentWeather.getMain().getTemp()) + (char) 0x00B0 + "C");
                textView_Time.setText("Now");
                Toast.makeText(getActivity(), currentWeather.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        Bundle bundle = new Bundle();
        bundle.putParcelable("location", prefLocation);

        textView_Time.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_forecastFragment, bundle));
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
