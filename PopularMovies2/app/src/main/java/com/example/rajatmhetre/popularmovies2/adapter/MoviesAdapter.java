package com.example.rajatmhetre.popularmovies2.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.rajatmhetre.popularmovies2.Module;

import com.example.rajatmhetre.popularmovies2.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rajatmhetre on 20/08/15.
 */
public class MoviesAdapter extends BaseAdapter {

    public Context mContext;
    public ArrayList<Movie> mData;

    public MoviesAdapter(Context c, ArrayList<Movie> d){
        mContext = c;
        mData = d;
    }

    @Override
    public int getCount() {
        if(mData == null) return 0;
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageItemView;
        if (convertView == null) {

            imageItemView = new ImageView(mContext);
            Module.setPosterImageSizeParams(mContext, imageItemView);
        } else {
            imageItemView = (ImageView) convertView;
        }

        String imageName = Module.extractValueFromMovieInfo(Module.MOVIE_POSTER_FIELD, mData.get(position));

        Picasso.with(mContext).load(Module.buildPosterImageUrl(imageName)).into(imageItemView);


        return imageItemView;
    }

}
