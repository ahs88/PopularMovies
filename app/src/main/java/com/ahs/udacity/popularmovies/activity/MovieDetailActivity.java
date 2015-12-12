package com.ahs.udacity.popularmovies.activity;

import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ahs.udacity.popularmovies.Movies;
import com.ahs.udacity.popularmovies.R;
import com.ahs.udacity.popularmovies.activity.fragment.MoviesDetailFragment;
import com.ahs.udacity.popularmovies.adapter.PopularMoviesAdapter;
import com.ahs.udacity.popularmovies.datamodel.MovieDetail;
import com.ahs.udacity.popularmovies.provider.DbManager;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubePlayer;

public class MovieDetailActivity extends AppCompatActivity implements MoviesDetailFragment.OnFragmentInteractionListener {


    private static final int RECOVERY_DIALOG_REQUEST = 1;
    public MovieDetail movieDetail;
    private int currentLoaderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().getExtras().getParcelable(PopularMoviesAdapter.MOVIE_DETAIL) != null) {
            movieDetail = getIntent().getExtras().getParcelable(PopularMoviesAdapter.MOVIE_DETAIL);
            currentLoaderId = getIntent().getExtras().getInt(PopularMoviesAdapter.CURRENT_LOADER_ID);
        }
        setContentView(R.layout.activity_movie_detail);

        addDetailFragment();

    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    *//*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favourite) {
            MoviesDetailFragment movieDetailFragment = (MoviesDetailFragment)getSupportFragmentManager().findFragmentByTag(MoviesDetailFragment.TAG);
            movieDetailFragment.markAsFavourite(findViewById(id),currentLoaderId);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

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

    public void mDetailTab(View v) {
        MoviesDetailFragment movieDetailFragment = (MoviesDetailFragment) getSupportFragmentManager().findFragmentByTag(MoviesDetailFragment.TAG);
        movieDetailFragment.mDetailTab(v);
    }

    public void playTrailer(View v) {

        MoviesDetailFragment movieDetailFragment = (MoviesDetailFragment) getSupportFragmentManager().findFragmentByTag(MoviesDetailFragment.TAG);
        movieDetailFragment.playTrailer(v);
    }

    public void markAsFovourite(View v) {
        MoviesDetailFragment movieDetailFragment = (MoviesDetailFragment)getSupportFragmentManager().findFragmentByTag(MoviesDetailFragment.TAG);
        movieDetailFragment.markAsFavourite(v, currentLoaderId);
    }

    public void shareMovie(View v){
        MoviesDetailFragment movieDetailFragment = (MoviesDetailFragment)getSupportFragmentManager().findFragmentByTag(MoviesDetailFragment.TAG);
        movieDetailFragment.share(v);
    }


}