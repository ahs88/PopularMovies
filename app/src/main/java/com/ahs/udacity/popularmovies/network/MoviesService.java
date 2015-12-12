package com.ahs.udacity.popularmovies.network;

import com.ahs.udacity.popularmovies.datamodel.MovieDetail;
import com.ahs.udacity.popularmovies.datamodel.MovieDetailList;
import com.ahs.udacity.popularmovies.datamodel.MovieType;
import com.ahs.udacity.popularmovies.datamodel.MovieTypeList;

import java.io.Serializable;
import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by shetty on 05/12/15.
 */
public interface MoviesService{
        @GET("/3/movie/popular")
        Call<MovieDetailList> getMovies(@Query("sort_by") String sort_by,@Query("api_key") String api_key,@Query("page") int page_num);

        @GET("3/movie/{movie_id}/videos")
        Call<MovieTypeList> getVideoKey(@Path("movie_id") int movie_id,@Query("api_key") String api_key);


}
