package com.finzy.weathernow.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PrefLocation implements Parcelable {

    private double latitide;
    private double longitude;

    public PrefLocation() {
    }

    public PrefLocation(double latitide, double longitude) {
        this.latitide = latitide;
        this.longitude = longitude;
    }

    protected PrefLocation(Parcel in) {
        latitide = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<PrefLocation> CREATOR = new Creator<PrefLocation>() {
        @Override
        public PrefLocation createFromParcel(Parcel in) {
            return new PrefLocation(in);
        }

        @Override
        public PrefLocation[] newArray(int size) {
            return new PrefLocation[size];
        }
    };

    public double getLatitide() {
        return latitide;
    }

    public void setLatitide(double latitide) {
        this.latitide = latitide;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitide);
        parcel.writeDouble(longitude);
    }
}
