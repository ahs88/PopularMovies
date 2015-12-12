package com.ahs.udacity.popularmovies.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.support.v4.os.ResultReceiver;

import com.ahs.udacity.popularmovies.constants.MovieConstants;
import com.ahs.udacity.popularmovies.network.MoviesService;
import com.ahs.udacity.popularmovies.network.RetroUtil;
import com.ahs.udacity.popularmovies.util.MovieOperations;

import retrofit.Retrofit;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownloadService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_MOVIE_LIST = "com.ahs.udacity.popularmovies.service.action.MOVIELIST";
    private static final String ACTION_BAZ = "com.ahs.udacity.popularmovies.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.ahs.udacity.popularmovies.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.ahs.udacity.popularmovies.service.extra.PARAM2";
    private static final String SORT_ORDER = "SORT_ORDER";
    private static final String API_KEY = "API_KEY";
    private static final String MOVIE_SERVICE = "MOVIE_SERVICE";
    private static final String PAGE_NUM = "PAGE_NUM";
    private static final String RESULT_RECEIVER = "RESULT_RECEIVER";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void getMovieList(Context context, String sort_oder, String api_key,int page,ResultReceiver resultReceiver) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_MOVIE_LIST);
        intent.putExtra(SORT_ORDER, sort_oder);
        intent.putExtra(API_KEY, api_key);
        intent.putExtra(PAGE_NUM,page);
        intent.putExtra(RESULT_RECEIVER,resultReceiver);
        context.startService(intent);
    }

    private static MoviesService loadMovieService() {
        Retrofit retrofit = RetroUtil.getInstance(true, MovieConstants.POPULAR_MOVIE_BASE_URL);
        MoviesService moviesService = retrofit.create(MoviesService.class);
        return moviesService;
    }


    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_MOVIE_LIST.equals(action)) {
                final String sort_order = intent.getStringExtra(SORT_ORDER);
                final String api_key = intent.getStringExtra(API_KEY);
                final int page_num = intent.getIntExtra(PAGE_NUM, 1);
                final MoviesService movie_service = loadMovieService();
                final ResultReceiver result_receiver = intent.getParcelableExtra(RESULT_RECEIVER);
                handleActionMovieList(sort_order, api_key,movie_service,page_num);
                result_receiver.send(Activity.RESULT_OK,null);

            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionMovieList(String sort_order, String api_key,MoviesService moviesService,int page_num) {
        // TODO: Handle action Foo
        MovieOperations movieOperations = new MovieOperations();
        try {
            movieOperations.updateMovieDetails(moviesService,this,api_key,sort_order,page_num);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
        //throw new UnsupportedOperationException("Not yet implemented");
    }


}
