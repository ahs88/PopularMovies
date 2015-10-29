package com.ahs.udacity.popularmovies.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shetty on 23/10/15.
 */
public class Utilities {
    public static List<String> convertJsonArrayToList(String jsonGenreIdArray) {
        List<String> genreIds = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonGenreIdArray);
            for(int i=0;i<jsonArray.length();i++)
            {
                genreIds.add(jsonArray.getString(i));
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return genreIds;
    }

}
