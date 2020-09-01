package com.example.spidertask2;

import android.os.Parcel;
import android.os.Parcelable;

public class DataItems implements Parcelable {

    private String description;
    private String title;
    private String nasa_id;

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getNasa_id() {
        return nasa_id;
    }

    protected DataItems(Parcel in) {
        description = in.readString();
        title = in.readString();
        nasa_id = in.readString();
    }

    public static final Creator<DataItems> CREATOR = new Creator<DataItems>() {
        @Override
        public DataItems createFromParcel(Parcel in) {
            return new DataItems(in);
        }

        @Override
        public DataItems[] newArray(int size) {
            return new DataItems[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(title);
        dest.writeString(nasa_id);
    }
}
