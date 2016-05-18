package com.example.rajatmhetre.popularmovies2.api;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.rajatmhetre.popularmovies2.Module;
import com.example.rajatmhetre.popularmovies2.adapter.MoviesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.example.rajatmhetre.popularmovies2.init;
import com.example.rajatmhetre.popularmovies2.model.Movie;

/**
 * Created by rajatmhetre on 20/08/15.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    public static final String SEPERATOR = "\n";
    Context mContext;
    MoviesAdapter mMoviesAdapter;

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();


    public FetchMoviesTask(Context context, MoviesAdapter adapter) {
        mContext = context;
        mMoviesAdapter = adapter;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        String sortBy = "vote_average.desc";

        if (params.length != 0) {
            sortBy = params[0];
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        try {
            final String MOVIES_BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, sortBy)
                    .appendQueryParameter(API_KEY_PARAM, init.API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            moviesJsonStr = buffer.toString();

            Log.v(LOG_TAG,moviesJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMoviesDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<Movie> getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {

        final String ROOT_MOVIES_LIST = "results";

        final String MOVIE_ID = "id";
        final String MOVIE_POSTER = "poster_path";
        final String ORIGINAL_TITLE = "original_title";
        final String PLOT_SYNOPSIS = "overview";
        final String USER_RATING = "vote_average";
        final String RELEASE_DATE = "release_date";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray movieArray = moviesJson.getJSONArray(ROOT_MOVIES_LIST);

        ArrayList<Movie> resultMovies = new ArrayList<Movie>(movieArray.length());
        for (int i = 0; i < movieArray.length(); i++) {
            String id;
            String imageLink;
            String originalTitle;
            String releaseDate;
            String userRating;
            String plotSynopsis;

            JSONObject movieObject = movieArray.getJSONObject(i);

            id = movieObject.getString(MOVIE_ID);
            imageLink = movieObject.getString(MOVIE_POSTER);
            originalTitle = movieObject.getString(ORIGINAL_TITLE);
            releaseDate = movieObject.getString(RELEASE_DATE);
            userRating = movieObject.getString(USER_RATING);
            plotSynopsis = movieObject.getString(PLOT_SYNOPSIS);

            resultMovies.add(new Movie(
                    id + SEPERATOR +
                            imageLink + SEPERATOR +
                            originalTitle + SEPERATOR +
                            releaseDate + SEPERATOR +
                            userRating + SEPERATOR +
                            plotSynopsis))
            ;

        }

        return resultMovies;

    }

    @Override
    protected void onPostExecute(ArrayList<Movie> result) {

        mMoviesAdapter.mData = result;
        mMoviesAdapter.notifyDataSetChanged();

    }

}