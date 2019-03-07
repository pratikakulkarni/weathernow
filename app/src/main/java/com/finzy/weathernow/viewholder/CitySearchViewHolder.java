package com.finzy.weathernow.viewholder;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.finzy.weathernow.R;
import com.finzy.weathernow.api.response.WeatherRes;

public class CitySearchViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textView_maxTemp)
    public TextView textView_maxTemp;
    @BindView(R.id.textView_minTemp)
    public TextView textView_minTemp;
    @BindView(R.id.textView_Temp)
    public TextView textView_Temp;
    @BindView(R.id.textView_CityInfo)
    public TextView textView_CityInfo;
    @BindView(R.id.item_parent)
    public ConstraintLayout item_parent;


    private Activity activity;

    public CitySearchViewHolder(View view, Activity activity) {
        super(view);
        this.activity = activity;
        ButterKnife.bind(this, view);
    }

    public void bindData(WeatherRes weatherRes) {
        textView_maxTemp.setText(Double.toString(weatherRes.getMain().getTempMax()) + (char) 0x00B0 + "C");
        textView_minTemp.setText(Double.toString(weatherRes.getMain().getTempMin()) + (char) 0x00B0 + "C");
        textView_Temp.setText(Double.toString(weatherRes.getMain().getTemp()) + (char) 0x00B0 + "C");
        textView_CityInfo.setText(weatherRes.getName() + ", " + weatherRes.getSys().getCountry());
    }
}