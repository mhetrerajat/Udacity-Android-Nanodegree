package com.example.rajatmhetre.popularmovies2.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rajatmhetre on 19/08/15.
 */
public class Movie implements Parcelable {

    public String dataString;

    public Movie(String data) {
        dataString = data;
    }

    private Movie(Parcel in) {
        dataString = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(dataString);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
