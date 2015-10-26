package com.ahs.udacity.popularmovies.provider;

import android.database.Cursor;
import android.graphics.Movie;
import android.util.Log;

import com.ahs.udacity.popularmovies.activity.fragment.MoviesDetailFragment;
import com.ahs.udacity.popularmovies.datamodel.MovieDetail;
import com.ahs.udacity.popularmovies.util.Utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by shetty on 17/10/15.
 */
public class DbManager {

    private static final String TAG = DbManager.class.getCanonicalName();

    public static MovieDetail getElementFromCursor(Cursor cursor){
        MovieDetail moviesDetail = new MovieDetail();
        List<String> keyList = moviesDetail.getDetailKeyList();
        Log.d(TAG, "KeyList size:"+keyList.size()+" cursor count:"+cursor.getCount());
        for(String key:keyList){

            try{
                Type val = moviesDetail.getmGetterMethodMap().get(key).getReturnType();
                //Log.d(TAG, "Movie detail setting key:"+key+" VALUE_TYPE:"+val+" method:"+moviesDetail.getmGetterMethodMap().get(key));
                if(val.equals(Integer.TYPE))
                {
                    int value = cursor.getInt(cursor.getColumnIndex(key));
                    Log.d(TAG, "Movie detail setting key:"+key+" integer:"+value+" method:"+moviesDetail.getmSetterMethodMap().get(key));
                    moviesDetail.getmSetterMethodMap().get(key).invoke(moviesDetail,value);
                }
                if(val.equals(String.class))
                {

                    String value = cursor.getString(cursor.getColumnIndex(key));
                    Log.d(TAG, "Movie detail setting key:"+key+ " String:"+value);
                    moviesDetail.getmSetterMethodMap().get(key).invoke(moviesDetail,value);
                }
                if(val.equals(List.class))
                {
                    String value = cursor.getString(cursor.getColumnIndex(key));
                    Log.d(TAG, "Movie detail setting key:"+key+ " String:"+value);
                    List<String> genreIds = Utilities.convertJsonArrayToList(value);
                    moviesDetail.getmSetterMethodMap().get(key).invoke(moviesDetail,genreIds);
                }
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }catch (IllegalStateException e){
                e.printStackTrace();
            }
        }
        return moviesDetail;
    }


}


