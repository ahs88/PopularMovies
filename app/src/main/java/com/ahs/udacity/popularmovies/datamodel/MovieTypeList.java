package com.ahs.udacity.popularmovies.datamodel;

/**
 * Created by shetty on 09/12/15.
 */


import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MovieTypeList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<MovieType> results = new ArrayList<MovieType>();

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The results
     */
    public List<MovieType> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<MovieType> results) {
        this.results = results;
    }

}
