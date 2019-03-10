package com.finzy.weathernow.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.finzy.weathernow.R;
import com.finzy.weathernow.api.response.WInfo;
import com.finzy.weathernow.viewholder.ForecastViewHolder;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastViewHolder> {

    private List<WInfo> wInfoList;
    private Activity activity;


    public List<WInfo> getWeatherResList() {
        return wInfoList;
    }

    public void setWeatherResList(List<WInfo> wInfoList) {
        this.wInfoList = wInfoList;
    }

    public ForecastAdapter(List<WInfo> wInfoList, Activity activity) {
        this.wInfoList = wInfoList;
        this.activity = activity;
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_forecast, parent, false);

        return new ForecastViewHolder(itemView, activity);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        WInfo wInfo = wInfoList.get(position);

        holder.bindData(wInfo);
    }

    @Override
    public int getItemCount() {
        return wInfoList.size();
    }
}