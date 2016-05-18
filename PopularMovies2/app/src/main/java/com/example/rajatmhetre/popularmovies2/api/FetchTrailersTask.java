package com.example.rajatmhetre.popularmovies2.api;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.rajatmhetre.popularmovies2.Module;
import com.example.rajatmhetre.popularmovies2.adapter.TrailerAdapter;
import com.example.rajatmhetre.popularmovies2.init;
import com.example.rajatmhetre.popularmovies2.model.Trailer;

import org.apache.http.client.methods.HttpGet;
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

/**
 * Created by rajatmhetre on 20/08/15.
 */
public class FetchTrailersTask extends AsyncTask<String, Void, ArrayList<Trailer>> {

    Context mContext;
    TrailerAdapter mTrailersAdapter;

    private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();


    public FetchTrailersTask(Context context, TrailerAdapter adapter){
        mContext = context;
        mTrailersAdapter = adapter;
    }

    @Override
    protected ArrayList<Trailer> doInBackground(String... params) {

        String movieId;

        if (params.length == 0) {
            return null;
        }

        movieId = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String trailersJsonStr = null;

        try {
            // Construct the URL for the API
            final String MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/movie/";

            final String API_KEY_PARAM = "api_key";

            final String TRAILERS_PATH = "trailers";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(movieId)
                    .appendPath(TRAILERS_PATH)
                    .appendQueryParameter(API_KEY_PARAM, init.API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG,builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(HttpGet.METHOD_NAME);
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
            trailersJsonStr = buffer.toString();
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
            return getTrailersDataFromJson(trailersJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Trailer> getTrailersDataFromJson(String moviesJsonStr)
            throws JSONException {

        final String ROOT_TRAILERS_LIST = "youtube";

        final String TRAILER_NAME = "name";
        final String TRAILER_SIZE = "size";
        final String TRAILER_SOURCE = "source";
        final String TRAILER_TYPE = "type";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray movieArray = moviesJson.getJSONArray(ROOT_TRAILERS_LIST);

        ArrayList<Trailer> resulTrailers = new ArrayList<Trailer>(movieArray.length());
        for(int i = 0; i < movieArray.length(); i++) {
            String name;
            String size;
            String source;
            String type;

            JSONObject trailerObject = movieArray.getJSONObject(i);

            name = trailerObject.getString(TRAILER_NAME);
            size = trailerObject.getString(TRAILER_SIZE);
            source = trailerObject.getString(TRAILER_SOURCE);
            type = trailerObject.getString(TRAILER_TYPE);

            resulTrailers.add(new Trailer(name,size,source,type));
        }

        return resulTrailers;

    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> result) {

        mTrailersAdapter.mData = result;
        mTrailersAdapter.notifyDataSetChanged();

    }
}