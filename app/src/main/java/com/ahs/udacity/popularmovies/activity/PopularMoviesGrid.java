package com.ahs.udacity.popularmovies.activity;

import android.accounts.Account;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Loader;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;

import com.ahs.udacity.popularmovies.R;
import com.ahs.udacity.popularmovies.accounts.GenericAccountService;
import com.ahs.udacity.popularmovies.activity.fragment.MoviesDetailFragment;
import com.ahs.udacity.popularmovies.adapter.PopularMoviesAdapter;
import com.ahs.udacity.popularmovies.datamodel.MovieDetail;
import com.ahs.udacity.popularmovies.provider.MovieContract;
import com.ahs.udacity.popularmovies.sync.SyncUtils;

public class PopularMoviesGrid extends AppCompatActivity implements MoviesDetailFragment.OnFragmentInteractionListener, android.support.v4.app.LoaderManager.LoaderCallbacks{

    private static final String TAG = PopularMoviesGrid.class.getCanonicalName() ;
    private static final String[] PROJECTION = {};
    private boolean mTwoPane;
    private PopularMoviesAdapter moviesAdapter;
    private RecyclerView gridView;
    private Object mSyncObserverHandle;
    private PopularMoviesGrid mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_popular_movies_grid);
        gridView = (RecyclerView)findViewById(R.id.mGridView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridView.setLayoutManager(gridLayoutManager);

        if(findViewById(R.id.movieDetailContainer)!=null)
        {
            Log.d(TAG, "movieDetailContainer");

            mTwoPane = true;

        }
        else {
            mTwoPane = false;
        }

        SyncUtils.CreateSyncAccount(this);
        //SyncUtils.TriggerRefresh();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().initLoader(0, null, this);
        mSyncStatusObserver.onStatusChanged(0);

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public  void mDetailTab(View v)
    {
        if(findViewById(R.id.movieDetailContainer)==null) {
            return;
        }
        MoviesDetailFragment movieDetailFragment = (MoviesDetailFragment)getSupportFragmentManager().findFragmentByTag(MoviesDetailFragment.TAG);
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
        }
        else
        {
            moviesDetailFragment = MoviesDetailFragment.newInstance(movieDetail);
            getSupportFragmentManager().beginTransaction().replace(R.id.movieDetailContainer, moviesDetailFragment, MoviesDetailFragment.TAG).commit();
        }
    }

    @Override
    public android.support.v4.content.Loader onCreateLoader(int id, Bundle args) {
         return new CursorLoader(this,  // Context
                MovieContract.Entry.CONTENT_URI, // URI
                null,                // Projection
                null,                           // Selection
                null,                           // Selection args
                MovieContract.Entry.COLUMN_MOVIE_RELEASE_DATE + " desc");

    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader loader, Object data) {
        Log.d(TAG,"onLoadFinished cursor:"+(Cursor)data);

        initAdapter((Cursor) data);
        moviesAdapter.changeCursor((Cursor) data);

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader loader) {
        if(moviesAdapter!=null) {
            moviesAdapter.changeCursor(null);
        }
    }

    public void initAdapter(Cursor cursor)
    {
        if(moviesAdapter==null) {
            moviesAdapter = new PopularMoviesAdapter(cursor, mContext, mTwoPane);
            gridView.setAdapter(moviesAdapter);
        }
        Log.d(TAG,"initAdapter");
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
    }
}
