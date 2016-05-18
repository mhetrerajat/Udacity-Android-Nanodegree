package com.example.rajatmhetre.popularmovies2.activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rajatmhetre.popularmovies2.R;
import com.example.rajatmhetre.popularmovies2.Module;
import com.example.rajatmhetre.popularmovies2.adapter.ReviewAdapter;
import com.example.rajatmhetre.popularmovies2.adapter.TrailerAdapter;
import com.example.rajatmhetre.popularmovies2.database.DbContract;
import com.example.rajatmhetre.popularmovies2.api.FetchReviewsTask;
import com.example.rajatmhetre.popularmovies2.api.FetchTrailersTask;

import com.example.rajatmhetre.popularmovies2.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivityFragment extends Fragment {

    TrailerAdapter mTrailersAdapter;
    ReviewAdapter mReviewsAdapter;

    ListView mTrailersListView;
    ListView mReviewssListView;

    DetailViewHolder mViewHolder;

    String moviePostId;
    String moviePostImageName;
    String movieTitle;
    String movieYear;
    String movieRating;
    String movieDescription;

    Movie mMovie;

    boolean mGotData = false;


    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();



        if (null != intent && intent.hasExtra(Intent.EXTRA_TEXT)) {
            mGotData = true;
            String stringExtra = intent.getStringExtra(Intent.EXTRA_TEXT);

            mMovie = new Movie(stringExtra);

        }
        else {
            Bundle arguments = getArguments();
            if (arguments != null) {
                mGotData = true;
                mMovie = new Movie(arguments.getString(Intent.EXTRA_TEXT));
            }
        }

        if(mGotData){
            String moviePostId = Module.extractValueFromMovieInfo(Module.MOVIE_ID_FIELD, mMovie);

            mTrailersAdapter = new TrailerAdapter(getActivity(), null);
            fetchTrailers(moviePostId);

            mReviewsAdapter = new ReviewAdapter(getActivity(), null);
            fetchReviews(moviePostId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        if(mGotData) {

            moviePostId = Module.extractValueFromMovieInfo(Module.MOVIE_ID_FIELD, mMovie);
            moviePostImageName = Module.extractValueFromMovieInfo(Module.MOVIE_POSTER_FIELD, mMovie);
            movieTitle = Module.extractValueFromMovieInfo(Module.MOVIE_TITLE_FIELD, mMovie);
            movieYear = Module.extractValueFromMovieInfo(Module.MOVIE_YEAR_FIELD, mMovie).length() >= 4 ?
                    Module.extractValueFromMovieInfo(Module.MOVIE_YEAR_FIELD, mMovie).substring(0, 4) :
                    "";
            movieRating = Module.extractValueFromMovieInfo(Module.MOVIE_RATING_FIELD, mMovie);
            movieDescription = Module.extractValueFromMovieInfo(Module.MOVIE_DESCRIPTTION_FIELD, mMovie);

            String src = Module.buildPosterImageUrl(moviePostImageName);

            mViewHolder = new DetailViewHolder(rootView);
            Picasso.with(getActivity()).load(src).into(mViewHolder.posterImageView);

            mViewHolder.titleTextView.setText(movieTitle);
            mViewHolder.yearTextView.setText(movieYear);
            mViewHolder.ratingTextView.setText(movieRating);
            mViewHolder.descriptionTextView.setText(movieDescription);

            mViewHolder.favoriteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    setAsFavorite(v);
                }
            });
            mViewHolder.removeFavoriteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    removeFromFavorites(v);
                }
            });

            // Handling showing and hiding button
            handleButtons();

            mTrailersListView = (ListView) rootView.findViewById(R.id.trailer_list);

            mTrailersListView.setAdapter(mTrailersAdapter);

            mTrailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String source = mTrailersAdapter.mData.get(position).mSource;
                    Module.watchYoutubeVideo(source, getActivity());
                }
            });

            mTrailersAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    Module.setListViewHeightBasedOnChildren(mTrailersListView);
                }
            });

            mReviewssListView = (ListView) rootView.findViewById(R.id.reviews_list);

            mReviewssListView.setAdapter(mReviewsAdapter);

            mReviewsAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    Module.setListViewHeightBasedOnChildren(mReviewssListView);
                }
            });

        }

        return rootView;
    }

    private boolean fetchTrailers(String movieId) {
        if (getActivity() == null) return false;

        if (!Module.isNetworkAvailable(getActivity())) {
            String text = "No Internet Connectivity";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getActivity(), text, duration);
            toast.show();
            return false;
        }

        FetchTrailersTask fetchTrailersTask = new FetchTrailersTask(getActivity(), mTrailersAdapter);
        fetchTrailersTask.execute(movieId);

        return true;
    }

    private boolean fetchReviews(String movieId) {
        if (getActivity() == null) return false;

        if (!Module.isNetworkAvailable(getActivity())) {
            String text = "No Internet Connectivity";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getActivity(), text, duration);
            toast.show();
            return false;
        }

        FetchReviewsTask fetchReviewsTask = new FetchReviewsTask(getActivity(), mReviewsAdapter);
        fetchReviewsTask.execute(movieId);

        return true;
    }



    public static class DetailViewHolder {
        public final ImageView posterImageView;
        public final TextView titleTextView;
        public final TextView yearTextView;
        public final TextView ratingTextView;
        public final TextView descriptionTextView;
        public final Button favoriteButton;
        public final Button removeFavoriteButton;

        public DetailViewHolder(View view) {
            posterImageView = (ImageView) view.findViewById(R.id.movies_detail_image);
            titleTextView = (TextView) view.findViewById(R.id.movies_detail_title);
            yearTextView = (TextView) view.findViewById(R.id.movies_detail_year);
            ratingTextView = (TextView) view.findViewById(R.id.movies_detail_rating);
            descriptionTextView = (TextView) view.findViewById(R.id.movies_detail_description);
            favoriteButton = (Button) view.findViewById(R.id.favorite_button);
            removeFavoriteButton = (Button) view.findViewById(R.id.remove_favorite_button);
        }
    }

    public void setAsFavorite(View view) {
        long movieId;

        Cursor movieCursor = getActivity().getContentResolver().query(
                DbContract.MovieEntry.CONTENT_URI,
                new String[]{
                        DbContract.MovieEntry._ID,
                        DbContract.MovieEntry.COLUMN_MOVIE_ID
                },
                DbContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{moviePostId},
                null);

        if (movieCursor.moveToFirst()) {
            int movieIdIndex = movieCursor.getColumnIndex(DbContract.MovieEntry._ID);
            movieId = movieCursor.getLong(movieIdIndex);
        } else {

            ContentValues movieValues = new ContentValues();

            movieValues.put(DbContract.MovieEntry.COLUMN_DESCRIPTION, movieDescription);
            movieValues.put(DbContract.MovieEntry.COLUMN_MOVIE_ID, moviePostId);
            movieValues.put(DbContract.MovieEntry.COLUMN_POSTER, moviePostImageName);
            movieValues.put(DbContract.MovieEntry.COLUMN_RATING, movieRating);
            movieValues.put(DbContract.MovieEntry.COLUMN_TITLE, movieTitle);
            movieValues.put(DbContract.MovieEntry.COLUMN_YEAR, movieYear);

            Uri insertedUri = getActivity().getContentResolver().insert(
                    DbContract.MovieEntry.CONTENT_URI,
                    movieValues
            );

            movieId = ContentUris.parseId(insertedUri);
        }

        movieCursor.close();
        handleButtons();
    }

    public void removeFromFavorites(View view) {

        Cursor movieCursor = getActivity().getContentResolver().query(
                DbContract.MovieEntry.CONTENT_URI,
                new String[]{
                        DbContract.MovieEntry._ID,
                        DbContract.MovieEntry.COLUMN_MOVIE_ID
                },
                DbContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{moviePostId},
                null);

        if (movieCursor.moveToFirst()) {
            getActivity().getContentResolver().delete(
                    DbContract.MovieEntry.CONTENT_URI,
                    DbContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{moviePostId}
            );
        }

        movieCursor.close();

        handleButtons();
    }

    private boolean isFavorite() {

        boolean isFavorite = false;
        Cursor movieCursor = getActivity().getContentResolver().query(
                DbContract.MovieEntry.CONTENT_URI,
                new String[]{
                        DbContract.MovieEntry._ID,
                        DbContract.MovieEntry.COLUMN_MOVIE_ID
                },
                DbContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{moviePostId},
                null);

        if (movieCursor.moveToFirst()) {
            isFavorite = true;
        }

        movieCursor.close();

        return isFavorite;
    }

    private void handleButtons() {
        if (isFavorite()) {
            mViewHolder.favoriteButton.setVisibility(View.GONE);
            mViewHolder.removeFavoriteButton.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.removeFavoriteButton.setVisibility(View.GONE);
            mViewHolder.favoriteButton.setVisibility(View.VISIBLE);
        }
    }


}
