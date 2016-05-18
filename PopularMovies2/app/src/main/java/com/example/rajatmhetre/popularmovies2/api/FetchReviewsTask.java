package com.example.rajatmhetre.popularmovies2.api;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.rajatmhetre.popularmovies2.adapter.ReviewAdapter;
import com.example.rajatmhetre.popularmovies2.init;
import com.example.rajatmhetre.popularmovies2.model.Review;
import com.example.rajatmhetre.popularmovies2.Module;

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
public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<Review>> {

    Context mContext;
    ReviewAdapter mReviewsAdapter;

    private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();


    public FetchReviewsTask(Context context, ReviewAdapter adapter) {
        mContext = context;
        mReviewsAdapter = adapter;
    }

    @Override
    protected ArrayList<Review> doInBackground(String... params) {

        String movieId;

        if (params.length == 0) {
            return null;
        }

        movieId = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String trailersJsonStr = null;

        try {
            final String MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/movie/";

            final String API_KEY_PARAM = "api_key";

            final String REVIEWS_PATH = "reviews";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(movieId)
                    .appendPath(REVIEWS_PATH)
                    .appendQueryParameter(API_KEY_PARAM, init.API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG,builtUri.toString());

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
            trailersJsonStr = buffer.toString();

            Log.v(LOG_TAG, trailersJsonStr);
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
            return getReviewsDataFromJson(trailersJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<Review> getReviewsDataFromJson(String reviewsJsonStr)
            throws JSONException {


        final String ROOT_REVIEWS_LIST = "results";

        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";

        JSONObject reviewJson = new JSONObject(reviewsJsonStr);
        JSONArray reviewArray = reviewJson.getJSONArray(ROOT_REVIEWS_LIST);

        ArrayList<Review> reviews = new ArrayList<Review>(reviewArray.length());
        for (int i = 0; i < reviewArray.length(); i++) {
            String author;
            String content;

            JSONObject trailerObject = reviewArray.getJSONObject(i);

            author = trailerObject.getString(REVIEW_AUTHOR);
            content = trailerObject.getString(REVIEW_CONTENT);

            reviews.add(new Review(author, content));
        }

        return reviews;

    }

    @Override
    protected void onPostExecute(ArrayList<Review> result) {

        mReviewsAdapter.mData = result;
        mReviewsAdapter.notifyDataSetChanged();

    }

}