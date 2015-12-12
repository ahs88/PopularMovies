package com.ahs.udacity.popularmovies.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ahs.udacity.popularmovies.activity.fragment.movieDetailFragments.Genre;
import com.ahs.udacity.popularmovies.activity.fragment.movieDetailFragments.Overview;
import com.ahs.udacity.popularmovies.activity.fragment.movieDetailFragments.Ratings;
import com.ahs.udacity.popularmovies.datamodel.MovieDetail;

import java.util.ArrayList;

/**
 * Created by shetty on 09/10/15.
 */
public class MovieDetailPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private static final int DEFAULT_SIZE = 3;
    public enum DetailType{OVERVIEW,RATINGS,GENRE};
    private MovieDetail movieDetail;

    public MovieDetailPagerAdapter(Context context, FragmentManager fm,MovieDetail movie_detail) {
        super(fm);
        mContext=context;
        movieDetail = movie_detail;
    }

    private int baseId;

    @Override
    public Fragment getItem(int position) {
        switch(DetailType.values()[position]){
            case OVERVIEW:
                Overview overview = Overview.newInstance(movieDetail.getOverview());
                return overview;
            case RATINGS:
                Ratings rating = Ratings.newInstance(movieDetail.getRating(), movieDetail.getPopularity(),movieDetail.getReleaseDate());
                return rating;
            case GENRE:
                Genre popularity = Genre.newInstance((ArrayList)movieDetail.getGenreIds());
                return popularity;

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
