package com.ahs.udacity.popularmovies.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ahs.udacity.popularmovies.provider.DbManager;
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
    @SerializedName("id")
    private String movieId;
    @SerializedName("title")
    private String movieTitle;
    @SerializedName("genre_ids")
    private List<String> genreIds = new ArrayList<>();
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;

    private String youtubeKey;
    private String posterLink;
    private String thumbNailLink;

    //constants to map setters and getter funcitons
    private static final String MOVIEID="MOVIEID";
    private static final String MOVIETITLE="MOVIETITLE";
    private static final String GENREID="GENREID";
    private static final String OVERVIEW="OVERVIEW";
    private static final String RELEASEDATE="RELEASEDATE";
    private static final String YOUTUBEKEY="YOUTUBEKEY";
    private static final String POSTERLINK="POSTERLINK";
    private static final String THUMBNAIL="THUMBNAIL";

    private Map<String,Method> mGetterMethodMap;

    //private Map<String,Method> mAdderMethodMap;
    private Map<String,Method> mSetterMethodMap;

    public MovieDetail(){
        try {
            initMethodMap();
        }catch (Exception e){

        }
    }


    private List<String> detailKeyList= Arrays.asList(MOVIEID,MOVIETITLE,GENREID,OVERVIEW,RELEASEDATE,YOUTUBEKEY,POSTERLINK,THUMBNAIL);

    public MovieDetail(Parcel parcel)
    {
        String []arr = new String[7];
                parcel.readStringArray(arr);
        int i=0;
        for(String key:arr){
            Object val;
            try{
                val = getmGetterMethodMap().get(detailKeyList.get(i)).invoke(this);
                if(val instanceof String)
                {
                    getmSetterMethodMap().get(detailKeyList.get(i)).invoke(this,key);
                    i++;
                }

            }catch (IllegalArgumentException ile){

            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        parcel.readList(genreIds, MovieDetail.class.getClassLoader());

    }


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

    private void initMethodMap() throws NoSuchMethodException {

        //getter method map creation
        mGetterMethodMap = new HashMap<>();
        mGetterMethodMap.put(MOVIEID,MovieDetail.class.getMethod("getMovieId"));
        mGetterMethodMap.put(MOVIETITLE,MovieDetail.class.getMethod("getMovieTitle"));
        mGetterMethodMap.put(GENREID,MovieDetail.class.getMethod("getGenreIds"));
        mGetterMethodMap.put(OVERVIEW,MovieDetail.class.getMethod("getOverview"));
        mGetterMethodMap.put(RELEASEDATE,MovieDetail.class.getMethod("getReleaseDate"));
        mGetterMethodMap.put(YOUTUBEKEY,MovieDetail.class.getMethod("getYoutubeKey"));
        mGetterMethodMap.put(POSTERLINK,MovieDetail.class.getMethod("getPosterLink"));
        mGetterMethodMap.put(THUMBNAIL,MovieDetail.class.getMethod("getThumbNailLink"));


        //args for setter method
        Class[] cStringArgs = new Class[1];
        cStringArgs[0] = String.class;

        Class[] cListArgs = new Class[1];
        cListArgs[0] = List.class;

        //setter method map creation
        mSetterMethodMap = new HashMap<>();
        mSetterMethodMap.put(MOVIEID,MovieDetail.class.getMethod("setMovieId", cStringArgs));
        mSetterMethodMap.put(MOVIETITLE,MovieDetail.class.getMethod("setMovieId", cStringArgs));
        mSetterMethodMap.put(GENREID,MovieDetail.class.getMethod("setGenreIds",cListArgs));
        mSetterMethodMap.put(OVERVIEW,MovieDetail.class.getMethod("setOverview",cStringArgs));
        mSetterMethodMap.put(RELEASEDATE,MovieDetail.class.getMethod("setReleaseDate",cStringArgs));
        mSetterMethodMap.put(YOUTUBEKEY,MovieDetail.class.getMethod("getYoutubeKey",cStringArgs));
        mSetterMethodMap.put(POSTERLINK,MovieDetail.class.getMethod("getPosterLink",cStringArgs));
        mSetterMethodMap.put(THUMBNAIL,MovieDetail.class.getMethod("getThumbNailLink",cStringArgs));

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
        return mGetterMethodMap;
    }

    @Override
    public String toString() {
        //return super.toString();
        return new String("id:"+movieId+" title:"+movieTitle+" genreIds:"+genreIds+" overview:"+overview+" release_date:"+releaseDate+" youtubeKey:"+youtubeKey+" poster_link:"+posterLink);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
          dest.writeStringArray(new String[]{this.movieId,this.movieTitle,this.overview,this.releaseDate,this.youtubeKey,this.posterLink,this.thumbNailLink});
          dest.writeList(this.genreIds);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
          public MovieDetail createFromParcel(Parcel in){
              return new MovieDetail(in);
          }

          public MovieDetail[] newArray(int size){
              return new MovieDetail[size];
          }

    };

}
