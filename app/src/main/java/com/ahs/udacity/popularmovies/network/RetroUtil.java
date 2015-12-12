package com.ahs.udacity.popularmovies.network;

import android.content.SyncAdapterType;

import com.ahs.udacity.popularmovies.sync.SyncAdapter;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by shetty on 05/12/15.
 */
public class RetroUtil {

    public static Retrofit retrofit;

    public static Retrofit getInstance(boolean converter,String base_url){

        if(retrofit == null){
            if(converter){
                retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
            }
            else
            {
                retrofit = new Retrofit.Builder().baseUrl(base_url).build();
            }
        }
        return retrofit;
    }

}
