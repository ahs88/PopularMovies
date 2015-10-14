package com.ahs.udacity.popularmovies.datamodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by akshath on 10/9/2015.
 */
public class MovieDetail {
    @SerializedName("id")
    private String movieId;
    @SerializedName("title")
    private String movieTitle;
    @SerializedName("genre_ids")
    private List<String> genreIds;
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;

    private String youtubeKey;
    private String posterLink;
    private String thumbNailLink;

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public List<String> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<String> genreIds) {
        this.genreIds = genreIds;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getYoutubeKey() {
        return youtubeKey;
    }

    public void setYoutubeKey(String youtubeKey) {
        this.youtubeKey = youtubeKey;
    }

    public String getPosterLink() {
        return posterLink;
    }

    public void setPosterLink(String posterLink) {
        this.posterLink = posterLink;
    }

    public String getThumbNailLink() {
        return thumbNailLink;
    }

    public void setThumbNailLink(String thumbNailLink) {
        this.thumbNailLink = thumbNailLink;
    }
}
