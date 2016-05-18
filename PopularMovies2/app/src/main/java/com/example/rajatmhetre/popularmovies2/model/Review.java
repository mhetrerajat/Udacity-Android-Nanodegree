package com.example.rajatmhetre.popularmovies2.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rajatmhetre on 19/08/15.
 */
public class Review implements Parcelable {

    public String mAuthor;
    public String mContent;

    public Review(String author, String content) {
        mAuthor = author;
        mContent = content;
    }

    private Review(Parcel in) {
        String[] data = new String[2];
        in.readStringArray(data);

        mAuthor = data[0];
        mContent = data[1];

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{
                mAuthor,
                mContent
        });
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {

        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }
    };
}
