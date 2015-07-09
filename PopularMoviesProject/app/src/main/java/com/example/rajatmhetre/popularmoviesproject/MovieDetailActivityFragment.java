package com.example.rajatmhetre.popularmoviesproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {


    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent movieIntent = getActivity().getIntent();
        MovieItem movieItem = (MovieItem) movieIntent.getParcelableExtra("movieDetails");

        Uri.Builder moviePosterURI = new Uri.Builder();

        ImageView movieDetailPoster = (ImageView) getActivity().findViewById(R.id.movieDetailPoster);
        TextView movieTitle = (TextView) getActivity().findViewById(R.id.movieTitle);
        TextView movieRelease = (TextView) getActivity().findViewById(R.id.movieRelease);
        TextView moviePopularity = (TextView) getActivity().findViewById(R.id.moviePopularity);
        TextView movieVoteAvg = (TextView) getActivity().findViewById(R.id.movieVoteAvg);
        TextView movieVoteCount = (TextView) getActivity().findViewById(R.id.movieVoteCount);
        TextView movieOverview = (TextView) getActivity().findViewById(R.id.movieOverview);


        String moviePosterURL = moviePosterURI.scheme("http").authority("image.tmdb.org").appendPath("t").appendPath("p").appendPath("w185").appendPath(movieItem.getPosterPath().replace("/", "")).build().toString();
        moviePosterURI.clearQuery();
        Picasso.with(getActivity().getApplicationContext()).load(moviePosterURL).into(movieDetailPoster);

        movieTitle.setText(movieItem.getOriginalTitle());
        movieRelease.setText(movieItem.getReleaseDate());
        moviePopularity.setText(movieItem.getPopularity().toString());
        movieVoteAvg.setText(movieItem.getVoteAvg().toString());
        movieVoteCount.setText(movieItem.getVoteCount().toString());
        movieOverview.setText(movieItem.getOverview());


        //int drawableId = mia.getImageFromList(moviePosterId).drawableId;
        //movieDetailPoster.setImageResource(drawableId);
    }
}
