package com.example.rajatmhetre.popularmovies2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rajatmhetre.popularmovies2.Module;
import com.example.rajatmhetre.popularmovies2.activity.FavoriteMoviesFragment;
import com.squareup.picasso.Picasso;

/**
 * Created by rajatmhetre on 20/08/15.
 */
public class FavoriteMoviesAdapter extends CursorAdapter {

    public FavoriteMoviesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    private static final int VIEW_TYPE = 0;
    private static final int VIEW_TYPE_COUNT = 1;

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    /**
     *
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ImageView imageItemView = new ImageView(context);
        Module.setPosterImageSizeParams(context, imageItemView);
        return imageItemView;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String imageName = cursor.getString(FavoriteMoviesFragment.COL_MOVIE_POSTER);

        Picasso.with(context).load(Module.buildPosterImageUrl(imageName)).into((ImageView)view);
    }
}