/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ahs.udacity.popularmovies.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;


import com.ahs.udacity.popularmovies.datamodel.MovieDetail;
import com.ahs.udacity.popularmovies.datamodel.MovieType;
import com.ahs.udacity.popularmovies.provider.MovieContract;
import com.google.common.io.CharStreams;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Define a sync adapter for the app.
 *
 * <p>This class is instantiated in {@link SyncService}, which also binds SyncAdapter to the system.
 * SyncAdapter should only be initialized in SyncService, never anywhere else.
 *
 * <p>The system calls onPerformSync() via an RPC call through the IBinder object supplied by
 * SyncService.
 */
class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "SyncAdapter";

    /**
     * URL to fetch content from during a sync.
     *
     * <p>This points to the Android Developers Blog. (Side note: We highly recommend reading the
     * Android Developer Blog to stay up to date on the latest Android platform developments!)
     */
    private static final String POPULAR_MOVIE_URL = "http://api.themoviedb.org/3/movie/popular?sort_by=popularity.desc&api_key=4908fbb64f831529fa956302208ea557";
    private static final String VIDEO_KEY_URL = "https://api.themoviedb.org/3/movie";
    private static final String POSTFIX = "videos?api_key=4908fbb64f831529fa956302208ea557";
    private static final String IMAGE_PREFIX="http://image.tmdb.org/t/p/w500";
    /**
     * Network connection timeout, in milliseconds.
     */
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 25000;  // 15 seconds

    /**
     * Network read timeout, in milliseconds.
     */
    private static final int NET_READ_TIMEOUT_MILLIS = 20000;  // 10 seconds

    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;

    /**
     * Project used when querying content provider. Returns all known fields.
     */
    private static final String[] PROJECTION = new String[] {
            MovieContract.Entry._ID,

            MovieContract.Entry.COLUMN_MOVIE_TITLE,
            MovieContract.Entry.COLUMN_YOUTUBE_VIDEO_KEY,
            MovieContract.Entry.COLUMN_MOVIE_RELEASE_DATE};

    // Constants representing column positions from PROJECTION.
    public static final int COLUMN_MOVIE_ID = 0;

    public static final int COLUMN_TITLE = 1;
    public static final int COLUMN_YOUTUBE_KEY = 2;
    public static final int COLUMN_RELEASE_DATE = 3;
    private String poster_link;

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Called by the Android system in response to a request to run the sync adapter. The work
     * required to read data from the network, parse it, and store it in the content provider is
     * done here. Extending AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
     * run on a background thread. For this reason, blocking I/O and other long-running tasks can be
     * run <em>in situ</em>, and you don't have to set up a separate thread for them.
     .
     *
     * <p>This is where we actually perform any work required to perform a sync.
     * {@link AbstractThreadedSyncAdapter} guarantees that this will be called on a non-UI thread,
     * so it is safe to peform blocking I/O here.
     *
     * <p>The syncResult argument allows you to pass information back to the method that triggered
     * the sync.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Beginning network synchronization");
        try {
            final URL location = new URL(POPULAR_MOVIE_URL);
            InputStream stream = null;

            try {
                Log.i(TAG, "Streaming data from network: " + location);
                stream = downloadUrl(location);
                updateMovieDetails(stream, syncResult);

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            }catch(JsonSyntaxException jse){
                    jse.printStackTrace();
            } finally
            {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (MalformedURLException e) {
            Log.wtf(TAG, "Feed URL is malformed", e);
            syncResult.stats.numParseExceptions++;
            return;
        } catch (IOException e) {
            Log.e(TAG, "Error reading from network: " + e.toString());
            syncResult.stats.numIoExceptions++;
            return;
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (RemoteException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        } catch (OperationApplicationException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        }
        Log.i(TAG, "Network synchronization complete");
    }

    /**
     * Read XML from an input stream, storing it into the content provider.
     *
     * <p>This is where incoming data is persisted, committing the results of a sync. In order to
     * minimize (expensive) disk operations, we compare incoming data with what's already in our
     * database, and compute a merge. Only changes (insert/update/delete) will result in a database
     * write.
     *
     * <p>As an additional optimization, we use a batch operation to perform all database writes at
     * once.
     *
     * <p>Merge strategy:
     * 1. Get cursor to all items in feed<br/>
     * 2. For each item, check if it's in the incoming data.<br/>
     *    a. YES: Remove from "incoming" list. Check if data has mutated, if so, perform
     *            database UPDATE.<br/>
     *    b. NO: Schedule DELETE from database.<br/>
     * (At this point, incoming database only contains missing items.)<br/>
     * 3. For any items remaining in incoming list, ADD to database.
     */
    public void updateMovieDetails(final InputStream stream, final SyncResult syncResult)
            throws IOException, XmlPullParserException, RemoteException,
            OperationApplicationException, ParseException {
        //final FeedParser feedParser = new FeedParser();

        List<MovieDetail> entries = null;
        Log.i(TAG, "Parsing stream as Movie detail");
        try {
             entries = parseJson(stream);
        }catch(JsonSyntaxException e){
            e.printStackTrace();
            Log.d(TAG,"error in json response");
            return;
        }



        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<Integer, MovieDetail> entryMap = new HashMap<Integer, MovieDetail>();
        for (MovieDetail e : entries) {

            //download youtube key
            InputStream keyStream = downloadUrl(new URL(VIDEO_KEY_URL + "/" + e.getMovieId() + "/" + POSTFIX));
            String video_key = streamTOString(keyStream);
            List<MovieType> movieTypes = parseKey("results",video_key);
            e.setYoutubeKey(getTrailerKey(movieTypes));
            Log.d(TAG, "adding movieDetail object:" + e);
            entryMap.put(e.getMovieId(), e);

        }


        final ContentResolver contentResolver = getContext().getContentResolver();
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
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
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
                            .withValue(MovieContract.Entry.COLUMN_THUMBNAIL_LINK, IMAGE_PREFIX + match.getThumbNailLink())
                            .withValue(MovieContract.Entry.COLUMN_POSTER_LINK, IMAGE_PREFIX + match.getPosterLink())
                            .withValue(MovieContract.Entry.COLUMN_MOVIE_OVERVIEW, match.getOverview())
                            .withValue(MovieContract.Entry.COLUMN_MOVIE_RELEASE_DATE, match.getReleaseDate())
                            .withValue(MovieContract.Entry.COLUMN_MOVIE_RATING, match.getRating())
                            .withValue(MovieContract.Entry.COLUMN_GENRE, new JSONArray(match.getGenreIds()).toString())
                            .withValue(MovieContract.Entry.COLUMN_MOVIE_POPULARITY, match.getPopularity())
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = MovieContract.Entry.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
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
                    .withValue(MovieContract.Entry.COLUMN_POSTER_LINK, IMAGE_PREFIX + e.getPosterLink())
                    .withValue(MovieContract.Entry.COLUMN_THUMBNAIL_LINK, IMAGE_PREFIX + e.getThumbNailLink())
                    .withValue(MovieContract.Entry.COLUMN_MOVIE_RELEASE_DATE, e.getReleaseDate())
                    .withValue(MovieContract.Entry.COLUMN_MOVIE_RATING, e.getRating())
                    .withValue(MovieContract.Entry.COLUMN_MOVIE_OVERVIEW, e.getOverview())
                    .withValue(MovieContract.Entry.COLUMN_GENRE, new JSONArray(e.getGenreIds()).toString())
                    .withValue(MovieContract.Entry.COLUMN_MOVIE_POPULARITY, e.getPopularity())
                    .build());
            syncResult.stats.numInserts++;
        }
        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(MovieContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                MovieContract.Entry.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }

    private List<MovieType> parseKey(String rootElement, String video_key_data) {
        JsonParser jsonParser = new JsonParser();
        //Log.d(TAG, "video_key_data:" + video_key_data);
        JsonArray jsonArray = (JsonArray)jsonParser.parse(video_key_data).getAsJsonObject().getAsJsonArray(rootElement);
            //JsonElement jsonelement = jsonArray.get(0);
        Gson gson = new Gson();
        List<MovieType> movieTypes= new ArrayList<MovieType>(3);
        for(JsonElement jsonElement:jsonArray){
            movieTypes.add(gson.fromJson(jsonElement, MovieType.class));
        }
        return movieTypes;

    }

    public String getTrailerKey(List<MovieType> movieTypes){
        for(MovieType movieType:movieTypes){
            if(movieType.getMovieType().equalsIgnoreCase("trailer")){
                return movieType.getKey();
            }
        }
        return null;
    }

    private List<MovieDetail> parseJson(InputStream stream) {
        String jsonString = null;
        jsonString = streamTOString(stream);

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = (JsonArray)jsonParser.parse(jsonString).getAsJsonObject().getAsJsonArray("results");
        Gson gson = new Gson();

        Type type = new TypeToken<List<MovieDetail>>(){}.getType();
        final List<MovieDetail> entries = new ArrayList<>();
        for (JsonElement object: jsonArray) {
            MovieDetail movieDetail = gson.fromJson(object, MovieDetail.class);

            entries.add(movieDetail);
        }   //feedParser.parse(stream);
        Log.i(TAG, "Parsing complete. Found " + entries.size() + " entries");
        return entries;

    }

    public String streamTOString(InputStream stream) {
        String string = null;
        try {
            string = CharStreams.toString(new InputStreamReader(stream, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
            return string;
    }
    /**
     * Given a string representation of a URL, sets up a connection and gets an input stream.
     */
    private InputStream downloadUrl(final URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(NET_READ_TIMEOUT_MILLIS /* milliseconds */);
        conn.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}
