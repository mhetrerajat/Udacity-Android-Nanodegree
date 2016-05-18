package com.example.rajatmhetre.popularmovies2.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.rajatmhetre.popularmovies2.R;
import com.example.rajatmhetre.popularmovies2.Module;
import com.example.rajatmhetre.popularmovies2.adapter.MoviesAdapter;
import com.example.rajatmhetre.popularmovies2.api.FetchMoviesTask;



public class MoviesFragment extends Fragment {

    private static final String MOVIES_PARCELABLE_KEY = "movies";

    public static final String POPULARITY_DESC = "popularity.desc";
    public static final String VOTE_AVERAGE_DESC = "vote_average.desc";
    public static final String SORT = "sort";

    private MoviesAdapter mMoviesAdapter;
    private GridView mGridView;

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMoviesAdapter = new MoviesAdapter(getActivity(),null);

        if(savedInstanceState == null || !savedInstanceState.containsKey(MOVIES_PARCELABLE_KEY)){
            Bundle bundle = this.getArguments();
           String sortBy = bundle.getString(SORT);

            fetchMovies(sortBy);
        }
        else {
            mMoviesAdapter.mData = savedInstanceState.getParcelableArrayList(MOVIES_PARCELABLE_KEY);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIES_PARCELABLE_KEY, mMoviesAdapter.mData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.grid_view_movies);

        mGridView.setAdapter(mMoviesAdapter);


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String extra = mMoviesAdapter.mData.get(position).dataString;

                ((Callback) getActivity())
                        .onItemSelected(extra);
            }
        });


        return rootView;
    }

    public void onSortChanged(String sortBy) {


        if(fetchMovies(sortBy)){
            mMoviesAdapter.notifyDataSetChanged();
            mGridView.smoothScrollToPosition(0);
        }

    }

    private boolean fetchMovies(String sortBy) {

        if(getActivity() == null) return false;

        if(!Module.isNetworkAvailable(getActivity())){

            String text = "No Internet Connectivity";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getActivity(),text, duration);
            toast.show();
            return false;
        }


        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(getActivity(), mMoviesAdapter);
        fetchMoviesTask.execute(sortBy);
        return true;
    }


    public interface Callback {

        public void onItemSelected(String movieData);
    }
}
