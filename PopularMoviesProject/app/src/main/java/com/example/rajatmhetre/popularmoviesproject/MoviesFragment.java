package com.example.rajatmhetre.popularmoviesproject;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {

    private MovieItemAdapter movieAdpater;
    private MovieItem currentMovieItem;
    private GridView movieGrid;
    private ProgressBar progressBar;
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;


    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movies_fragment, container, false);

        movieGrid = (GridView) rootView.findViewById(R.id.movieGrid);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

                FetchMovieData movieData = new FetchMovieData(getActivity().getApplicationContext(),movieGrid,progressBar);
                movieData.execute();

            }
        };
        prefs.registerOnSharedPreferenceChangeListener(prefListener);

        FetchMovieData movieData = new FetchMovieData(getActivity().getApplicationContext(),movieGrid,progressBar);
        movieData.execute();


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(),"You just clicked" + position,Toast.LENGTH_SHORT).show();
                //movieAdpater = new MovieItemAdapter(getActivity().getApplicationContext());
                currentMovieItem = (MovieItem) parent.getItemAtPosition(position);

                Intent movieDetailIntent = new Intent(getActivity(), MovieDetailActivity.class);
                movieDetailIntent.putExtra("movieDetails",currentMovieItem);
                getActivity().startActivity(movieDetailIntent);
            }
        });
    }
}

//CustomItem - class used in case of more than one type of view is used.
class CustomItem {
    final int drawableId;

    public CustomItem(int drawableId) {
        this.drawableId = drawableId;
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