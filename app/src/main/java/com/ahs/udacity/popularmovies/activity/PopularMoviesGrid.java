package com.ahs.udacity.popularmovies.activity;

import android.accounts.Account;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Loader;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.support.annotation.MainThread;
import android.support.v4.content.CursorLoader;
import android.support.v4.os.ResultReceiver;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahs.udacity.popularmovies.R;
import com.ahs.udacity.popularmovies.accounts.GenericAccountService;
import com.ahs.udacity.popularmovies.activity.fragment.MoviesDetailFragment;
import com.ahs.udacity.popularmovies.activity.listener.EndlessScrollListener;
import com.ahs.udacity.popularmovies.adapter.CursorRecyclerViewAdapter;
import com.ahs.udacity.popularmovies.adapter.PopularMoviesAdapter;
import com.ahs.udacity.popularmovies.constants.MovieConstants;
import com.ahs.udacity.popularmovies.datamodel.MovieDetail;
import com.ahs.udacity.popularmovies.network.MoviesService;
import com.ahs.udacity.popularmovies.network.RetroUtil;
import com.ahs.udacity.popularmovies.provider.DbManager;
import com.ahs.udacity.popularmovies.provider.MovieContract;
import com.ahs.udacity.popularmovies.service.DownloadService;
import com.ahs.udacity.popularmovies.sync.SyncUtils;
import com.ahs.udacity.popularmovies.util.Utilities;

import java.io.IOException;

import retrofit.Retrofit;

public class PopularMoviesGrid extends AppCompatActivity implements MoviesDetailFragment.OnFragmentInteractionListener, android.support.v4.app.LoaderManager.LoaderCallbacks, SearchView.OnQueryTextListener, SearchView.OnCloseListener, EndlessScrollListener.LoadMore {

