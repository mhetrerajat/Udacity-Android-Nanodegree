package com.example.rajatmhetre.popularmoviesproject;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.media.Image;
import android.net.Uri;
import android.os.Parcelable;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

class FetchMovieTaskListener implements AsyncResponse
{
    public static ArrayList<MovieItem> movieList = new ArrayList<MovieItem>();

    FetchMovieTaskListener(ArrayList<MovieItem> movieList) {
        this.movieList = movieList;
    }

    @Override
    public void getMovieList(ArrayList<MovieItem> movieListObject) {
        movieList = movieListObject;
    }
}


public class MoviesFragment extends Fragment{

    private MovieItemAdapter movieAdpater;
    private MovieItem currentMovieItem;
    private GridView movieGrid;
    private ProgressBar progressBar;
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    public ArrayList<MovieItem> movieList = new ArrayList<MovieItem>();

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("FRAGMENT_CREATE", "Create !");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("MovieList", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movies_fragment, container, false);

        setRetainInstance(true);

        movieGrid = (GridView) rootView.findViewById(R.id.movieGrid);
        if(getResources().getConfiguration().orientation == 1){
            movieGrid.setNumColumns(2);
        }
        else if(getResources().getConfiguration().orientation == 2){
            movieGrid.setNumColumns(3);
        }

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    FetchMovieData movieData;
                    movieData = new FetchMovieData(getActivity().getApplicationContext(), new AsyncResponse() {
                        @Override
                        public void getMovieList(ArrayList<MovieItem> movieListObject) {
                            movieList = movieListObject;
                            movieGrid.setAdapter(new MovieItemAdapter(getActivity(), movieList));
                        }
                    });
                    movieData.execute();
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(prefListener);

        if(savedInstanceState == null) {
            FetchMovieData movieData;
            movieData = new FetchMovieData(getActivity().getApplicationContext(), new AsyncResponse() {
                @Override
                public void getMovieList(ArrayList<MovieItem> movieListObject) {
                    movieList = movieListObject;
                    movieGrid.setAdapter(new MovieItemAdapter(getActivity(), movieList));
                }
            });
            movieData.execute();
        }
        else {
            movieList = savedInstanceState.getParcelableArrayList("MovieList");
            movieGrid.setAdapter(new MovieItemAdapter(getActivity(), movieList));
        }

        progressBar.setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                currentMovieItem = (MovieItem) parent.getItemAtPosition(position);

                Intent movieDetailIntent = new Intent(getActivity(), MovieDetailActivity.class);
                movieDetailIntent.putExtra("movieDetails", currentMovieItem);
                getActivity().startActivity(movieDetailIntent);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("FRAGMENT_DESTROY","Destroy!");
    }

}


class ViewHolder{
    ImageView movieGridPoster;
}


//MovieItemAdapter - connect data to view.
class MovieItemAdapter extends BaseAdapter{

    private Context obContext;
    private List<MovieItem> movieItemList = null;
    private LayoutInflater inflater;
    private String moviePosterURL;

    public MovieItemAdapter(Context c){
        inflater = LayoutInflater.from(c);
        this.obContext = c;
    }

    public MovieItemAdapter(Context c,ArrayList<MovieItem> movieItemList){
        inflater = LayoutInflater.from(c);
        this.obContext = c;
        this.movieItemList = movieItemList;
    }


    @Override
    public int getCount() {
        return movieItemList.size();
    }

    @Override
    public MovieItem getItem(int position) {
        return movieItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //ImageView moviePoster;
        ViewHolder holder;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.movie_grid_item, parent, false);

            holder = new ViewHolder();
            holder.movieGridPoster = (ImageView) convertView.findViewById(R.id.movieGridPoster);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //moviePoster = (ImageView) convertView.getTag(R.id.movieGridPoster);

        MovieItem currentMovieItem = this.getItem(position);
        String moviePosterPath = currentMovieItem.getPosterPath().replaceFirst("/", "");

        Uri.Builder moviePosterURI = new Uri.Builder();

        moviePosterURL = moviePosterURI.scheme("http").authority("image.tmdb.org").appendPath("t").appendPath("p").appendPath("w185").appendPath(moviePosterPath).build().toString();
        moviePosterURI.clearQuery();

        Picasso.with(obContext).load(moviePosterURL).into(holder.movieGridPoster);

        return convertView;


    }




}