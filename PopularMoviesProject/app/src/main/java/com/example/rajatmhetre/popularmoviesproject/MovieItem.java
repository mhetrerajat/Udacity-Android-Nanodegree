package com.example.rajatmhetre.popularmoviesproject;

import java.io.Serializable;

/**
 * Created by rajatmhetre on 21/06/15.
 */
public class MovieItem implements Serializable{

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
}
