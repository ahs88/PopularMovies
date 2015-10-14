package com.ahs.udacity.popularmovies.datamodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by akshath on 10/10/2015.
 */
public class MovieType {
    private String id;
    @SerializedName("type")
    private String movieType;
    @SerializedName("key")
    private String key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
