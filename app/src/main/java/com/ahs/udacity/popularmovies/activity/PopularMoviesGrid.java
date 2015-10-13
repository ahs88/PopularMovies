package com.ahs.udacity.popularmovies.activity;

import android.net.Uri;
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
import com.ahs.udacity.popularmovies.activity.fragment.MoviesDetailFragment;
import com.ahs.udacity.popularmovies.adapter.PopularMoviesAdapter;

public class PopularMoviesGrid extends AppCompatActivity implements MoviesDetailFragment.OnFragmentInteractionListener{

    private static final String TAG = PopularMoviesGrid.class.getCanonicalName() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies_grid);
        RecyclerView gridView = (RecyclerView)findViewById(R.id.mGridView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridView.setLayoutManager(gridLayoutManager);
        PopularMoviesAdapter moviesAdapter = new PopularMoviesAdapter(this);
        gridView.setAdapter(moviesAdapter);
        if(findViewById(R.id.movieDetailContainer)!=null)
        {
            Log.d(TAG, "movieDetailContainer");

            MoviesDetailFragment moviesDetailFragment = (MoviesDetailFragment)getFragmentManager().findFragmentByTag(MoviesDetailFragment.TAG);
            if(moviesDetailFragment == null) {
                moviesDetailFragment =MoviesDetailFragment.newInstance("", "");
                getFragmentManager().beginTransaction().add(R.id.movieDetailContainer, moviesDetailFragment, MoviesDetailFragment.TAG).commit();
            }

        }
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

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