    private static final String TAG = PopularMoviesGrid.class.getCanonicalName();
    private static final String[] PROJECTION = {};
    private static final int POPUALR = 0;
    private static final int HIGHLY_RATED = 1;
    private static final int FAVOUTITE = 2;
    private static String[] optionTitles = new String[]{"Popular Movies", "Highly Rated", "Favourites"};
    private static final int GRID_COUNT_PORT = 2;
    private static final int GRID_COUNT_LAND = 3;
    private static final String CURRENT_LOADER_STATE = "CURRENT_LOADER_STATE";
    protected int CURRENT_LOADER_ID = 0;
    private boolean mTwoPane;
    private PopularMoviesAdapter moviesAdapter;
    private RecyclerView gridView;
    private Object mSyncObserverHandle;
    private PopularMoviesGrid mContext;
    private ImageView spinningView;
    private AnimationDrawable frameAnimation;
    private SearchView searchView;
    private StaggeredGridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_popular_movies_grid);
        if (savedInstanceState != null) {
            CURRENT_LOADER_ID = savedInstanceState.getInt(CURRENT_LOADER_STATE);
        }

        setupGridView();


        if (findViewById(R.id.movieDetailContainer) != null) {
            Log.d(TAG, "movieDetailContainer");

            mTwoPane = true;

        } else {
            mTwoPane = false;
        }

        SyncUtils.CreateSyncAccount(this);
        getSupportLoaderManager().initLoader(CURRENT_LOADER_ID, null, this);
        //SyncUtils.TriggerRefresh();

    }

    private void setupGridView() {
        gridView = (RecyclerView) findViewById(R.id.mGridView);
        gridView.setHasFixedSize(false);

        gridLayoutManager = new StaggeredGridLayoutManager((findViewById(R.id.orientation_land)==null)?GRID_COUNT_PORT:GRID_COUNT_LAND, StaggeredGridLayoutManager.VERTICAL);


        gridView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
                super.getItemOffsets(outRect, itemPosition, parent);
                outRect.top = 5;
                outRect.bottom = 5;
                outRect.right = 5;
                outRect.left = 5;

            }
        });

        gridView.setOnScrollListener(new EndlessScrollListener(gridLayoutManager, this, 5,(findViewById(R.id.orientation_land) == null)?GRID_COUNT_PORT:GRID_COUNT_LAND));

        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        gridView.setLayoutManager(gridLayoutManager);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onresume");

        mSyncStatusObserver.onStatusChanged(CURRENT_LOADER_ID);

        // Watch for sync state changes
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);

        SyncUtils.TriggerRefresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_popular_movies_grid, menu);
        MenuItem menuItem = (MenuItem) menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.setQueryHint(getString(R.string.search_hint));
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_popular:
                getSupportLoaderManager().restartLoader(POPUALR, null, this);
                CURRENT_LOADER_ID = POPUALR;
                break;
            case R.id.highly_rated:
                getSupportLoaderManager().restartLoader(HIGHLY_RATED, null, this);
                CURRENT_LOADER_ID = HIGHLY_RATED;
                break;
            case R.id.action_favourite:
                getSupportLoaderManager().restartLoader(FAVOUTITE, null, this);
                CURRENT_LOADER_ID = FAVOUTITE;
                break;

        }
        removeDetaiFragment();
        return super.onOptionsItemSelected(item);
    }

    public void mDetailTab(View v) {
        if (findViewById(R.id.movieDetailContainer) == null) {
            return;
        }
        MoviesDetailFragment movieDetailFragment = (MoviesDetailFragment) getSupportFragmentManager().findFragmentByTag(MoviesDetailFragment.TAG);
        movieDetailFragment.mDetailTab(v);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
        /** Callback invoked with the sync adapter status changes. */
        @Override
        public void onStatusChanged(int which) {
            mContext.runOnUiThread(new Runnable() {
                /**
                 * The SyncAdapter runs on a background thread. To update the UI, onStatusChanged()
                 * runs on the UI thread.
                 */
                @Override
                public void run() {
                    // Create a handle to the account that was created by
                    // SyncService.CreateSyncAccount(). This will be used to query the system to
                    // see how the sync status has changed.
                    Account account = GenericAccountService.GetAccount();
                    if (account == null) {
                        // GetAccount() returned an invalid value. This shouldn't happen, but
                        // we'll set the status to "not refreshing".
                        //setRefreshActionButtonState(false);
                        return;
                    }

                    // Test the ContentResolver to see if the sync adapter is active or pending.
                    // Set the state of the refresh button accordingly.
                    boolean syncActive = ContentResolver.isSyncActive(
                            account, MovieContract.CONTENT_AUTHORITY);
                    boolean syncPending = ContentResolver.isSyncPending(
                            account, MovieContract.CONTENT_AUTHORITY);
                    //setRefreshActionButtonState(syncActive || syncPending);
                }
            });
        }
    };


    public void addDetailFragment(MovieDetail movieDetail) {
        MoviesDetailFragment moviesDetailFragment = (MoviesDetailFragment) getSupportFragmentManager().findFragmentByTag(MoviesDetailFragment.TAG);
        if (moviesDetailFragment == null) {
            moviesDetailFragment = MoviesDetailFragment.newInstance(movieDetail);
            getSupportFragmentManager().beginTransaction().add(R.id.movieDetailContainer, moviesDetailFragment, MoviesDetailFragment.TAG).commit();
        } else {
            moviesDetailFragment = MoviesDetailFragment.newInstance(movieDetail);
            getSupportFragmentManager().beginTransaction().replace(R.id.movieDetailContainer, moviesDetailFragment, MoviesDetailFragment.TAG).commit();
        }
    }

    public void removeDetaiFragment() {
        MoviesDetailFragment moviesDetailFragment = (MoviesDetailFragment) getSupportFragmentManager().findFragmentByTag(MoviesDetailFragment.TAG);
        if (moviesDetailFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(moviesDetailFragment).commit();
        }
    }

    @Override
    public android.support.v4.content.Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case 0: {
                return new CursorLoader(this,  // Context
                        MovieContract.Entry.CONTENT_URI, // URI
                        null,                // Projection
                        null,                           // Selection
                        null,                           // Selection args
                        MovieContract.Entry.COLUMN_MOVIE_POPULARITY + " desc");

            }
            case 1: {
                return new CursorLoader(this,  // Context
                        MovieContract.Entry.CONTENT_URI, // URI
                        null,                // Projection
                        null,                           // Selection
                        null,                           // Selection args
                        MovieContract.Entry.COLUMN_MOVIE_RATING + " desc");

            }
            case 2: {
                String arguments[] = {"1"};
                return new CursorLoader(this,  // Context
                        MovieContract.Entry.CONTENT_URI, // URI
                        null,                // Projection
                        MovieDetail.ISFAVOURTIE + "=?",                           // Selection
                        arguments,                           // Selection args
                        MovieContract.Entry.COLUMN_MOVIE_RELEASE_DATE + " desc");
            }
            default: {
                /*return new CursorLoader(this,  // Context
                        MovieContract.Entry.CONTENT_URI, // URI
                        null,                // Projection
                        null,                           // Selection
                        null,                           // Selection args
                        MovieContract.Entry.COLUMN_MOVIE_RELEASE_DATE + " desc");*/
                return null;

            }

        }
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader loader, Object data) {
        Log.d(TAG, "onLoadFinished cursor:" + (Cursor) data + " loader.getId:" + loader.getId() + " CURRENT_LOADER_ID:" + CURRENT_LOADER_ID+" cursor size:"+((Cursor) data).getCount()+" isNetwork Available:"+Utilities.isNetworkAvailable(this));

        if (loader.getId() == CURRENT_LOADER_ID) {
            initAdapter((Cursor) data);
            setTitle(optionTitles[CURRENT_LOADER_ID]);
        }



        //if cursor is empty and  if network not available display no internet
        if(((Cursor) data).getCount() == 0)
        {

            if(CURRENT_LOADER_ID == FAVOUTITE){
                findViewById(R.id.no_internet).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.no_internet_text)).setText(getString(R.string.no_favourites));
            }
            if(!Utilities.isNetworkAvailable(this)){
                findViewById(R.id.no_internet).setVisibility(View.VISIBLE);

                if(CURRENT_LOADER_ID!=FAVOUTITE)
                {
                    ((TextView)findViewById(R.id.no_internet_text)).setText(getString(R.string.no_internet));
                }
            }

        }
        else
        {
            //hide even when offline, offline msg displayed on youtube player
            findViewById(R.id.no_internet).setVisibility(View.GONE);
        }
        //getSupportLoaderManager().destroyLoader(loader.getId());

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader loader) {
        Log.d(TAG, "onLoadReset ");
        if (moviesAdapter != null) {
            moviesAdapter.changeCursor(null);
        }
    }

    public void initAdapter(final Cursor cursor) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (moviesAdapter == null) {
                    moviesAdapter = new PopularMoviesAdapter(cursor, mContext, mTwoPane, CURRENT_LOADER_ID);
                    moviesAdapter.swapCursor((Cursor) cursor);
                    gridView.setAdapter(moviesAdapter);
                }
                moviesAdapter.swapCursor((Cursor) cursor);
                Log.d(TAG, "initAdapter");
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
        searchView.clearFocus();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(CURRENT_LOADER_ID);
    }

    public void playTrailer(View v) {
        MoviesDetailFragment movieDetailFragment = (MoviesDetailFragment) getSupportFragmentManager().findFragmentByTag(MoviesDetailFragment.TAG);
        movieDetailFragment.playTrailer(v);
    }

    public void markAsFovourite(View v) {
        MoviesDetailFragment movieDetailFragment = (MoviesDetailFragment) getSupportFragmentManager().findFragmentByTag(MoviesDetailFragment.TAG);
        movieDetailFragment.markAsFavourite(v, CURRENT_LOADER_ID);
    }

    public void shareMovie(View v) {
        MoviesDetailFragment movieDetailFragment = (MoviesDetailFragment) getSupportFragmentManager().findFragmentByTag(MoviesDetailFragment.TAG);
        movieDetailFragment.share(v);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Log.d(TAG, "filter string:" + s);
        moviesAdapter.getFilter().filter(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        Log.d(TAG, "filter string:" + s);
        moviesAdapter.getFilter().filter(s);
        return false;
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public void loadMore(int count) {
        Log.d(TAG, "loadMore  count:" + count);
        String sort_by = MovieConstants.SORT_BY_POPULARITY;
        if (CURRENT_LOADER_ID == FAVOUTITE) {
            return;
        }
        if (CURRENT_LOADER_ID == POPUALR)
            sort_by = MovieConstants.SORT_BY_POPULARITY;
        else
            sort_by = MovieConstants.SORT_BY_HIGHLY_RATED;


        Handler handler = new Handler(Looper.getMainLooper());
        ResultReceiver resultReceiver = new MovieListReceiver(handler);
        DownloadService.getMovieList(this, sort_by, MovieConstants.MOVIE_API_KEY, count, resultReceiver);

    }

    @Override
    public void loading() {
        if (CURRENT_LOADER_ID == FAVOUTITE) {
            return;
        }
        if (spinningView == null) {
            spinningView = (ImageView) findViewById(R.id.loading);
        }
        spinningView.setVisibility(View.VISIBLE);
        spinningView.setBackgroundResource(R.drawable.spin_animation);

        if (frameAnimation == null) {
            frameAnimation = (AnimationDrawable) spinningView.getBackground();
        }

        frameAnimation.start();

    }

    @Override
    public void stopLoading() {
        if (frameAnimation != null && spinningView != null) {
            frameAnimation.stop();
            spinningView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_LOADER_STATE, CURRENT_LOADER_ID);
    }



    class MovieListReceiver extends ResultReceiver {


        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public MovieListReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            stopLoading();
        }
    }

}
