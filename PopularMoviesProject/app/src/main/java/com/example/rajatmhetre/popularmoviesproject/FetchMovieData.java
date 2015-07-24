package com.example.rajatmhetre.popularmoviesproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class FetchMovieData extends AsyncTask<Void,Void,ArrayList<MovieItem>> {

    private final String LOG_TAG = getClass().getSimpleName();

    private final String TAG_RESULTS = "results";
    private final String TAG_BACKDROP_PATH = "backdrop_path";
    private final String TAG_ORIGINAL_TITLE = "original_title";
    private final String TAG_OVERVIEW = "overview";
    private final String TAG_RELEASE_DATE = "release_date";
    private final String TAG_POSTER_PATH = "poster_path";
    private final String TAG_POPULARITY = "popularity";
    private final String TAG_VOTE_AVERAGE = "vote_average";
    private final String TAG_VOTE_COUNT = "vote_count";
    private final String API_KEY = "xxxxxx";

    private Context context;
    //private GridView movieGrid;
    public static ArrayList<MovieItem> movieDataList;
    //private ProgressBar progressBar;
    private String sortByPreference;

    public AsyncResponse delegate = null;

    public FetchMovieData(Context context, AsyncResponse delegate) {
        this.context = context;
        this.delegate = delegate;
        //this.movieGrid = movieGrid;
        //this.progressBar = progressBar;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //movieGrid.setClickable(false);
        //progressBar.setVisibility(View.VISIBLE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        sortByPreference = preferences.getString("sort_movies","popularity.desc");
    }

    @Override
    protected ArrayList<MovieItem> doInBackground(Void... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;

        JSONArray movieDataArray = null;

        String movieDataJson = null;

        MovieItem movieItem = null;

        movieDataList = new ArrayList<MovieItem>();

        Uri.Builder dataURI = new Uri.Builder();
        dataURI.scheme("http").authority("api.themoviedb.org").appendPath("3").appendPath("discover").appendPath("movie").appendQueryParameter("sort_by",sortByPreference).appendQueryParameter("api_key", API_KEY);

        try{
            URL dataURL = new URL(dataURI.build().toString());

            urlConnection = (HttpURLConnection) dataURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = bufferedReader.readLine()) != null){
                buffer.append(line + "\n");
            }

            movieDataJson = buffer.toString();

            JSONObject movieDataJSONObj = new JSONObject(movieDataJson);
            movieDataArray = movieDataJSONObj.getJSONArray(TAG_RESULTS);

            for(int i=0; i < movieDataArray.length(); i++){
                JSONObject eachMovieObj = movieDataArray.getJSONObject(i);

                String VAL_BACKDROP_PATH = eachMovieObj.getString(TAG_BACKDROP_PATH);
                String VAL_ORIGINAL_TITLE = eachMovieObj.getString(TAG_ORIGINAL_TITLE);
                String VAL_OVERVIEW = eachMovieObj.getString(TAG_OVERVIEW);
                String VAL_RELEASE_DATE = eachMovieObj.getString(TAG_RELEASE_DATE);
                String VAL_POSTER_PATH = eachMovieObj.getString(TAG_POSTER_PATH);
                Double VAL_POPULARITY = eachMovieObj.getDouble(TAG_POPULARITY);
                Double VAL_VOTE_AVERAGE = eachMovieObj.getDouble(TAG_VOTE_AVERAGE);
                Integer VAL_VOTE_COUNT = eachMovieObj.getInt(TAG_VOTE_COUNT);

                movieItem = new MovieItem(VAL_BACKDROP_PATH,VAL_ORIGINAL_TITLE,VAL_OVERVIEW,VAL_RELEASE_DATE,VAL_POSTER_PATH,VAL_POPULARITY,VAL_VOTE_AVERAGE,VAL_VOTE_COUNT);
                movieDataList.add(movieItem);
            }


        }catch(Exception e){
            Log.e(LOG_TAG, "Oops! Something went wrong : " + e.getLocalizedMessage());
        }finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }

            if(bufferedReader != null){
                try{
                    bufferedReader.close();
                }catch(Exception e){
                    Log.e(LOG_TAG, "Oops! Unable to close the reader : " + e.getLocalizedMessage());
                }
            }
        }

        return movieDataList;
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Toast.makeText(context,"Oops! Something made to cancel downloading task.",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(ArrayList<MovieItem> movieItems) {
        super.onPostExecute(movieItems);
        Log.e("MOVIE_DATA_LIST", String.valueOf(movieDataList.size()));
        delegate.getMovieList(movieDataList);
    }

}
