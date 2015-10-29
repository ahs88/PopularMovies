package com.ahs.udacity.popularmovies.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ahs.udacity.popularmovies.R;
import com.ahs.udacity.popularmovies.activity.fragment.MoviesDetailFragment;
import com.ahs.udacity.popularmovies.adapter.PopularMoviesAdapter;
import com.ahs.udacity.popularmovies.datamodel.MovieDetail;

public class MovieDetailActivity extends AppCompatActivity implements MoviesDetailFragment.OnFragmentInteractionListener {


    public MovieDetail movieDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent()!=null && getIntent().getExtras().getParcelable(PopularMoviesAdapter.MOVIE_DETAIL)!=null)
        {
            movieDetail = getIntent().getExtras().getParcelable(PopularMoviesAdapter.MOVIE_DETAIL);
        }
        setContentView(R.layout.activity_movie_detail);
        addDetailFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
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

    public void addDetailFragment() {
        MoviesDetailFragment moviesDetailFragment = (MoviesDetailFragment) getSupportFragmentManager().findFragmentByTag(MoviesDetailFragment.TAG);
        if (moviesDetailFragment == null) {
            moviesDetailFragment = MoviesDetailFragment.newInstance(movieDetail);
            getSupportFragmentManager().beginTransaction().add(R.id.movie_container, moviesDetailFragment, MoviesDetailFragment.TAG).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void mDetailTab(View v)
    {
        MoviesDetailFragment movieDetailFragment = (MoviesDetailFragment)getSupportFragmentManager().findFragmentByTag(MoviesDetailFragment.TAG);
        movieDetailFragment.mDetailTab(v);
    }

    public void playTrailer(View v)
    {
        MoviesDetailFragment movieDetailFragment = (MoviesDetailFragment)getSupportFragmentManager().findFragmentByTag(MoviesDetailFragment.TAG);
        movieDetailFragment.playTrailer(v);
    }

}
