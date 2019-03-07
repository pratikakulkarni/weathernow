package com.finzy.weathernow.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.finzy.weathernow.R;
import com.finzy.weathernow.activity.CitySearchActivity;
import com.finzy.weathernow.api.response.WeatherRes;
import com.finzy.weathernow.viewholder.CitySearchViewHolder;

import java.util.List;

public class CitySearchAdapter extends RecyclerView.Adapter<CitySearchViewHolder> {

    private List<WeatherRes> weatherResList;
    private Activity activity;


    public List<WeatherRes> getWeatherResList() {
        return weatherResList;
    }

    public void setWeatherResList(List<WeatherRes> weatherResList) {
        this.weatherResList = weatherResList;
    }

    public CitySearchAdapter(List<WeatherRes> weatherResList, Activity activity) {
        this.weatherResList = weatherResList;
        this.activity = activity;
    }

    @Override
    public CitySearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_city_search, parent, false);

        return new CitySearchViewHolder(itemView, activity);
    }

    @Override
    public void onBindViewHolder(CitySearchViewHolder holder, int position) {
        WeatherRes weatherRes = weatherResList.get(position);

        holder.itemView.setOnClickListener(v->{
            if(activity instanceof CitySearchActivity){
                ((CitySearchActivity)activity).setPreferredLocation(weatherResList.get(holder.getAdapterPosition()));
            }
        });

        holder.bindData(weatherRes);
    }

    @Override
    public int getItemCount() {
        return weatherResList.size();
    }


}