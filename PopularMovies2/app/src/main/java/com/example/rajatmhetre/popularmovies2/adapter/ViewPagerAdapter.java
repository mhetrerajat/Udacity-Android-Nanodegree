package com.example.rajatmhetre.popularmovies2.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.rajatmhetre.popularmovies2.activity.FavoriteMoviesFragment;
import com.example.rajatmhetre.popularmovies2.activity.MoviesFragment;

/**
 * Created by rajatmhetre on 20/08/15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence mTitles[];
    int mNumbOfTabs;

    public ViewPagerAdapter(FragmentManager fm,CharSequence titles[], int numbOfTabs) {
        super(fm);

        this.mTitles = titles;
        this.mNumbOfTabs = numbOfTabs;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0 : {
                MoviesFragment fragment1 = new MoviesFragment();
                Bundle bundle = new Bundle();
                bundle.putString(MoviesFragment.SORT, MoviesFragment.POPULARITY_DESC);
                fragment1.setArguments(bundle);

                return fragment1;
            }
            case 1 : {
                MoviesFragment fragment2 = new MoviesFragment();
                Bundle bundle = new Bundle();
                bundle.putString(MoviesFragment.SORT, MoviesFragment.VOTE_AVERAGE_DESC);
                fragment2.setArguments(bundle);
                return fragment2;
            }
            default:
                return new FavoriteMoviesFragment();
        }

    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mNumbOfTabs;
    }
}