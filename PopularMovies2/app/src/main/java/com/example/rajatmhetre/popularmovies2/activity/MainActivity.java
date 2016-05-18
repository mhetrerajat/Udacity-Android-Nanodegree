package com.example.rajatmhetre.popularmovies2.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.rajatmhetre.popularmovies2.R;
import com.example.rajatmhetre.popularmovies2.adapter.ViewPagerAdapter;
import com.example.rajatmhetre.popularmovies2.widget.SlidingTabLayout;


public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback {
    private static final CharSequence MOST_POPULAR = "Popular";
    private static final CharSequence HIGHEST_RATED = "Rated";
    private static final CharSequence FAVORITES = "Favorites";
    private static final int SECOND_PAGER_TAB_INDEX = 1;
    private static final CharSequence Titles[] = {MOST_POPULAR, HIGHEST_RATED, FAVORITES};
    private static final int NUMBOFTABS = 3;


    Toolbar mToolbar;
    ViewPager mPager;
    ViewPagerAdapter mPagerAdapter;
    SlidingTabLayout mTabs;

    private boolean mTwoPane;

    private static final String DETAILFRAGMENT_TAG = "DFTAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);



        if (findViewById(R.id.movie_detail_container) != null) {

            mTwoPane = true;

        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }


        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, NUMBOFTABS);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);



        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.icons);
            }
        });

        mTabs.setViewPager(mPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMoviesInCurrentTab();
    }

    private void loadMoviesInCurrentTab(){

        if(!(mPagerAdapter.getItem(mPager.getCurrentItem()) instanceof MoviesFragment) ) return;

        String sortBy = getString(R.string.most_popular_value);

        if(mPager.getCurrentItem() == SECOND_PAGER_TAB_INDEX ) sortBy = getString(R.string.highest_rated_value);

        MoviesFragment currentMovieFragment = (MoviesFragment) mPagerAdapter.getItem(mPager.getCurrentItem());
        if (null != currentMovieFragment) currentMovieFragment.onSortChanged(sortBy);
    }

    @Override
    public void onItemSelected(String movieData) {
        if (mTwoPane) {

            Bundle args = new Bundle();
            args.putString(Intent.EXTRA_TEXT, movieData);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, movieData);
            startActivity(intent);
        }
    }
}
