package com.ahs.udacity.popularmovies.util;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.ahs.udacity.popularmovies.constants.MovieConstants;
import com.ahs.udacity.popularmovies.datamodel.MovieDetail;
import com.ahs.udacity.popularmovies.datamodel.MovieDetailList;
import com.ahs.udacity.popularmovies.datamodel.MovieType;
import com.ahs.udacity.popularmovies.datamodel.MovieTypeList;
import com.ahs.udacity.popularmovies.network.MoviesService;
import com.ahs.udacity.popularmovies.provider.MovieContract;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shetty on 05/12/15.
 */
public class MovieOperations {

    private static final String TAG = "MovieOperation";


    public void updateMovieDetails(MoviesService movie_service, final Context context,String api_key,String sort_order,int page_num) throws RemoteException, OperationApplicationException {


        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        List<MovieDetail> entries = new ArrayList<>();
        MovieDetailList movieDetailList = null;
        try {
            movieDetailList = movie_service.getMovies(sort_order,api_key,page_num).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(movieDetailList!=null && movieDetailList.getMovieDetailList().size()>0) {
            entries = movieDetailList.getMovieDetailList();
        }

        HashMap<Integer, MovieDetail> entryMap = new HashMap<Integer, MovieDetail>();
        for (MovieDetail e : entries) {

            //download youtube key
          /*  InputStream keyStream = downloadUrl(new URL(VIDEO_KEY_URL + "/" + e.getMovieId() + "/" + POSTFIX));
            String video_key = streamTOString(keyStream);*/
            List<MovieType> movieTypes = null;
            MovieTypeList movieTypeList = null;
            try {
                movieTypeList = movie_service.getVideoKey(e.getMovieId(),api_key).execute().body();
            } catch (IOException e1) {
                Log.d(TAG,"IOError:"+e1.getMessage());
                e1.printStackTrace();

            }

            if(movieTypeList!=null) {
                movieTypes = movieTypeList.getResults();
                e.setYoutubeKey(getTrailerKey(movieTypes));
                Log.d(TAG, "adding movieDetail object:" + e);
                entryMap.put(e.getMovieId(), e);
            }

        }


        final ContentResolver contentResolver = context.getContentResolver();
        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = MovieContract.Entry.CONTENT_URI; // Get all entries
        Cursor c = contentResolver.query(uri, null, null, null, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

        // Find stale data
        int id;

        String title;
        String youtube_key;
        String release_date;
        String thumb_nail;
        String overview;
        String popularity;
        String rating;
        String poster_link;
        while (c.moveToNext()) {

            id = c.getInt(c.getColumnIndex(MovieContract.Entry._ID));

            title = c.getString(c.getColumnIndex(MovieContract.Entry.COLUMN_MOVIE_TITLE));
            youtube_key = c.getString(c.getColumnIndex(MovieContract.Entry.COLUMN_YOUTUBE_VIDEO_KEY));
            thumb_nail = c.getString(c.getColumnIndex(MovieContract.Entry.COLUMN_THUMBNAIL_LINK));
            poster_link = c.getString(c.getColumnIndex(MovieContract.Entry.COLUMN_POSTER_LINK));
            release_date = c.getString(c.getColumnIndex(MovieContract.Entry.COLUMN_MOVIE_RELEASE_DATE));
            overview = c.getString(c.getColumnIndex(MovieContract.Entry.COLUMN_MOVIE_OVERVIEW));
            rating  = c.getString(c.getColumnIndex(MovieContract.Entry.COLUMN_MOVIE_RATING));
            popularity = c.getString(c.getColumnIndex(MovieContract.Entry.COLUMN_MOVIE_POPULARITY));

            MovieDetail match = entryMap.get(id);
            if (match != null) {
                // Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(id);
                // Check to see if the entry needs to be updated
                Uri existingUri = MovieContract.Entry.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                if ((match.getMovieTitle() != null && !match.getMovieTitle().equals(title)) || (match.getOverview()!=null && !match.getOverview().equals(overview)) ||
                        (match.getYoutubeKey() != null && !match.getYoutubeKey().equals(youtube_key)) ||
                        (match.getReleaseDate() != release_date)) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(MovieContract.Entry.COLUMN_MOVIE_TITLE, match.getMovieTitle())
                            .withValue(MovieContract.Entry.COLUMN_YOUTUBE_VIDEO_KEY, match.getYoutubeKey())
                            .withValue(MovieContract.Entry.COLUMN_THUMBNAIL_LINK, MovieConstants.IMAGE_PREFIX + match.getThumbNailLink())
                            .withValue(MovieContract.Entry.COLUMN_POSTER_LINK, MovieConstants.IMAGE_PREFIX + match.getPosterLink())
                            .withValue(MovieContract.Entry.COLUMN_MOVIE_OVERVIEW, match.getOverview())
                            .withValue(MovieContract.Entry.COLUMN_MOVIE_RELEASE_DATE, match.getReleaseDate())
                            .withValue(MovieContract.Entry.COLUMN_MOVIE_RATING, match.getRating())
                            .withValue(MovieContract.Entry.COLUMN_GENRE, new JSONArray(match.getGenreIds()).toString())
                            .withValue(MovieContract.Entry.COLUMN_MOVIE_POPULARITY, match.getPopularity())
                            /*.withValue(MovieContract.Entry.COLUMN_IS_FAVOURITE, match.isFavourite())*/
                            .build());

                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                // Entry doesn't exist. Remove it from the database.
                /*Uri deleteUri = MovieContract.Entry.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());*/

            }
        }
        c.close();

        // Add new items
        for (MovieDetail e : entryMap.values()) {
            Log.i(TAG, "Scheduling insert: entry_id=" + e.getMovieId());
            batch.add(ContentProviderOperation.newInsert(MovieContract.Entry.CONTENT_URI)
                    .withValue(MovieContract.Entry._ID, e.getMovieId())
                    .withValue(MovieContract.Entry.COLUMN_MOVIE_TITLE, e.getMovieTitle())
                    .withValue(MovieContract.Entry.COLUMN_YOUTUBE_VIDEO_KEY, e.getYoutubeKey())
                    .withValue(MovieContract.Entry.COLUMN_POSTER_LINK, MovieConstants.IMAGE_PREFIX + e.getPosterLink())
                    .withValue(MovieContract.Entry.COLUMN_THUMBNAIL_LINK, MovieConstants.IMAGE_PREFIX + e.getThumbNailLink())
                    .withValue(MovieContract.Entry.COLUMN_MOVIE_RELEASE_DATE, e.getReleaseDate())
                    .withValue(MovieContract.Entry.COLUMN_MOVIE_RATING, e.getRating())
                    .withValue(MovieContract.Entry.COLUMN_MOVIE_OVERVIEW, e.getOverview())
                    .withValue(MovieContract.Entry.COLUMN_GENRE, new JSONArray(e.getGenreIds()).toString())
                    .withValue(MovieContract.Entry.COLUMN_MOVIE_POPULARITY, e.getPopularity())
                    /*.withValue(MovieContract.Entry.COLUMN_IS_FAVOURITE, e.isFavourite())*/
                    .build());

        }
        Log.i(TAG, "Merge solution ready. Applying batch update");
        contentResolver.applyBatch(MovieContract.CONTENT_AUTHORITY, batch);
        contentResolver.notifyChange(
                MovieContract.Entry.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }



    public String getTrailerKey(List<MovieType> movieTypes){
        for(MovieType movieType:movieTypes){
            if(movieType.getMovieType().equalsIgnoreCase("trailer")){
                return movieType.getKey();
            }
        }
        return null;
    }

}
