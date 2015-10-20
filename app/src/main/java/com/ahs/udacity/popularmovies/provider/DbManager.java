package com.ahs.udacity.popularmovies.provider;

import android.database.Cursor;
import android.graphics.Movie;

import com.ahs.udacity.popularmovies.activity.fragment.MoviesDetailFragment;
import com.ahs.udacity.popularmovies.datamodel.MovieDetail;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by shetty on 17/10/15.
 */
public class DbManager {

    public static MovieDetail getElementFromCursor(Cursor cursor){
        MovieDetail moviesDetail = new MovieDetail();
        for(String key:moviesDetail.getDetailKeyList()){
            Object val;
            try{
                val = moviesDetail.getmGetterMethodMap().get(key).invoke(moviesDetail);
                if(val instanceof String)
                {
                    moviesDetail.getmSetterMethodMap().get(key).invoke(moviesDetail,cursor.getColumnIndex(key));
                }
                if(val instanceof List)
                {
                    moviesDetail.getmSetterMethodMap().get(key).invoke(moviesDetail,cursor.getColumnIndex(key));
                }
            }catch (IllegalArgumentException ile){

            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return moviesDetail;
    }

}


