package com.ahs.udacity.popularmovies.datamodel;

import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.ahs.udacity.popularmovies.provider.DbManager;
import com.ahs.udacity.popularmovies.provider.MovieContract;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by akshath on 10/9/2015.
 */
public class MovieDetail implements Parcelable{

    private static final String TAG = MovieDetail.class.getCanonicalName();
    @SerializedName("id")
    private int movieId;
    @SerializedName("title")
    private String movieTitle;
    @SerializedName("genre_ids")
    private List<String> genreIds = new ArrayList<>();
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;

    private String youtubeKey;
    @SerializedName("poster_path")
    private String posterLink;
    @SerializedName("backdrop_path")
    private String thumbNailLink;
    @SerializedName("vote_count")
    private String popularity;
    @SerializedName("vote_average")
    private String rating;

    //constants to map setters and getter funcitons

    private static final String MOVIEID= MovieContract.Entry._ID;
    private static final String MOVIETITLE=MovieContract.Entry.COLUMN_MOVIE_TITLE;
    private static final String GENREID=MovieContract.Entry.COLUMN_GENRE;
    private static final String OVERVIEW= MovieContract.Entry.COLUMN_MOVIE_OVERVIEW;
    private static final String RELEASEDATE=MovieContract.Entry.COLUMN_MOVIE_RELEASE_DATE;
    private static final String YOUTUBEKEY=MovieContract.Entry.COLUMN_YOUTUBE_VIDEO_KEY;
    private static final String POSTERLINK=MovieContract.Entry.COLUMN_POSTER_LINK;
    private static final String THUMBNAIL=MovieContract.Entry.COLUMN_THUMBNAIL_LINK;
    private static final String POPULARITY =MovieContract.Entry.COLUMN_MOVIE_POPULARITY ;
    private static final String RATING = MovieContract.Entry.COLUMN_MOVIE_RATING;

    private Map<String,Method> mGetterMethodMap=new HashMap<>();

    //private Map<String,Method> mAdderMethodMap;
    private Map<String,Method> mSetterMethodMap= new HashMap<>();

    public MovieDetail(){
        try {
            initMethodMap();
           // initializeDefaultValue();
        }catch (Exception e){

        }
    }

    private void initializeDefaultValue() {

    }


    private List<String> detailKeyList= Arrays.asList(MOVIEID,MOVIETITLE,OVERVIEW,RELEASEDATE,YOUTUBEKEY,POSTERLINK,THUMBNAIL,POPULARITY,RATING,GENREID);

    public MovieDetail(Parcel parcel) throws NoSuchMethodException {
        initMethodMap();
        movieId = parcel.readInt();
        String []arr = new String[detailKeyList.size()-2];
                parcel.readStringArray(arr);
        int i=1;
        for(String key:arr){
            try{
                Log.d(TAG,"MovieDetail:"+detailKeyList.get(i)+" method name:"+getmSetterMethodMap().get(detailKeyList.get(i)));
                    getmSetterMethodMap().get(detailKeyList.get(i)).invoke(this,key);
                    i++;

            }catch (IllegalArgumentException ile){

            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        parcel.readList(genreIds, MovieDetail.class.getClassLoader());

    }


    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
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

    private void initMethodMap() throws NoSuchMethodException {

        //getter method map creation
        //mGetterMethodMap = new HashMap<>();
        mGetterMethodMap.put(MOVIEID,MovieDetail.class.getMethod("getMovieId"));
        mGetterMethodMap.put(MOVIETITLE,MovieDetail.class.getMethod("getMovieTitle"));
        mGetterMethodMap.put(GENREID,MovieDetail.class.getMethod("getGenreIds"));
        mGetterMethodMap.put(OVERVIEW,MovieDetail.class.getMethod("getOverview"));
        mGetterMethodMap.put(RELEASEDATE,MovieDetail.class.getMethod("getReleaseDate"));
        mGetterMethodMap.put(YOUTUBEKEY,MovieDetail.class.getMethod("getYoutubeKey"));
        mGetterMethodMap.put(POSTERLINK,MovieDetail.class.getMethod("getPosterLink"));
        mGetterMethodMap.put(THUMBNAIL,MovieDetail.class.getMethod("getThumbNailLink"));
        mGetterMethodMap.put(POPULARITY,MovieDetail.class.getMethod("getPopularity"));
        mGetterMethodMap.put(RATING,MovieDetail.class.getMethod("getRating"));

        //args for setter method
        Class[] cStringArgs = new Class[1];
        cStringArgs[0] = String.class;

        Class[] cListArgs = new Class[1];
        cListArgs[0] = List.class;

        Class[] cIntArgs = new Class[1];
        cIntArgs[0] = int.class;

        //setter method map creation
        //mSetterMethodMap = new HashMap<>();
        mSetterMethodMap.put(MOVIEID,MovieDetail.class.getMethod("setMovieId", cIntArgs));
        mSetterMethodMap.put(MOVIETITLE,MovieDetail.class.getMethod("setMovieTitle", String.class));
        mSetterMethodMap.put(GENREID,MovieDetail.class.getMethod("setGenreIds",cListArgs));
        mSetterMethodMap.put(OVERVIEW,MovieDetail.class.getMethod("setOverview",cStringArgs));
        mSetterMethodMap.put(RELEASEDATE,MovieDetail.class.getMethod("setReleaseDate",cStringArgs));
        mSetterMethodMap.put(YOUTUBEKEY,MovieDetail.class.getMethod("setYoutubeKey",cStringArgs));
        mSetterMethodMap.put(POSTERLINK,MovieDetail.class.getMethod("setPosterLink",cStringArgs));
        mSetterMethodMap.put(THUMBNAIL,MovieDetail.class.getMethod("setThumbNailLink",cStringArgs));
        mSetterMethodMap.put(POPULARITY,MovieDetail.class.getMethod("setPopularity", String.class));
        mSetterMethodMap.put(RATING,MovieDetail.class.getMethod("setRating", String.class));
    }

    public List<String> getDetailKeyList() {
        return detailKeyList;
    }


    public Map<String, Method> getmGetterMethodMap() {
        return mGetterMethodMap;
    }

    public void setmGetterMethodMap(Map<String, Method> mGetterMethodMap) {
        this.mGetterMethodMap = mGetterMethodMap;
    }

    public void setmSetterMethodMap(Map<String, Method> mSetterMethodMap) {
        this.mSetterMethodMap = mSetterMethodMap;
    }

    public Map<String, Method> getmSetterMethodMap() {
        return mSetterMethodMap;
    }

    @Override
    public String toString() {
        //return super.toString();
        return new String("id:"+movieId+" title:"+movieTitle+" genreIds:"+genreIds+" overview:"+overview+" release_date:"+releaseDate+" youtubeKey:"+youtubeKey+" poster_link:"+posterLink+" thumnnailLink:"+thumbNailLink);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
          dest.writeInt(this.movieId);
          dest.writeStringArray(new String[]{this.movieTitle,this.overview,this.releaseDate,this.youtubeKey,this.posterLink,this.thumbNailLink,this.popularity,this.rating});
          dest.writeList(this.genreIds);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
          public MovieDetail createFromParcel(Parcel in){
              try {
                  return new MovieDetail(in);
              } catch (NoSuchMethodException e) {
                  e.printStackTrace();

              }
              return null;
          }

          public MovieDetail[] newArray(int size){
              return new MovieDetail[size];
          }

    };

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
