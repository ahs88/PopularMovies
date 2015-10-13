package com.ahs.udacity.popularmovies.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ahs.udacity.popularmovies.activity.fragment.movieDetailFragments.Genre;
import com.ahs.udacity.popularmovies.activity.fragment.movieDetailFragments.Overview;
import com.ahs.udacity.popularmovies.activity.fragment.movieDetailFragments.Ratings;

/**
 * Created by shetty on 09/10/15.
 */
public class MovieDetailPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private static final int DEFAULT_SIZE = 3;
    public enum DetailType{OVERVIEW,GENRE,RATINGS};

    public MovieDetailPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext=context;

    }

    private int baseId;

    @Override
    public Fragment getItem(int position) {
        switch(DetailType.values()[position]){
            case OVERVIEW:
                Overview overview = Overview.newInstance("","");
                return overview;
            case GENRE:
                Genre popularity = Genre.newInstance("", "");
                return popularity;
            case RATINGS:
                Ratings rating = Ratings.newInstance("", "");
                return rating;

        }
        return null;
    }

    @Override
    public int getCount() {
        return DetailType.values().length;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public long getItemId(int position) {
        // give an ID different from position when position has been changed
        return baseId + position;
    }


}
