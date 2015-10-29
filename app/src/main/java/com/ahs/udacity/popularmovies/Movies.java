package com.ahs.udacity.popularmovies;

import android.app.Application;
import android.graphics.Movie;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shetty on 23/10/15.
 */
public class Movies extends Application {
    private static final String TAG = "Movies";
    private static Map<Integer,String> genreMap;
    private static Movies movies;

    public static Movies getInstance()
    {
        if(movies == null) {
            movies = new Movies();
        }
        return movies;
    }

    @Override
    public void onCreate() {

        Log.d(TAG,"application onCreate");
        initGenreMap();
        super.onCreate();
    }

    //TODO create genre table with id and movie name and convert Uppercase to lowercase and replace space with underscore to retrieve corresponding image
    private void initGenreMap() {

        genreMap = new HashMap<>();
        genreMap.put(28,"action");
        genreMap.put(12,"adventure");
        genreMap.put(16,"animation");
        genreMap.put(35,"comedy");
        genreMap.put(80,"crime");
        genreMap.put(99,"documentary");
        genreMap.put(14,"drama");
        genreMap.put(10751,"family");
        genreMap.put(18,"fantasy");
        genreMap.put(10769,"foreign");
        genreMap.put(36,"history");
        genreMap.put(27,"horror");
        genreMap.put(10402,"music");
        genreMap.put(9648,"mystery");
        genreMap.put(10749,"romance");
        genreMap.put(878,"science_fiction");
        genreMap.put(1770,"tv_movie");
        genreMap.put(53,"thriller");
        genreMap.put(10752,"war");
        genreMap.put(37,"western");

    }


    public String getImageName(int genre_id)
    {
        String imageName = genreMap.get(new Integer(genre_id));
        Log.d(TAG, "getImageName genre_id:"+genre_id+" imageName:"+imageName);
        return imageName;
    }

}
