package com.example.rajatmhetre.popularmoviesproject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rajatmhetre on 21/06/15.
 */
public class MovieItem implements Parcelable{

    private String backdropPath,originalTitle,overview,releaseDate,posterPath;
    private Double popularity,voteAvg;
    private Integer voteCount;

    public MovieItem(String backdropPath, String originalTitle, String overview, String releaseDate, String posterPath,Double popularity, Double voteAvg, Integer voteCount){
        this.backdropPath = backdropPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.popularity = popularity;
        this.voteAvg = voteAvg;
        this.voteCount = voteCount;
    }

    public String getBackdropPath(){
        return backdropPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Double getVoteAvg() {
        return voteAvg;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(backdropPath);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeDouble(popularity);
        dest.writeDouble(voteAvg);
        dest.writeInt(voteCount);

    }

    private MovieItem(Parcel in) {
        backdropPath = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        popularity = in.readDouble();
        voteAvg = in.readDouble();
        voteCount = in.readInt();
    }

    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {

        public MovieItem createFromParcel(Parcel source) {
            return new MovieItem(source);
        }

        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

}
