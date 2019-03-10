package com.finzy.weathernow.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.finzy.weathernow.R;
import com.finzy.weathernow.adapter.ForecastAdapter;
import com.finzy.weathernow.api.response.ForecastRes;
import com.finzy.weathernow.api.response.WInfo;
import com.finzy.weathernow.models.PrefLocation;
import com.finzy.weathernow.viewmodel.ForecastViewModel;
import com.finzy.weathernow.viewmodel.factory.ForecastFactory;

import java.util.ArrayList;

public class ForecastFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private PrefLocation prefLocation;

    @BindView(R.id.recyclerView_Forecast)
    RecyclerView recyclerView_Forecast;
    @BindView(R.id.textView_CityName)
    TextView textView_CityName;

    @BindView(R.id.button_goBack)
    Button button_goBack;
    @BindView(R.id.layout_Loading)
    FrameLayout layout_Loading;

    private ForecastAdapter forecastAdapter;

    private ForecastViewModel forecastViewModel;
    private LiveData<ForecastRes> forecastResLiveData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            prefLocation = getArguments().getParcelable("location");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        ButterKnife.bind(this, view);

        layout_Loading.setVisibility(View.VISIBLE);

        forecastViewModel = ViewModelProviders
                .of(this, new ForecastFactory(getActivity().getApplication(), getActivity(), prefLocation))
                .get(ForecastViewModel.class);

        forecastResLiveData = forecastViewModel.getForecaseResponseObservable();

        forecastResLiveData.observe(this, forecastRes -> {
            layout_Loading.setVisibility(View.GONE);
            if (forecastRes != null) {
                if (forecastRes.getCity() != null)
                    textView_CityName.setText(forecastRes.getCity().getName());
                setAdapter();
            }
//            Toast.makeText(getActivity(), forecastRes.getCity().getName(), Toast.LENGTH_SHORT).show();
        });

        button_goBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        return view;
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

    private void setAdapter() {
        ArrayList<WInfo> wInfoArrayList = null;
        if (forecastResLiveData != null && forecastResLiveData.getValue() != null) {
            wInfoArrayList = (ArrayList<WInfo>) forecastResLiveData.getValue().getList();
        }
        if (wInfoArrayList == null) {
            wInfoArrayList = new ArrayList<>();
        }

        forecastAdapter = new ForecastAdapter(wInfoArrayList, getActivity());

        recyclerView_Forecast.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_Forecast.setLayoutManager(layoutManager);
        recyclerView_Forecast.setAdapter(forecastAdapter);
    }
}